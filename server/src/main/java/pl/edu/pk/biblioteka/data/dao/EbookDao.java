package pl.edu.pk.biblioteka.data.dao;

import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.Dao;
import pl.edu.pk.biblioteka.data.Audiobook;
import pl.edu.pk.biblioteka.data.Copy;
import pl.edu.pk.biblioteka.data.Ebook;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EbookDao extends Dao<Ebook> {
    private static final Logger logger = Logger.getLogger(EbookDao.class.getName());

    public Optional<Ebook> get(int copyId) {
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT id_ibuka, id_ksiazki FROM ibuk " +
                    "WHERE id_ibuka = ? ;");
            ps.setInt(1, copyId);

            logger.info(ps.toString());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(new Ebook(
                        rs.getInt("id_ibuka"),
                        rs.getInt("id_ksiazki")
                ));
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return Optional.empty();
    }

    public int remove(Ebook ebook) {
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("DELETE FROM ibuk " +
                    "WHERE id_ibuka = ? ;");
            ps.setInt(1, ebook.getEbookId());

            logger.info(ps.toString());

            int rowCount = ps.executeUpdate();

            if (rowCount > 0) {
                logger.info("Deleted " + rowCount + " row");
                return rowCount;
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return -1;
    }

    public int add(int bookId) {
        PreparedStatement ps;

        try {
            ps = conn.prepareStatement("INSERT INTO ibuk " +
                    "(id_ksiazki) VALUES (?);", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, bookId);

            int rowCount = ps.executeUpdate();
            logger.info("rowCount: " + rowCount);

            if (rowCount == 1) {
                ResultSet resultSet = ps.getGeneratedKeys();
                if (resultSet.next()) {
                    int ebookId = resultSet.getInt(1);
                    logger.info("ebookId: " + ebookId);
                    return ebookId;
                }
            }

        } catch (SQLException e) {
            logger.error(e);
        }

        return -1;
    }

    public List<Ebook> getAll(int bookId) {
        List<Ebook> resultList =  new ArrayList<>();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT id_ibuka, id_ksiazki " +
                    "FROM ibuk " +
                    "WHERE id_ksiazki = ?;");
            ps.setInt(1, bookId);

            logger.info(ps.toString());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resultList.add(new Ebook(
                        rs.getInt("id_ibuka"),
                        rs.getInt("id_ksiazki")
                ));
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return resultList;
    }
}
