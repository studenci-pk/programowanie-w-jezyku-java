package pl.edu.pk.biblioteka.servlets;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import pl.edu.pk.biblioteka.data.Ebook;
import pl.edu.pk.biblioteka.data.dao.AudiobookDao;
import pl.edu.pk.biblioteka.data.dao.EbookDao;
import pl.edu.pk.biblioteka.utils.AccessValidator;
import pl.edu.pk.biblioteka.utils.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Dodawanie e-zasobu
 */
public @WebServlet("/add/resource") @MultipartConfig
class AddResourcesServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(AddResourcesServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info(request.getHeaderNames().toString());
        HttpSession session = request.getSession(false);
        boolean success = false;

        if (AccessValidator.isLibrarian(session)) {
            Collection<Part> parts = request.getParts();
            logger.info(parts.size());

            Part bookPart = request.getPart("bookid");
            Part upFile = request.getPart("upfile");

            InputStream inputStream = bookPart.getInputStream();
            StringWriter writer = new StringWriter();
            IOUtils.copy(inputStream, writer);
            String theString = writer.toString();
            Integer bookId = Integer.valueOf(theString);

            if (upFile.getSubmittedFileName().matches("(?i).+\\.mp3")) { // Jeśli audibook ('(?i)' ignoruje wielkosc znakow)
                AudiobookDao audiobookDao = new AudiobookDao();
                int audiobookId = audiobookDao.add(bookId);
                if (audiobookId > 0) {
                    if (saveFile(String.format("audiobooks/%d.mp3", audiobookId), upFile)) {
                        success = true;
                    }
                }

            } else if (upFile.getSubmittedFileName().matches("(?i).+\\.pdf")) { // Jeśli ebook
                EbookDao ebookDao = new EbookDao();
                int ebookId = ebookDao.add(bookId);
                if (ebookId > 0) {
                    if (saveFile(String.format("ebooks/%d.pdf", ebookId), upFile)) {
                        success = true;
                    }
                }
            }

            if (success) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }

    /**
     * Zapis do pliku
     * @param fileName nazwa pliku
     * @param filePart
     * @return
     */
    private boolean saveFile(String fileName, Part filePart) {
        String rootPath = getClass().getResource("/").getPath();
        File file = new File(rootPath + File.separator + fileName);

        logger.info(file.getAbsolutePath());
        if (!file.exists()) {
            boolean status = file.mkdirs();
            logger.info(status);
        }

        try (OutputStream out = new FileOutputStream(file);
             InputStream fileContent = filePart.getInputStream()) {

            final byte[] bytes = new byte[1024];

            int read;
            while ((read = fileContent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            return true;

        } catch (IOException e) {
            logger.error(e);
        }

        return false;
    }
}
