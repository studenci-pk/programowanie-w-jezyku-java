package pl.edu.pk.biblioteka.servlets;

import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.data.dao.DepartmentDao;
import pl.edu.pk.biblioteka.data.dao.PublisherDao;
import pl.edu.pk.biblioteka.utils.AccessValidator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public@WebServlet("/add-department")
class AddDepartment extends HttpServlet {
    private static final Logger logger = Logger.getLogger(AddDepartment.class.getName());

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String name = request.getHeader("name");
        logger.info("name: " + name);

        HttpSession session = request.getSession(false);

        if (AccessValidator.isLibrarian(session)) {
            DepartmentDao departmentDao = new DepartmentDao();
            int i = departmentDao.add(name);

            if (i > 0) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            departmentDao.close();
        }

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Niepoprawne dane logowania");
        out.close();
    }
}
