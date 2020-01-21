package pl.edu.pk.biblioteka.servlets;

import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.data.Permissions;
import pl.edu.pk.biblioteka.data.dao.AccountDao;
import pl.edu.pk.biblioteka.data.Account;
import pl.edu.pk.biblioteka.dto.dao.LibrarianDao;
import pl.edu.pk.biblioteka.dto.dao.ReaderDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Optional;

/**
 * Serwlet do rejestrownia uzytkowników
 */
public @WebServlet("/register")
class RegisterServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(RegisterServlet.class.getName());

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html"); // odpowiadamy w html
        PrintWriter out = response.getWriter(); // strumień do odpowiedzi


        // Wyciągnięcie przesąłnych warotści
        String accType = request.getParameter("accType");

        String pesel = request.getParameter("pesel");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");

        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        String faculty = request.getParameter("faculty");
        String subject = request.getParameter("subject");


        AccountDao accountDao = new AccountDao();
        Optional<Account> account = accountDao.get(login); // Wyszukanie konta o taki samym loginie

        if(account.isPresent()) { // error jeśli jest konto o takim loginie
            response.sendError(HttpServletResponse.SC_CONFLICT, "Podany login jest już w użyciu!");
            out.close();
            return;
        }

        int i = 0;
        if (accType.equalsIgnoreCase("librarian")) { // Jeśli pracownik się rejestruje
            LibrarianDao librarianDao = new LibrarianDao();
            try {
                // Dodanie konta biblitokearza
                i = librarianDao.add(pesel, name, surname,
                        login, password, Optional.ofNullable(email),
                        new Date(Calendar.getInstance().getTime().getTime()),
                        Permissions.BLOCKED_ACCOUNT);
            } catch (SQLException e) {
                logger.error(e);
            }
            librarianDao.close();
        } else { // Jeśli czytelnik się rejestruje
            ReaderDao readerDao = new ReaderDao();
            try {
                // Dodanie kotna czytelnika
                i = readerDao.add(pesel, name, surname, faculty, subject,
                        login, password, Optional.ofNullable(email),
                        new Date(Calendar.getInstance().getTime().getTime()),
                        Permissions.BLOCKED_ACCOUNT);
            } catch (SQLException e) {
                logger.error(e);
            }
            readerDao.close();
        }

        if (i == 1) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        out.close();
        accountDao.close();
    }
}
