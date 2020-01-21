package pl.edu.pk.biblioteka.data.dao;

import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.Dao;
import pl.edu.pk.biblioteka.data.Librarian;
import pl.edu.pk.biblioteka.utils.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LibrarianDao extends Dao<Librarian> {
    private static final Logger logger = Logger.getLogger(LibrarianDao.class.getName());

    public int add(String pesel, String name, String surname, Optional<String> nickname, int accountId) {
        try {
            PreparedStatement ps;
            ps = conn.prepareStatement("INSERT INTO pracownik_bibliotekarz(" +
                    "pesel, imie, nazwisko, nickname, id_konta) " +
                    "VALUES (?, ?, ?, ?, ?);"); // ?,
            int iterator = 1;
            ps.setString(iterator++, pesel);
            ps.setString(iterator++, name);
            ps.setString(iterator++, surname);
            ps.setNString(iterator++, nickname.orElse(null));
            ps.setInt(iterator++, accountId);

            return ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        }

        return 0;
    }

    public int add(Librarian librarian) {
        try {
            PreparedStatement ps;
            ps = conn.prepareStatement("INSERT INTO pracownik_bibliotekarz(" +
                    "pesel, imie, nazwisko, nickname, id_konta) " +
                    "VALUES (?, ?, ?, ?, ?);"); // ?,
            int iterator = 1;
            ps.setString(iterator++, librarian.getPesel());
            ps.setString(iterator++, librarian.getName());
            ps.setString(iterator++, librarian.getSurname());
            ps.setNString(iterator++, librarian.getNickname().orElse(null));
            ps.setInt(iterator++, librarian.getAccountId());

            return ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        }

        return 0;
    }

    public Optional<Librarian> get(int librarianId) {
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT pesel, imie, nazwisko, nickname, id_konta " +
                    "FROM pracownik_bibliotekarz WHERE id_pracownika=?;");
            ps.setInt(1, librarianId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(new Librarian(
                        librarianId,
                        rs.getString("pesel"),
                        rs.getString("imie"),
                        rs.getString("nazwisko"),
                        Optional.ofNullable(rs.getNString("nickname")),
                        rs.getInt("id_konta")));
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return Optional.empty();
    }

    public List<Librarian> getAll() {
        List<Librarian> resultList =  new ArrayList<>();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT id_pracownika, pesel, imie, nazwisko, nickname, id_konta " +
                    "FROM pracownik_bibliotekarz;");

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                resultList.add(new Librarian(
                        rs.getInt("id_pracownika"),
                        rs.getString("pesel"),
                        rs.getString("imie"),
                        rs.getString("nazwisko"),
                        Optional.ofNullable(rs.getNString("nickname")),
                        rs.getInt("id_konta")));
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return resultList;
    }

    public int remove(Librarian librarian) {
        try {
            PreparedStatement ps;
            ps = conn.prepareStatement("DELETE FROM pracownik_bibliotekarz " +
                    "WHERE id_pracownika=?;");
            int iterator = 1;
            ps.setInt(iterator++, librarian.getLibrarianId());

            return ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        }

        return 0;
    }

    public int update(Librarian librarian) {
        try {
            PreparedStatement ps;
            ps = conn.prepareStatement("UPDATE pracownik_bibliotekarz SET pesel=?, imie=?, nazwisko=?, nickname=?, id_konta=?" +
                    "WHERE id_pracownika=?;");
            int iterator = 1;
            ps.setString(iterator++, librarian.getPesel());
            ps.setString(iterator++, librarian.getName());
            ps.setString(iterator++, librarian.getSurname());
            ps.setNString(iterator++, librarian.getNickname().orElse(null));
            ps.setInt(iterator++, librarian.getAccountId());
            ps.setInt(iterator++, librarian.getLibrarianId());

            return ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        }
        return 0;
    }
}
