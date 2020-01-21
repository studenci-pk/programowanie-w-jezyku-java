package pl.edu.pk.biblioteka.servlets;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import pl.edu.pk.biblioteka.data.Account;
import pl.edu.pk.biblioteka.data.dao.AccountDao;
import pl.edu.pk.biblioteka.utils.AccessValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.function.Consumer;

public @WebServlet("/accounts")
class AccountsServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(AccountsServlet.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);

        if (AccessValidator.isLibrarian(session)) {
            AccountDao accountDao = new AccountDao();
            List<Account> accounts = accountDao.getAll();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("accounts", accounts);
            out.print(jsonObject.toString());
            out.flush();
            accountDao.close();

            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Nieautoryzowany dostęp");
        }

    }
}
