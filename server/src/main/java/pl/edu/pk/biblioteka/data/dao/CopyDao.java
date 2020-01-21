package pl.edu.pk.biblioteka.data.dao;

import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.Dao;
import pl.edu.pk.biblioteka.data.Account;
import pl.edu.pk.biblioteka.data.Charge;
import pl.edu.pk.biblioteka.data.Copy;
import pl.edu.pk.biblioteka.data.ReservationStatus;
import pl.edu.pk.biblioteka.dto.LoanDto;
import pl.edu.pk.biblioteka.utils.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CopyDao extends Dao<Copy> {
    private static final Logger logger = Logger.getLogger(CopyDao.class.getName());

    public Optional<Copy> get(int copyId) {
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT id_egzemplarza, id_ksiazki, wycofany FROM egzemplarz " +
                    "WHERE id_egzemplarza = ? ;");
            ps.setInt(1, copyId);

            logger.info(ps.toString());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(new Copy(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getBoolean(3),
                        0 //ReservationStatus.FREE
                        ));
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return Optional.empty();
    }

    public List<Copy> getAll() {
        List<Copy> resultList =  new ArrayList<>();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT e.id_egzemplarza, e.id_ksiazki, e.wycofany " +
                    "IF(wz.id_statusRezerwacji is null, 1, wz.id_statusRezerwacji) " +
                    "FROM wypozyczenie_zwrot wz " +
                    "RIGHT JOIN egzemplarz e " +
                    "ON (wz.id_egzemplarza=e.id_egzemplarza);");

            logger.info(ps.toString());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resultList.add(new Copy(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getBoolean(3),
                        rs.getInt(4)
                ));
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return resultList;
    }

    public int remove(Copy copy) {
        return remove(copy.getCopyId());
    }

    public int remove(int copyId) {
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("DELETE FROM egzemplarz " +
                    "WHERE id_egzemplarza = ? ;");
            ps.setInt(1, copyId);

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

    public Optional<Copy> add(Integer bookId) {
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("INSERT INTO egzemplarz (id_ksiazki) " +
                    "VALUES (?) ;", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, bookId);

            logger.info(ps.toString());

            int rowCount = ps.executeUpdate();

            if (rowCount > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int copyId = rs.getInt(1);
                    logger.info("Added " + rowCount + " row");
                    logger.info("copyId: " + copyId);
                    return Optional.of(new Copy(copyId, bookId, false,1));
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return Optional.empty();
    }

    public List<Copy> getAll(int bookId) {
        List<Copy> resultList =  new ArrayList<>();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT DISTINCT e.id_egzemplarza, e.id_ksiazki, e.wycofany, " +
                    "IF(wz.id_statusRezerwacji is null, 1, wz.id_statusRezerwacji) " +
                    "FROM wypozyczenie_zwrot wz " +
                    "RIGHT JOIN egzemplarz e " +
                    "ON (wz.id_egzemplarza=e.id_egzemplarza AND wz.id_statusRezerwacji <> 1) " +
                    "WHERE e.id_ksiazki = ?;");
            ps.setInt(1, bookId);

            logger.info(ps.toString());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resultList.add(new Copy(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getBoolean(3),
                        rs.getInt(4)
                ));
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return resultList;
    }

    public int update(Copy c) {
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("UPDATE egzemplarz " +
                    "SET id_egzemplarza = ?, id_ksiazki = ?, wycofany = ? " +
                    "WHERE id_egzemplarza = ? ;");
            ps.setInt(1, c.getCopyId());
            ps.setInt(2, c.getBookId());
            ps.setBoolean(3, c.isWithdrawn());
            ps.setInt(4, c.getCopyId());

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
}