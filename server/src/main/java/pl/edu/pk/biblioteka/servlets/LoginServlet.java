package pl.edu.pk.biblioteka.servlets;

import org.apache.log4j.Logger;
//import org.json.simple.JSONObject;
//import org.json.JSONObject;
import org.json.JSONObject;
import org.springframework.util.DigestUtils;
import pl.edu.pk.biblioteka.data.Account;
import pl.edu.pk.biblioteka.data.Permissions;
import pl.edu.pk.biblioteka.data.dao.AccountDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

/**
 * Serwlet do logowania
 */
public @WebServlet("/login")
class LoginServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(LoginServlet.class);
    private final int FIVETEEN_MINUTES = 60 * 15;

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Odebranie przesłanych parametrów
        String login = request.getParameter("username");
        String password = request.getParameter("userpass");
        password = DigestUtils.md5DigestAsHex(password.getBytes()); // Zaszyfrowanie przesłąengo hasła

        AccountDao accountDao = new AccountDao();
        Optional<Account> account = accountDao.get(login, password); // Szukanie konta o takim lognie i haśle

        if (account.isPresent()){
            Account acc = account.get(); // Wyciągnięcie 'Account' z 'Optional'

            HttpSession session = request.getSession(); // Utworzenie sesji i ustawienie dla niej parametrów
            session.setAttribute("userId", acc.getAccountId());
            session.setAttribute("permissionId", acc.getPermissionId());
            session.setMaxInactiveInterval(FIVETEEN_MINUTES);
            response.setStatus(HttpServletResponse.SC_OK);

            // Odesłanie informacji o typie konta na jakie się zalogowano
            JSONObject jsonObject = new JSONObject();
            switch (account.get().getPermissionId()) {
                case Permissions.LIBRARIAN_ACCESS:
                    jsonObject.put("accountType", "librarian");
                    break;
                case Permissions.COMMON_READER:
                    jsonObject.put("accountType", "reader");
                    break;
                default:
                    jsonObject.put("accountType", JSONObject.NULL);
            }

            out.print(jsonObject.toString());
        } else {
            response.sendError(HttpServletResponse.SC_CONFLICT, "Podane nieprawidłowe dane");
        }

        out.close();
        accountDao.close();
    }
}