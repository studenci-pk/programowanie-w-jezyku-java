package pl.edu.pk.biblioteka.data.dao;

import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.Dao;
import pl.edu.pk.biblioteka.data.Publisher;
import pl.edu.pk.biblioteka.utils.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PublisherDao extends Dao<Publisher> {
    private static final Logger logger = Logger.getLogger(PublisherDao.class.getName());

    public List<Publisher> getAll() {
        List<Publisher> publishers = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM wydawnictwo;");
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                publishers.add(new Publisher(
                        resultSet.getInt("id_wydawnictwa"),
                        resultSet.getString("nazwa"),
                        resultSet.getString("miejscowosc"),
                        resultSet.getString("kraj")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return publishers;
    }

    public int add(String name, String address, String country) {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO wydawnictwo (nazwa, miejscowosc, kraj) " +
                            "VALUES (?, ? , ?);",
                    Statement.RETURN_GENERATED_KEYS);

            int iterator = 1;
            ps.setString(iterator++, name);
            ps.setString(iterator++, address);
            ps.setString(iterator++, country);

            int rowCount = ps.executeUpdate();

            if (rowCount == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int publisherId = rs.getInt(1);
                    logger.info("publisherId: " + publisherId);
                    return publisherId;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return -1;
    }
}
