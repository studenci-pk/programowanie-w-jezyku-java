package pl.edu.pk.biblioteka.servlets;

import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.dto.BookDto;
import pl.edu.pk.biblioteka.dto.dao.BookDao;
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
import java.util.Optional;

/**
 * Serwlet do odblokowania książek
 */
public @WebServlet("/book/restore")
class RestoreBookServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(RestoreBookServlet.class.getName());

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (AccessValidator.isLibrarian(session)) {
            Map<String, String> headersInfo = WebUtils.getParametersInfo(request);
            Integer bookId = Integer.valueOf(headersInfo.get("bookid"));
            BookDao bookDao = new BookDao();
            Optional<BookDto> book = bookDao.get(bookId);
            boolean succeed = false;

            logger.info(book.isPresent());
            if (book.isPresent()) { //jesli znaleziono ksiazke ustaw na wycofana i zrob update
                BookDto b = book.get();
                b.setWithdrawn(false);
                succeed = bookDao.update(b) > 0;
            }

            if (succeed) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            bookDao.close();

        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }
}
