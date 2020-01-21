package pl.edu.pk.biblioteka.data.dao;

import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.Dao;
import pl.edu.pk.biblioteka.data.Audiobook;
import pl.edu.pk.biblioteka.data.Copy;
import pl.edu.pk.biblioteka.data.Ebook;
import pl.edu.pk.biblioteka.dto.LoanDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AudiobookDao extends Dao<Audiobook> {
    private static final Logger logger = Logger.getLogger(AudiobookDao.class.getName());

    public Optional<Audiobook> get(int audiobookId) {
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT id_audiobooka, id_ksiazki FROM audiobook " +
                    "WHERE id_audiobooka = ? ;");
            ps.setInt(1, audiobookId);

            logger.info(ps.toString());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(new Audiobook(
                        rs.getInt("id_audiobooka"),
                        rs.getInt("id_ksiazki")
                ));
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return Optional.empty();
    }

    public int remove(Audiobook audiobook) {
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("DELETE FROM audiobook " +
                    "WHERE id_audiobooka = ? ;");
            ps.setInt(1, audiobook.getAudiobookId());

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
            ps = conn.prepareStatement("INSERT INTO audiobook " +
                    "(id_ksiazki) VALUES (?);", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, bookId);

            int rowCount = ps.executeUpdate();
            logger.info("rowCount: " + rowCount);

            if (rowCount == 1) {
                ResultSet resultSet = ps.getGeneratedKeys();
                if (resultSet.next()) {
                    int audiobookId = resultSet.getInt(1);
                    logger.info("audiobookId: " + audiobookId);
                    return audiobookId;
                }
            }

        } catch (SQLException e) {
            logger.error(e);
        }

        return -1;
    }

    public List<Audiobook> getAll(int bookId) {
        List<Audiobook> resultList =  new ArrayList<>();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT id_audiobooka, id_ksiazki " +
                    "FROM audiobook " +
                    "WHERE id_ksiazki = ?;");
            ps.setInt(1, bookId);

            logger.info(ps.toString());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resultList.add(new Audiobook(
                        rs.getInt("id_audiobooka"),
                        rs.getInt("id_ksiazki")
                ));
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return resultList;
    }
}
