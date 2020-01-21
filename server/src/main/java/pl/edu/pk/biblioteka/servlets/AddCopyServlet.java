package pl.edu.pk.biblioteka.servlets;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import pl.edu.pk.biblioteka.data.Copy;
import pl.edu.pk.biblioteka.data.dao.CopyDao;
import pl.edu.pk.biblioteka.utils.AccessValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

public @WebServlet("/resources/add/copy")
class AddCopyServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(AddCopyServlet.class.getName());

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        Integer bookId = Integer.valueOf(request.getHeader("bookId"));

        HttpSession session = request.getSession(false);

        if (AccessValidator.isLibrarian(session)) {
            CopyDao copyDao = new CopyDao();
            Optional<Copy> copy = copyDao.add(bookId);
            JSONObject jsonObject = new JSONObject();
            if (copy.isPresent()) {
                Copy c = copy.get();
                logger.info(jsonObject.put("copy", new JSONObject(c)));
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendError(HttpServletResponse.SC_CONFLICT);
                jsonObject.put("message", "error");
            }

            out.print(jsonObject.toString());
            out.flush();
            copyDao.close();
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}

