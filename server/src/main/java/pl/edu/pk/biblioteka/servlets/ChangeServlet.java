package pl.edu.pk.biblioteka.servlets;

import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.dto.LibrarianDto;
import pl.edu.pk.biblioteka.dto.ReaderDto;
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
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * Serwlet to edycji danych konta
 */
public @WebServlet("/change")
class ChangeServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ChangeServlet.class);

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        Map<String, String> parametersInfo = WebUtils.getParametersInfo(request);
        boolean success = false;

        if (AccessValidator.isLibrarian(session)) {
            Integer userId = (Integer) session.getAttribute("userId");

            LibrarianDao librarianDao = new LibrarianDao();
            Optional<LibrarianDto> librarian = librarianDao.getByAccountId(userId);

            logger.info(librarian.isPresent());
            if (librarian.isPresent()) {
                LibrarianDto l = librarian.get();

                parametersInfo.forEach((key, value) -> {
                    switch (key.toLowerCase()) {
                        case "name":
                            l.setName(value);
                            break;
                        case "surname":
                            l.setSurname(value);
                            break;
                        case "email":
                            l.getAccount().setEmail(value);
                            break;
                    }
                });

                String oldPassword = parametersInfo.get("oldpassword");
                String newPassword = parametersInfo.get("newpassword");
                if (oldPassword != null && newPassword != null) {
                    if (l.getAccount().getPassword().equalsIgnoreCase(oldPassword)) {
                        l.getAccount().setPassword(newPassword);
                    }
                }

                if (librarianDao.update(l) > 0) {
                    success = true;
                }
            }
            librarianDao.close();

        } else if (AccessValidator.isReader(session)) {
            Integer userId = (Integer) session.getAttribute("userId");


            ReaderDao readerDao = new ReaderDao();
            Optional<ReaderDto> reader = readerDao.getByAccountId(userId);

            logger.info(reader.isPresent());
            if (reader.isPresent()) {
                ReaderDto r = reader.get();

                parametersInfo.forEach((key, value) -> {
                    switch (key.toLowerCase()) {
                        case "name":
                            r.setName(value);
                            break;
                        case "surname":
                            r.setSurname(value);
                            break;
                        case "email":
                            r.getAccount().setEmail(value);
                    }
                });

                String oldPassword = parametersInfo.get("oldpassword");
                String newPassword = parametersInfo.get("newpassword");
                if (oldPassword != null && newPassword != null) {
                    if (r.getAccount().getPassword().equalsIgnoreCase(oldPassword)) {
                        r.getAccount().setPassword(newPassword);
                    }
                }

                if (readerDao.update(r) > 0) {
                    success = true;
                }
            }
            readerDao.close();
        }


        if (success) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        out.flush();
        out.close();
    }
}
