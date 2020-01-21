package pl.edu.pk.biblioteka.servlets;

import org.json.JSONObject;
import pl.edu.pk.biblioteka.dto.dao.CartDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public @WebServlet("/get/cart")
class GetCartServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        //co oznacza false? ze nie tworzy sesji jak jej nie ma tylko zwrzcz null
        HttpSession session = request.getSession(false);

        if(session != null)
        {
            Integer userId = (Integer) session.getAttribute("userId");
            CartDao cartDao = new CartDao();
            List<String> titles = cartDao.getByAccountId(userId);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("titles", titles);
            out.print(jsonObject.toString());
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);
        }

        out.close();


    }

//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doPost(req, resp);
//    }

}
