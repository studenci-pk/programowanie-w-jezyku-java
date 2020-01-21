package pl.edu.pk.biblioteka.servlets;

import org.json.JSONObject;
import pl.edu.pk.biblioteka.data.dao.AuthorDao;
import pl.edu.pk.biblioteka.data.dao.DepartmentDao;
import pl.edu.pk.biblioteka.data.dao.PublisherDao;
import pl.edu.pk.biblioteka.data.*;
import pl.edu.pk.biblioteka.utils.AccessValidator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Serwlet odsyłający informacje o autorach, wydawcach, działach
 */
public @WebServlet("/get/info")
class GetInfoServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        JSONObject jsonResponse = new JSONObject();

        //Prepare Authors
        AuthorDao authorDao = new AuthorDao();
        List<Author> authors = authorDao.getAll();
        jsonResponse.put("authors", authors);


        // Prepare Publishers
        PublisherDao publisherDao = new PublisherDao();
        List<Publisher> publishers = publisherDao.getAll();
        jsonResponse.put("publishers", publishers);


        // Prepare Departments
        DepartmentDao departmentDao = new DepartmentDao();
        List<Department> departments = departmentDao.getAll();
        jsonResponse.put("departments", departments);


        // Send
        PrintWriter out = response.getWriter();
        response.setStatus(HttpServletResponse.SC_OK);
        System.out.println(jsonResponse.toString());
        out.print(jsonResponse.toString());
        out.flush();
        authorDao.close();
        publisherDao.close();
        departmentDao.close();
    }
}
