package pl.edu.pk.biblioteka.servlets;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import pl.edu.pk.biblioteka.dto.AudiobookDto;
import pl.edu.pk.biblioteka.dto.EbookDto;
import pl.edu.pk.biblioteka.dto.dao.AudiobookDao;
import pl.edu.pk.biblioteka.dto.dao.EbookDao;
import pl.edu.pk.biblioteka.utils.AccessValidator;
import pl.edu.pk.biblioteka.utils.WebUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * Serwlet do wysyłanie e-zasobów
 */
public @WebServlet("/download")
class DownloadServlet extends HttpServlet {
    private static Logger logger = Logger.getLogger(DownloadServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Map<String, String> headersInfo = WebUtils.getHeadersInfo(request);
        logger.info(headersInfo.toString());
        boolean success = false;

        logger.info(AccessValidator.isNotBlocked(session));
        if (AccessValidator.isNotBlocked(session)) {
            if (headersInfo.containsKey("audiobookid")) { // jeśli ktoś chce otrzymać audiobooka
                int audiobookId = Integer.valueOf(headersInfo.get("audiobookid")); // konwersja na int
                AudiobookDao audiobookDao = new AudiobookDao();
                Optional<AudiobookDto> audiobook = audiobookDao.get(audiobookId);

                logger.info(audiobook.isPresent());
                if (audiobook.isPresent()) { // jeśli jest taki audibook
                    success = true;

                    AudiobookDto a = audiobook.get();
                    logger.info(a);

                    String path = getClass().getResource("/").getPath(); // Sciezka do zasobow
                    File file = new File(path + "/audiobooks/" + a.getAudiobookId() + ".mp3"); // Plik z szukanym audibookiem

                    response.setContentType("application/mp3");
                    response.addHeader("Content-Disposition", // Dodanie nagłówka m.in z nazwa pliku
                            "attachment; filename=" + String.format("%s.mp3", a.getBook().getTitle()));
                    response.setContentLength((int) file.length()); // Ustawienie wielkosci pliku

                    InputStream fileInputStream = new FileInputStream(file);
                    ServletOutputStream outputStream = response.getOutputStream();

                    int read;
                    byte[] bytes = new byte[1024];

                    // Wysylanie pliku
                    while ((read = fileInputStream.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, read);
                    }

                    outputStream.flush();
                    outputStream.close();
                }

            } else if (headersInfo.containsKey("ebookid")) { // Analogicznie
                int ebookId = Integer.valueOf(headersInfo.get("ebookid"));

                EbookDao ebookDao = new EbookDao();
                Optional<EbookDto> ebook = ebookDao.get(ebookId);

                if (ebook.isPresent()) {
                    success = true;

                    EbookDto e = ebook.get();

                    String path = getClass().getResource("/").getPath();
                    File file = new File(path + "/ebooks/" + e.getEbookId() + ".pdf");

                    response.setContentType("application/pdf");
                    response.addHeader("Content-Disposition",
                            "attachment; filename=" + String.format("%s.pdf", e.getBook().getTitle()));
                    response.setContentLength((int) file.length());

                    InputStream fileInputStream = new FileInputStream(file);
                    ServletOutputStream outputStream = response.getOutputStream();
                    logger.info(fileInputStream.available());

                    int read;
                    byte[] bytes = new byte[1024];

                    while ((read = fileInputStream.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, read);
                    }

                    outputStream.flush();
                    outputStream.close();
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