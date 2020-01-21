package pl.edu.pk.biblioteka.servlets;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import pl.edu.pk.biblioteka.dto.dao.BookDao;
import pl.edu.pk.biblioteka.dto.BookDto;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Wyszukiwanie książek
 */
public @WebServlet("/browse/books")
class GetBooksServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(GetBooksServlet.class.getName());

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        Map<String, String> headersInfo = WebUtils.getHeadersInfo(request);
        Map<String, String> filteredMap = headersInfo.entrySet().stream() // Strumien
                .filter((e) -> e.getKey().matches("signature|author|publisher" + // Wybranie wybranych, niepustych wartości
                        "|department|title|category|keywords") && !e.getValue().isEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)); // Utworzenie mapy
        logger.info(headersInfo.toString());
        logger.info(filteredMap.toString());

        BookDao bookDao = new BookDao();
        List<BookDto> books = bookDao.getByMap(filteredMap); // Wyszukanie książek na podstawie przesłanych parametrów (zawartych w mapie)

        HttpSession session = request.getSession(false);

        if (!AccessValidator.isLibrarian(session)) { // Jeśli nie bibliotkarz ukryj wycofane ksiazki
            books = books.stream().filter(book -> !book.isWithdrawn()).collect(Collectors.toList());
        }

        logger.info("Size: " + books.size());
        logger.info("Books: " + books.toString());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("books", books);
        logger.info(books.toString());

        response.setStatus(HttpServletResponse.SC_OK);
        out.print(jsonObject.toString());
        out.flush();
        books.clear();
    }
}
