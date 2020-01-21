package pl.edu.pk.biblioteka.servlets;

import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.dto.dao.BookDao;
import pl.edu.pk.biblioteka.dto.dao.CartDao;
import pl.edu.pk.biblioteka.utils.AccessValidator;
import pl.edu.pk.biblioteka.utils.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * Serwlet do usuwania z koszyka
 */
public @WebServlet("/cart/remove")
class RemoveCartServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(RemoveCartServlet.class.getName());

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false); // Pobranie sesji

        if (AccessValidator.isReader(session)) { // Jeśli użytkownik to czytelnik
            Map<String, String> headersInfo = WebUtils.getParametersInfo(request); // Utwórz mapę na podstawie przesłanych parametrów
            String name = headersInfo.get("name");
            Integer accountId = (Integer) session.getAttribute("userId"); // konsersja id konta pobranego z sesji
            CartDao cartDao = new CartDao();
            boolean succeed = cartDao.remove(name, accountId); // Usunięcie z koszyka

            if (succeed) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            cartDao.close();

        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }
}
