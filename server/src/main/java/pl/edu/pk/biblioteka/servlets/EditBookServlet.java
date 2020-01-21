package pl.edu.pk.biblioteka.servlets;

import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.dto.dao.BookDao;
import pl.edu.pk.biblioteka.utils.AccessValidator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public @WebServlet("/edit/book")
class EditBookServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(EditBookServlet.class.getName());

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int bookId = Integer.valueOf(request.getParameter("bookId"));
        String title = request.getParameter("title");
        String keywords = request.getParameter("keywords");
        String category = request.getParameter("category");
        Integer authorId = Integer.valueOf(request.getParameter("authorId"));
        Integer publisherId = Integer.valueOf(request.getParameter("publisherId"));
        Integer departmentId = Integer.valueOf(request.getParameter("departmentId"));

        logger.info(title);
        logger.info(keywords);
        logger.info(category);
        logger.info(authorId);
        logger.info(publisherId);
        logger.info(departmentId);

        HttpSession session = request.getSession(false);

        if (AccessValidator.isLibrarian(session)) {
            BookDao bookDao = new BookDao();
            //Update ksiazki
            int i = bookDao.update(bookId, authorId, publisherId, departmentId, title, category, keywords);

            if (i > 0) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }

            bookDao.close();
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
