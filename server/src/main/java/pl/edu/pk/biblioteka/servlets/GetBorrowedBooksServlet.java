package pl.edu.pk.biblioteka.servlets;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import pl.edu.pk.biblioteka.dto.LoanDto;
import pl.edu.pk.biblioteka.dto.dao.LoanDao;
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

/**
 * Serwlet do wysyłania listy wypozyczen danego czytelnika
 */
public @WebServlet("/get/loans")
class GetBorrowedBooksServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(GetBorrowedBooksServlet.class.getName());

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        Map<String, String> headersInfo = WebUtils.getHeadersInfo(request);
        logger.info(headersInfo.toString());

        HttpSession session = request.getSession(false);

        if (AccessValidator.isLibrarian(session)) {
            Integer accountId = Integer.valueOf(headersInfo.get("accountid")); // Pobierz id konta z nagłówka
            sendLoans(accountId, response);
        } else if (AccessValidator.isReader(session)) {
            Integer accountId = (Integer) session.getAttribute("userId"); // Pobierz id konta z sesji
            sendLoans(accountId, response);
        }

    }

    /**
     * Odesłanie informacji o wszystkich znalzionych wypożyczeniach
     * @param accountId id konta
     * @param response
     * @throws IOException
     */
    private void sendLoans(Integer accountId, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        LoanDao loanDao = new LoanDao();
        JSONObject jsonObject = new JSONObject();

        List<LoanDto> loans = loanDao.getByAccountId(accountId);
        jsonObject.put("loans", loans);

        response.setStatus(HttpServletResponse.SC_OK);
        logger.info(jsonObject.toString());
        out.print(jsonObject.toString());
        out.flush();
        out.close();
        loanDao.close();
    }
}

