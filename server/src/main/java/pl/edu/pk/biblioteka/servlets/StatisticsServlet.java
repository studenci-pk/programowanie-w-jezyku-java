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
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Serwlet do wysyłania (miesięcznych) statystyk za ostatni rok
 */
public @WebServlet("/get/stats")
class StatisticsServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(StatisticsServlet.class);

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json"); // odpowiadamy w json
        PrintWriter out = response.getWriter(); // pobieramy strumień

        HttpSession session = request.getSession(false); // pobieramy sesje z rządania

        if (AccessValidator.isLibrarian(session)) { // sprawdzamy czy użytkownk jest biblitekarzem
            Map<String, String> headersInfo = WebUtils.getHeadersInfo(request); // Tworzymy 'Map'e z nagłowka
            logger.info(headersInfo.toString());

            LoanDao loanDao = new LoanDao();

            List<LoanDto> loans = loanDao.getAll(); // Pobieramy wszytkie wypozyczenia z bazy
            logger.info(loans.toString());

            List<LoanDto> filteredLoans = loans.stream() // Pobieramy strumien
                    .filter(loan -> loan.getReservationDate() != null) // Odrzucamy wypozyczenia bez daty
                    .collect(Collectors.toList()); // Tworzymy liste tylko z wypozyczeniami z data
            logger.info(filteredLoans.toString());

            List<Date> dates = filteredLoans.stream() // Pobieramy strumien
                    .map(LoanDto::getReservationDate) // Wyciągamy tylko daty z obiektu 'LoanDto'
                    .collect(Collectors.toList()); // Tworzymy liste wyciagnietych dat
            logger.info(filteredLoans.toString());


            //TODO: usunac ta petle (testowa petla)
            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < new Random().nextInt(30); j++) {
                    dates.add(Date.valueOf(String.format("2020-%02d-01", i+1)));
                }
            }

            logger.info(dates.toString());

            int currYear = Calendar.getInstance() // Utworznie aktualengo kalendarza
                    .get(Calendar.YEAR); // Pobranie aktualnego roku
            Map<String, Long> borrowedBooksPerMonth = dates.stream() // Pobranie strumienia
                    .filter(
                            o -> new Calendar.Builder() // Utworznie 'budowniczego' calendarza
                            .setInstant(o) // ustawienie daty na podstawie obiektu sql.Date
                            .build() // 'Zbudowanie' kalendarza
                                    .get(Calendar.YEAR) == currYear) // Filtrowanie tylko tych dat z tego roku
                    .collect( // Tworzenie mapy <Miesiac, Liczba wypozyczen>
                    Collectors.groupingBy( // Grupowanie według miesiecy
                            o -> new SimpleDateFormat("MMM").format( // Utworzenie formatu wyświetlania dat
                            new Calendar.Builder()
                                    .setInstant(o)
                                    .build().getTime()), // Utworzenie obiektu Date, na podstawie którgo odbywa sie grupowanie
                            Collectors.counting())); // Policzenie wystąpień

            JSONObject jsonObject = new JSONObject(); // Utworzenie obiektu JSONObject
            logger.info(borrowedBooksPerMonth.toString());
            jsonObject.put("months", borrowedBooksPerMonth); // Wpisanie talicy 'months' do obiektu json
            out.print(jsonObject.toString()); // Wpisanie w postaci tekstu obiektu json
            out.flush();
            out.close();

            loanDao.close();
        }
    }
}
