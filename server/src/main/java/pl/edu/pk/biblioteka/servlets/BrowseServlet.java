package pl.edu.pk.biblioteka.servlets;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import pl.edu.pk.biblioteka.data.dao.BookDao;
import pl.edu.pk.biblioteka.data.BookDto;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.json.JSONWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Serwlet wyszukujący książki po tytule
 */
public @Deprecated @WebServlet("/browse")
class BrowseServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(BrowseServlet.class.getName());

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String title = request.getHeader("title");
        logger.info(title);

        BookDao bookDao = new BookDao();
        List<BookDto> books = bookDao.get(title);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("books", books);

        response.setStatus(HttpServletResponse.SC_OK);
        out.print(jsonObject.toString());
        out.flush();
        bookDao.close();
    }
}
