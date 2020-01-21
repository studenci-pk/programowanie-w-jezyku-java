package pl.edu.pk.biblioteka.servlets;

import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.dto.AccountHistoryDto;
import pl.edu.pk.biblioteka.dto.LoanDto;
import pl.edu.pk.biblioteka.dto.dao.AccountHistoryDao;
import pl.edu.pk.biblioteka.dto.dao.LoanDao;
import pl.edu.pk.biblioteka.utils.AccessValidator;
import pl.edu.pk.biblioteka.utils.DatabaseConnector;
import pl.edu.pk.biblioteka.utils.WebUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

/**
 * Serwlet do przyjmowania zamówień
 */
public @WebServlet("/reservation/accepted")
class ReservationUpdateServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ReservationUpdateServlet.class.getName());

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json"); // Odpowiedź w json
        PrintWriter out = response.getWriter(); // Strumień dla odpowiedzi

        Map<String, String> headersInfo = WebUtils.getHeadersInfo(request); // Mapa utwrzona z nagłówka
        logger.info(headersInfo.toString());

        if (!headersInfo.containsKey("loanid")) { // jeśli nie przesłano id wypozyczenia to wysyłamy error i konczymy
            response.sendError(HttpServletResponse.SC_CONFLICT);
            out.flush();
            return;
        }

        HttpSession session = request.getSession(false); // Pobieramy sesje z żadania (jeśli jest)
        boolean success = false;

        try {
            if (AccessValidator.isLibrarian(session)) { // Jeśli użytkownik to pracownik
                Integer loanId = Integer.valueOf(headersInfo.get("loanid")); // Pobieramy id wypozyczenia

                Connection connection = DatabaseConnector.getConnection(); // Utworznie połączenia z bazą
                connection.setAutoCommit(false); // Wyłączenie automatycznych commitów
                LoanDao loanDao = new LoanDao(connection); // Utworzenie dao z utworzonym wczesniej połączeniem
                Optional<LoanDto> loan = loanDao.get(loanId); // Pobranie wypozyczenia o podanym id

                if (loan.isPresent()) {
                    logger.info(loan.get().toString());
                    LoanDto l = loan.get(); // Wyciągnięcie 'LoanDto' z Optional-a

                    Integer reservationStatusId = Integer.valueOf(headersInfo.get("reservationstatusid")); // Pobranie id statusu jaki ma zostac wpisany
                    l.getReservationStatus().setReservationStatusId(reservationStatusId); // ustawienie nowego statusu id

                    if (loanDao.update(l) > 0) { // jesli update się udał
                        AccountHistoryDao accountHistoryDao = new AccountHistoryDao(connection); // Utworzenie dao z utworzonym wczesniej połączeniem
                        accountHistoryDao.add(l.getAccount(), l); // Dodanie do wypozyczneia do histori wypozyczeń
                        success = true;
                    }
                }

                if (success) {
                    connection.commit(); // zatwierdz zmiany
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    connection.rollback(); // cofaj zmiany
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }

                connection.setAutoCommit(true);
                connection.close(); // Zamknij połączenie
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            out.flush();
        }
    }
}
