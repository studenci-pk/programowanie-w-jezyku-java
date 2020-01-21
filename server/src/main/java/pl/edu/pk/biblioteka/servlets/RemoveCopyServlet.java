package pl.edu.pk.biblioteka.servlets;

import javafx.scene.layout.Pane;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import pl.edu.pk.biblioteka.data.Audiobook;
import pl.edu.pk.biblioteka.data.Copy;
import pl.edu.pk.biblioteka.data.Ebook;
import pl.edu.pk.biblioteka.data.dao.AudiobookDao;
import pl.edu.pk.biblioteka.data.dao.CopyDao;
import pl.edu.pk.biblioteka.data.dao.EbookDao;
import pl.edu.pk.biblioteka.utils.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Serwlet do usuwania zasobów
 */
public @WebServlet("/resources/remove")
class RemoveCopyServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(RemoveCopyServlet.class.getName());

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json"); // Odpowiedz w json
        PrintWriter out = response.getWriter(); // Strumien do odpowiedzi

        Map<String, String> headersInfo = WebUtils.getHeadersInfo(request); // Utworzenie Map'y z nagłówka
        logger.info(headersInfo);
        JSONObject jsonObject = new JSONObject(); // Utworzenie obiektu JSON
        boolean success = false;

        if(headersInfo.containsKey("copyid")) { // jeśli przesłano id egzemplarza
            int copyId = Integer.valueOf(headersInfo.get("copyid")); // konsersja na int
            CopyDao copyDao = new CopyDao();
            Optional<Copy> copy = copyDao.get(copyId); // znaleznie egzemplarza o takim id
            if (copy.isPresent()) {
                Copy c = copy.get(); // Wyciągnięcie 'Copy' z 'Optional'
                c.setWithdrawn(true);
                if (copyDao.update(c) > 0) {
                    // Wyślij informacje w json o wycofanym egzmeplarzu
                    jsonObject.put("copy", new JSONObject(c));
                    out.print(jsonObject.toString());
                    success = true;
                }
//                if (copyDao.remove(c) > 0) { // Jeśli udało się usunąć
//                    // Wyślij informacje w json o usuniętym egzmeplarzu
//                    jsonObject.put("copy", new JSONObject(c));
//                    out.print(jsonObject.toString());
//                    success = true;
//                }
            }
            copyDao.close();

        } else if(headersInfo.containsKey("ebookid")) { // jeśli przesłano id ebooka
            int ebookId = Integer.valueOf(headersInfo.get("ebookid")); // konwersja na int
            EbookDao ebookDao = new EbookDao();
            Optional<Ebook> ebook = ebookDao.get(ebookId); // Wyszukanie takeigo ebooka
            if (ebook.isPresent()) {
                Ebook e = ebook.get(); // Wyciągnięcie 'Ebook' z 'Optional'
                if (ebookDao.remove(e) > 0) { // jeslu udało się usunąć
                    // Wyślij informacje w json o usuniętym ebooku
                    jsonObject.put("ebook", new JSONObject(e));
                    out.print(jsonObject.toString());
                    success = true;
                }
            }
            ebookDao.close();

        } else if(headersInfo.containsKey("audiobookid")) { // Analogicznie ^
            int audiobookId = Integer.valueOf(headersInfo.get("audiobookid"));
            AudiobookDao audiobookDao = new AudiobookDao();
            Optional<Audiobook> audiobook = audiobookDao.get(audiobookId);
            if (audiobook.isPresent()) {
                Audiobook a = audiobook.get();
                if (audiobookDao.remove(a) > 0) {
                    jsonObject.put("audiobook", new JSONObject(a));
                    out.print(jsonObject.toString());
                    success = true;
                }
            }
            audiobookDao.close();
        }

        if (success) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        out.flush();
        out.close();
    }
}

