package pl.edu.pk.biblioteka.servlets;

import pl.edu.pk.biblioteka.dto.dao.CartDao;
import pl.edu.pk.biblioteka.utils.AccessValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 * Dodawanie do koszyka
 */
public @WebServlet("/add-to-cart")
class AddToCartServlet extends HttpServlet {
    private static final Logger logger= Logger.getLogger(AddToCartServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String tytul = request.getHeader("tytul");
        logger.info("tytul: " + tytul);

        HttpSession session = request.getSession(false);

        if (AccessValidator.isReader(session)) {
            Integer permissionId = (Integer) session.getAttribute("permissionId");
            Integer accountId = (Integer) session.getAttribute("userId");

            CartDao cartDao = new CartDao();
            int i = cartDao.add(tytul, accountId);

            if (i > 0) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Niepoprawne dane logowania");
        }
        out.close();
    }
}
