package pl.edu.pk.biblioteka.servlets;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import pl.edu.pk.biblioteka.data.Copy;
import pl.edu.pk.biblioteka.data.ReservationStatus;
import pl.edu.pk.biblioteka.data.dao.CopyDao;
import pl.edu.pk.biblioteka.dto.AccountHistoryDto;
import pl.edu.pk.biblioteka.dto.LoanDto;
import pl.edu.pk.biblioteka.dto.dao.AccountHistoryDao;
import pl.edu.pk.biblioteka.dto.dao.LoanDao;
import pl.edu.pk.biblioteka.utils.AccessValidator;
import pl.edu.pk.biblioteka.utils.WebUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Serwlet do rezerwowania egzemplarzy
 */
public @WebServlet("/resources/reserve")
class ReserveServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ReserveServlet.class.getName());
    private static final int MAX = 3;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json"); // Odpowiedz w json
        PrintWriter out = response.getWriter(); // Strumień do odpowiedzi

        Map<String, String> headersInfo = WebUtils.getHeadersInfo(request); // Utworzenie mapy z nagłowka
        logger.info(headersInfo.toString());

        if (!headersInfo.containsKey("copyid")) { // Jeśli nie podano 'copyid' wysyłamy error i konczymy
            response.sendError(HttpServletResponse.SC_CONFLICT);
            out.flush();
            return;
        }

        HttpSession session = request.getSession(false); // Pobieramy sesje z żadania

        if (AccessValidator.isNotBlocked(session)) { // Sprawdzamy czy uzytkonik jest zalogowany / nie jest zablokowany
            LoanDao loanDao = new LoanDao();
            AccountHistoryDao history = new AccountHistoryDao();
            Integer copyId = Integer.valueOf(headersInfo.get("copyid")); // Wyciagamy z nagłówka id egzemplarza
            Integer accountId = (Integer) session.getAttribute("userId"); // Wyciagamy id konta z sesji

            List<LoanDto> all = loanDao.getByAccountId(accountId);
            List<AccountHistoryDto> historical = history.getByAccountId(accountId);
            all.removeIf(l -> historical.stream().anyMatch(h -> h.getLoan().equals(l)));
            long currentBorrowed = all.size();
            logger.info("currentBorrowed: " + currentBorrowed);

            if (currentBorrowed < MAX) { // Mniej niz max wypozyczen

                int loanId = loanDao.add(copyId, accountId); // Dodajemy wypozyczenie

                if (loanId > 0) { // Jeśli dodano
                    Optional<LoanDto> loan = loanDao.get(loanId); // pobieramy nowo dodane wypozyczenie
                    logger.info(loan + " " + loanId);
                    if (loan.isPresent()) {
                        // odsyłamy informacje o tym wypozyczeniu
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("reservation", new JSONObject(loan.get()));
                        logger.info(jsonObject.toString());
                        response.setStatus(HttpServletResponse.SC_OK);
                        out.print(jsonObject.toString());
                    } else {
                        response.sendError(HttpServletResponse.SC_CONFLICT);
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_CONFLICT);
                }
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Limit osiągnięty");
            }

            loanDao.close();
            history.close();

        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }

        out.flush();
    }
}
