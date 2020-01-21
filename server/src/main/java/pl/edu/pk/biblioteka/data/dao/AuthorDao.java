package pl.edu.pk.biblioteka.data.dao;

import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.Dao;
import pl.edu.pk.biblioteka.data.Author;
import pl.edu.pk.biblioteka.utils.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorDao extends Dao<Author> {
    private static final Logger logger = Logger.getLogger(AuthorDao.class.getName());

    public int add(String name, String surname, String country) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO autor (imie, nazwisko, krajPochodzenia)" +
                    "VALUES (?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
            int iterator = 1;
            ps.setString(iterator++, name);
            ps.setString(iterator++, surname);
            ps.setString(iterator++, country);

            int rowCount = ps.executeUpdate();

            if (rowCount == 1) {
                ResultSet resultSet = ps.getGeneratedKeys();
                if (resultSet.next()) {
                    int authorId = resultSet.getInt(1);
                    logger.info("authorId: " + authorId);
                    return authorId;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return -1;
    }

    public List<Author> getAll() {
        List<Author> authors = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM autor;");
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                authors.add(new Author(
                        resultSet.getInt("id_autora"),
                        resultSet.getString("imie"),
                        resultSet.getString("nazwisko"),
                        resultSet.getString("krajPochodzenia")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return authors;
    }
}