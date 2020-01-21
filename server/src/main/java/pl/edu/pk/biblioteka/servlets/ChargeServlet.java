package pl.edu.pk.biblioteka.servlets;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import pl.edu.pk.biblioteka.dto.LoanDto;
import pl.edu.pk.biblioteka.dto.dao.LoanDao;
import pl.edu.pk.biblioteka.utils.WebUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Optional;

/**
 * Serwlet do usuwania/akceptacji naleznosci
 */
public @WebServlet("/charge")
class ChargeServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ChargeServlet.class.getName());

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        Map<String, String> headersInfo = WebUtils.getHeadersInfo(request);
        logger.info(headersInfo.toString());

        if (!headersInfo.containsKey("loanid")) {
            response.sendError(HttpServletResponse.SC_CONFLICT);
            out.flush();
            return;
        }

        Integer loanId = Integer.valueOf(headersInfo.get("loanid"));

        LoanDao loanDao = new LoanDao();
        Optional<LoanDto> loan = loanDao.get(loanId);

        if (loan.isPresent()) {
            LoanDto l = loan.get();
            l.setCharge(null);
            int responseCode = loanDao.update(l);

            if (responseCode > 0) {
                response.setStatus(HttpServletResponse.SC_OK);
                logger.info("Successful update");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }


        out.flush();
        loanDao.close();
    }
}
