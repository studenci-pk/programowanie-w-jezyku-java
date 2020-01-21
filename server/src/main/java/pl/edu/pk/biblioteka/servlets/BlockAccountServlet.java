package pl.edu.pk.biblioteka.servlets;

import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.data.Account;
import pl.edu.pk.biblioteka.data.Permissions;
import pl.edu.pk.biblioteka.data.dao.AccountDao;
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
import java.util.Map;
import java.util.Optional;

/**
 * Serwlet do blokowania/odblokownaia kont
 */
public @WebServlet("/account/block")
class BlockAccountServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(BlockAccountServlet.class.getName());

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        Map<String, String> parametersInfo = WebUtils.getParametersInfo(request);
        logger.info(parametersInfo.toString());
        boolean success = false;

        if (AccessValidator.isLibrarian(session)) {
            String librarianId = parametersInfo.get("librarianid");
            String readerId = parametersInfo.get("readerid");

            if (librarianId != null) { // jeśli to pracownik
                LibrarianDao librarianDao = new LibrarianDao();
                Optional<LibrarianDto> librarian = librarianDao.get(Integer.valueOf(librarianId));
                if (librarian.isPresent()) {
                    LibrarianDto l = librarian.get(); // 'LibrarianDto' z 'Optional<LibrarianDto>'
                    Account account = l.getAccount();
                    // Jeśli konto jest aktywne to zablokuj, a jesli zablokowane to odblokuj
                    if (account.getPermissionId() == Permissions.LIBRARIAN_ACCESS) {
                        account.setPermissionId(Permissions.BLOCKED_ACCOUNT);
                    } else {
                        account.setPermissionId(Permissions.LIBRARIAN_ACCESS);
                    }
                    // Update w bazie
                    AccountDao accountDao = new AccountDao();
                    if (accountDao.update(account) > 0) {
                        success = true;
                    }
                    accountDao.close();
                }

                librarianDao.close();
            } else if (readerId != null) {
                ReaderDao readerDao = new ReaderDao();
                Optional<ReaderDto> reader = readerDao.get(Integer.valueOf(readerId));
                if (reader.isPresent()) {
                    ReaderDto l = reader.get();
                    Account account = l.getAccount();
                    if (account.getPermissionId() == Permissions.COMMON_READER) {
                        account.setPermissionId(Permissions.BLOCKED_ACCOUNT);
                    } else {
                        account.setPermissionId(Permissions.COMMON_READER);
                    }
                    AccountDao accountDao = new AccountDao();
                    if (accountDao.update(account) > 0) {
                        success = true;
                    }
                    accountDao.close();
                }

                readerDao.close();
            }
        }

        if (success) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        out.flush();
    }
}