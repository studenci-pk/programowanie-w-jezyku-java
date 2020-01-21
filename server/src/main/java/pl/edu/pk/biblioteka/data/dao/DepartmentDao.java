package pl.edu.pk.biblioteka.data.dao;

import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.Dao;
import pl.edu.pk.biblioteka.data.Department;
import pl.edu.pk.biblioteka.utils.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DepartmentDao extends Dao<Department> {
    public static final Logger logger = Logger.getLogger(DepartmentDao.class.getName());

    public int add(String name) {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO dzial (nazwa) VALUES (?);",
                    Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, name);

            int rowCount = ps.executeUpdate();

            if(rowCount == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int departmentId = rs.getInt(1);
                    logger.info("departmentId: " + departmentId);
                    return departmentId;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return -1;
    }

    public List<Department> getAll() {
        List<Department> departments = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM dzial;");
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                departments.add(new Department(
                        resultSet.getInt("id_dzialu"),
                        resultSet.getString("nazwa")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return departments;
    }
}
