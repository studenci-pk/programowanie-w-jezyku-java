package pl.edu.pk.biblioteka.servlets;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import pl.edu.pk.biblioteka.dto.*;
import pl.edu.pk.biblioteka.dto.dao.LibrarianDao;
import pl.edu.pk.biblioteka.dto.dao.ReaderDao;
import pl.edu.pk.biblioteka.utils.AccessValidator;
import pl.edu.pk.biblioteka.utils.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Serwlet wyszukujący konta uzytkoników
 */
public @WebServlet("/browse/accounts")
class GetAccountsServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(GetAccountsServlet.class.getName());

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);

        if (AccessValidator.isLibrarian(session)) {
            Map<String, String> headersInfo = WebUtils.getHeadersInfo(request);
            logger.info(headersInfo.toString());

            JSONObject jsonObject = new JSONObject();

            Map<String, String> filteredMap = headersInfo.entrySet().stream()
                    .filter((e) -> e.getKey().matches("login|name|surname") && !e.getValue().isEmpty())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            logger.info(headersInfo.toString());
            logger.info(filteredMap.toString());

            // Jeśli mamy przesłać pracowników
            if (Boolean.valueOf(headersInfo.getOrDefault("librarians", "false"))) {
                LibrarianDao librarianDao = new LibrarianDao();
                List<LibrarianDto> librarians = librarianDao.getByMap(filteredMap);
                jsonObject.put("librarians", librarians);

                logger.info(librarians.toString());
                librarianDao.close();
            }

            // Jeśli mamy przesłać czytelników
            if (Boolean.valueOf(headersInfo.getOrDefault("readers", "false"))) {
                ReaderDao readerDao = new ReaderDao();
                List<ReaderDto> readers = readerDao.getByMap(filteredMap);
                jsonObject.put("readers", readers);

                logger.info(readers.toString());
                readerDao.close();
            }


            response.setStatus(HttpServletResponse.SC_OK);
            out.print(jsonObject.toString());
            logger.info(jsonObject.toString());
            out.flush();
        }
    }
}
