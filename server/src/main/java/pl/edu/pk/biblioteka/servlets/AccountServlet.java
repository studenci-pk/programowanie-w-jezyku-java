package pl.edu.pk.biblioteka.servlets;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import pl.edu.pk.biblioteka.dto.dao.LibrarianDao;
import pl.edu.pk.biblioteka.dto.LibrarianDto;
import pl.edu.pk.biblioteka.dto.dao.ReaderDao;
import pl.edu.pk.biblioteka.dto.ReaderDto;
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
import java.util.Map;
import java.util.Optional;

public @WebServlet("/account")
class AccountServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(AccountServlet.class.getName());

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        boolean success = false;

        if (AccessValidator.isLibrarian(session)) {
            Integer userId = (Integer) session.getAttribute("userId");
            LibrarianDao librarianDao = new LibrarianDao();
            Optional<LibrarianDto> librarian = librarianDao.getByAccountId(userId);
            logger.info(librarian.isPresent());
            if (librarian.isPresent()) {
                LibrarianDto lib = librarian.get();

                JSONObject responseJson = new JSONObject();
                responseJson.put("librarian", new JSONObject(lib));
                logger.info(responseJson.toString());
                out.print(responseJson.toString());
                librarianDao.close();
                success = true;
            }
        } else if (AccessValidator.isReader(session)) {
            Integer userId = (Integer) session.getAttribute("userId");
            ReaderDao readerDao = new ReaderDao();
            Optional<ReaderDto> reader = readerDao.getByAccountId(userId);
            if (reader.isPresent()) {
                ReaderDto red = reader.get();

                JSONObject responseJson = new JSONObject();
                responseJson.put("reader", new JSONObject(red));
                logger.info(responseJson.toString());
                out.print(responseJson.toString());
                readerDao.close();
                success = true;
            }
        }

        if (success) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Nieautoryzowany dostęp");
        }
        out.flush();
    }
}
