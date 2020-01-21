package pl.edu.pk.biblioteka.servlets;

import org.apache.log4j.Logger;
import org.json.JSONObject;
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
import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Serwlet dodający wypozyczenia. Nie uzywany - przestarzaly
 */
public @Deprecated @WebServlet("/loan")
class LoanServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(LoanServlet.class.getName());

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        Map<String, String> headersInfo = WebUtils.getHeadersInfo(request);
        Map<String, String> filteredMap = headersInfo.entrySet().stream()
                .filter((e) -> e.getKey().matches("login|name|surname") && !e.getValue().isEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        logger.info(headersInfo.toString());
        logger.info(filteredMap.toString());

        if (AccessValidator.isNotBlocked(session)) {
            int accountId = (Integer) session.getAttribute("userId");
            int copyId = 1;
            int reservationStatusId = 1;
            int chargeId = 1;
            Calendar calendar = Calendar.getInstance();

            LoanDao loanDao = new LoanDao();
            int responseCode = loanDao.add(copyId,
                    accountId,
                    reservationStatusId,
                    chargeId,
                    new Date(calendar.getTimeInMillis()),
                    new Date(calendar.getTimeInMillis()),
                    "Biblioteka",
                    new Date(calendar.getTimeInMillis()),
                    new Date(calendar.getTimeInMillis()));

            logger.info(responseCode);
            loanDao.close();
        }
    }

    private Date add(Calendar calendar, int days) {
        calendar.add(Calendar.DATE, days);
        long l = LocalDate.now().plusDays(7).toEpochDay();
        logger.info(l + " <> " + calendar.getTimeInMillis());

        return new Date(calendar.getTimeInMillis());
    }
}
