package pl.edu.pk.biblioteka.servlets;

import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.data.dao.PublisherDao;
import pl.edu.pk.biblioteka.utils.AccessValidator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Serwlet dodający nowego wydawce
 */
public@WebServlet("/add-publisher")
class AddPublisherServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(AddPublisherServlet.class.getName());

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String name = request.getHeader("name");
        String address = request.getHeader("address");
        String country = request.getHeader("country");
        logger.info("name: " + name + "\naddress: " + address + "\ncountry: " + country);

        HttpSession session = request.getSession(false);

        if (AccessValidator.isLibrarian(session)) {
            PublisherDao publisherDao = new PublisherDao();
            int i = publisherDao.add(name, address, country);

            if (i > 0) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            publisherDao.close();
        }

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Niepoprawne dane logowania");
        out.close();
    }
}
