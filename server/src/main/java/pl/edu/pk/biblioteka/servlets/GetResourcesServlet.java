package pl.edu.pk.biblioteka.servlets;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import pl.edu.pk.biblioteka.data.Audiobook;
import pl.edu.pk.biblioteka.data.Copy;
import pl.edu.pk.biblioteka.data.Ebook;
import pl.edu.pk.biblioteka.data.ReservationStatus;
import pl.edu.pk.biblioteka.data.dao.AudiobookDao;
import pl.edu.pk.biblioteka.data.dao.CopyDao;
import pl.edu.pk.biblioteka.data.dao.EbookDao;
import pl.edu.pk.biblioteka.dto.AccountHistoryDto;
import pl.edu.pk.biblioteka.dto.BookDto;
import pl.edu.pk.biblioteka.dto.LoanDto;
import pl.edu.pk.biblioteka.dto.dao.AccountHistoryDao;
import pl.edu.pk.biblioteka.dto.dao.BookDao;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Serwlet wysyłający wszytkie zasoby (egzemplarze, e/audio booki, )
 */
public @WebServlet("/resources/all")
class GetResourcesServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(GetResourcesServlet.class.getName());

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        Map<String, String> headersInfo = WebUtils.getHeadersInfo(request);
//        Map<String, String> filteredMap = headersInfo.entrySet().stream()
//                .filter((e) -> e.getKey().matches("signature|author|publisher" +
//                        "|department|title|category|keywords") && !e.getValue().isEmpty())
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        logger.info(headersInfo.toString());
//        logger.info(filteredMap.toString());

        HttpSession session = request.getSession(false);

        if (AccessValidator.isNotBlocked(session)) {
            Integer accountId = (Integer) session.getAttribute("userId");
            LoanDao loanDao = new LoanDao();
            CopyDao copyDao = new CopyDao();
            AudiobookDao audiobookDao = new AudiobookDao();
            EbookDao ebookDao = new EbookDao();
            AccountHistoryDao accountHistoryDao = new AccountHistoryDao();
            JSONObject jsonObject = new JSONObject();

            if (headersInfo.containsKey("bookid")) {
                int bookId = Integer.valueOf(headersInfo.get("bookid"));
                // Niewypozycznone egzemplarze
                List<LoanDto> loans = loanDao.getByAccountId(accountId);
                List<Copy> copies = copyDao.getAll(bookId);
                logger.info("bookId = " + bookId + "\nloans.size = " + loans.size() + "\ncopies.size = " + copies.size());

                // Wyrzucamy historyczne wypozyczenia i przesyłamy tylko te egzemplarze ktore nie sa wypozyczone przez tego klienta
                List<AccountHistoryDto> historyDtos = accountHistoryDao.getByAccountId(accountId); // Pobranie historycznych wypozyczen
                logger.info("historyDtos: " + historyDtos.size());
                loans.removeIf(loan -> historyDtos.stream().anyMatch(hist -> hist.getLoan().equals(loan))); // Usuięcie historychych wypozyczen z wszystkihc wypozyczen
                logger.info("loans: " + loans.size());
                copies.removeIf(copy -> loans.stream().anyMatch(loan -> loan.getCopy().equals(copy))); // Usuniecie wypozyczonych egzemplarzy
                logger.info("copies: " + copies.size());

                if (true) { // ukryj wycofane egzemplarze
                    copies = copies.stream().filter(copy -> !copy.isWithdrawn()).collect(Collectors.toList());
                    logger.info("copies: " + copies.size());
                }

                jsonObject.put("copies", copies); // Wpisanie tabicy z egzemplarzami (ich danymi)

                // Audibooki
                List<Audiobook> audiobooks = audiobookDao.getAll(bookId);

                if (!audiobooks.isEmpty()) {
                    jsonObject.put("audiobooks", audiobooks);
                }

                // Ebooki
                List<Ebook> ebooks = ebookDao.getAll(bookId);

                if (!ebooks.isEmpty()) {
                    jsonObject.put("ebooks", ebooks);
                }
            }

            response.setStatus(HttpServletResponse.SC_OK);
            logger.info(jsonObject.toString());
            out.print(jsonObject.toString());
            out.flush();
            copyDao.close();
            loanDao.close();
            ebookDao.close();
            audiobookDao.close();
            accountHistoryDao.close();
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}

