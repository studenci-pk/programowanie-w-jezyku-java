package pl.edu.pk.biblioteka.data.dao;

import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.Dao;
import pl.edu.pk.biblioteka.data.Reader;
import pl.edu.pk.biblioteka.utils.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReaderDao extends Dao<Reader> {
    private static final Logger logger = Logger.getLogger(ReaderDao.class.getName());

    public int add(String pesel, String name, String surname, String faculty, String subject, int accountId) {
        try {
            PreparedStatement ps;
            ps = conn.prepareStatement("INSERT INTO uzytkownik_student_czytelnik(" +
                    "pesel, imie, nazwisko, wydzial, kierunek, id_konta) " +
                    "VALUES (?, ?, ?, ?, ?, ?);");
            int iterator = 1;
            ps.setString(iterator++, pesel);
            ps.setString(iterator++, name);
            ps.setString(iterator++, surname);
            ps.setString(iterator++, faculty);
            ps.setString(iterator++, subject);
            ps.setInt(iterator++, accountId);

            return ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        }

        return 0;
    }

    public int add(Reader reader) {
        try {
            PreparedStatement ps;
            ps = conn.prepareStatement("INSERT INTO uzytkownik_student_czytelnik(" +
                    "pesel, imie, nazwisko, wydzial, kierunek, id_konta) " +
                    "VALUES (?, ?, ?, ?, ?, ?);");
            int iterator = 1;
            ps.setString(iterator++, reader.getPesel());
            ps.setString(iterator++, reader.getName());
            ps.setString(iterator++, reader.getSurname());
            ps.setString(iterator++, reader.getFaculty());
            ps.setString(iterator++, reader.getSubject());
            ps.setInt(iterator++, reader.getAccountId());

            return ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        }

        return 0;
    }

    public Optional<Reader> get(int readerId) {
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT pesel, imie, nazwisko, wydzial, kierunek, id_konta " +
                    "FROM uzytkownik_student_czytelnik WHERE id_czytelnika=?;");
            ps.setInt(1, readerId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(new Reader(
                        readerId,
                        rs.getString("pesel"),
                        rs.getString("imie"),
                        rs.getString("nazwisko"),
                        rs.getString("wydzial"),
                        rs.getString("kierunek"),
                        rs.getInt("id_konta")));
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return Optional.empty();
    }

    public List<Reader> getAll() {
        List<Reader> resultList =  new ArrayList<>();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT id_czytelnika, pesel, imie, nazwisko, wydzial, kierunek, id_konta " +
                    "FROM uzytkownik_student_czytelnik;");

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                resultList.add(new Reader(
                        rs.getInt("id_czytelnika"),
                        rs.getString("pesel"),
                        rs.getString("imie"),
                        rs.getString("nazwisko"),
                        rs.getString("wydzial"),
                        rs.getString("kierunek"),
                        rs.getInt("id_konta")));
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return resultList;
    }

    public int remove(Reader reader) {
        try {
            PreparedStatement ps;
            ps = conn.prepareStatement("DELETE FROM uzytkownik_student_czytelnik " +
                    "WHERE id_czytelnika=?;");
            int iterator = 1;
            ps.setInt(iterator++, reader.getReaderId());

            return ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        }

        return 0;
    }

    public int update(Reader reader) {
        try {
            PreparedStatement ps;
            ps = conn.prepareStatement("UPDATE uzytkownik_student_czytelnik SET pesel=?, imie=?, nazwisko=?, wydzial=?, kierunek=?, id_konta=?" +
                    "WHERE id_czytelnika=?;");
            int iterator = 1;
            ps.setString(iterator++, reader.getPesel());
            ps.setString(iterator++, reader.getName());
            ps.setString(iterator++, reader.getSurname());
            ps.setString(iterator++, reader.getFaculty());
            ps.setString(iterator++, reader.getSubject());
            ps.setInt(iterator++, reader.getAccountId());
            ps.setInt(iterator++, reader.getReaderId());

            return ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        }
        return 0;
    }
}
