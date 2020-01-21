package pl.edu.pk.biblioteka.servlets;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import pl.edu.pk.biblioteka.dto.LibrarianDto;
import pl.edu.pk.biblioteka.dto.ReaderDto;
import pl.edu.pk.biblioteka.dto.dao.LibrarianDao;
import pl.edu.pk.biblioteka.dto.dao.ReaderDao;
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

public @WebServlet("/browse/account/details")
class GetAccountsDetailsServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(GetAccountsDetailsServlet.class.getName());

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);

        if (AccessValidator.isLibrarian(session)) {
            Map<String, String> headersInfo = WebUtils.getHeadersInfo(request);
            logger.info(headersInfo.toString());

            JSONObject jsonObject = new JSONObject();

            response.setStatus(HttpServletResponse.SC_OK);
            out.print(jsonObject.toString());
            logger.info(jsonObject.toString());
            out.flush();
        }
    }
}
