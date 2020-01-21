package pl.edu.pk.biblioteka.dto.dao;

import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.Dao;
import pl.edu.pk.biblioteka.data.Account;
import pl.edu.pk.biblioteka.data.dao.AccountDao;
import pl.edu.pk.biblioteka.dto.LibrarianDto;
import pl.edu.pk.biblioteka.dto.ReaderDto;
import pl.edu.pk.biblioteka.utils.DatabaseConnector;
import pl.edu.pk.biblioteka.utils.MapUtils;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class ReaderDao extends Dao<ReaderDto> {
    private static final Logger logger = Logger.getLogger(ReaderDao.class.getName());

    public int add(String pesel, String name, String surname, String faculty, String subject, Account account) {
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
            ps.setInt(iterator++, account.getAccountId());

            return ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        }

        return 0;
    }

    public int add(ReaderDto reader) {
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
            ps.setInt(iterator++, reader.getAccount().getAccountId());

            return ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        }

        return 0;
    }

    public Optional<ReaderDto> get(int readerId) {
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT pesel, imie, nazwisko, wydzial, kierunek, id_konta, login, haslo, email, dataUtworzenia, id_uprawnienia " +
                    "FROM uzytkownik_student_czytelnik NATURAL JOIN konto WHERE id_czytelnika=?;");
            ps.setInt(1, readerId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(new ReaderDto(
                        readerId,
                        rs.getString("pesel"),
                        rs.getString("imie"),
                        rs.getString("nazwisko"),
                        rs.getString("wydzial"),
                        rs.getString("kierunek"),
                        new Account(
                                rs.getInt("id_konta"),
                                rs.getString("login"),
                                rs.getString("haslo"),
                                rs.getString("email"),
                                rs.getDate("dataUtworzenia"),
                                rs.getInt("id_uprawnienia")
                        )));
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return Optional.empty();
    }

    public Optional<ReaderDto> getByAccountId(int accountId) {
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT id_czytelnika, pesel, imie, nazwisko, wydzial, kierunek, id_konta, login, haslo, email, dataUtworzenia, id_uprawnienia " +
                    "FROM uzytkownik_student_czytelnik NATURAL JOIN konto WHERE id_konta=?;");
            ps.setInt(1, accountId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(new ReaderDto(
                        rs.getInt("id_czytelnika"),
                        rs.getString("pesel"),
                        rs.getString("imie"),
                        rs.getString("nazwisko"),
                        rs.getString("wydzial"),
                        rs.getString("kierunek"),
                        new Account(
                                rs.getInt("id_konta"),
                                rs.getString("login"),
                                rs.getString("haslo"),
                                rs.getString("email"),
                                rs.getDate("dataUtworzenia"),
                                rs.getInt("id_uprawnienia")
                        )));
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return Optional.empty();
    }

    public List<ReaderDto> getAll() {
        List<ReaderDto> resultList =  new ArrayList<>();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT id_czytelnika, pesel, imie, nazwisko, wydzial, kierunek, id_konta, login, haslo, email, dataUtworzenia, id_uprawnienia " +
                    "FROM uzytkownik_student_czytelnik NATURAL JOIN konto;");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resultList.add(new ReaderDto(
                        rs.getInt("id_czytelnika"),
                        rs.getString("pesel"),
                        rs.getString("imie"),
                        rs.getString("nazwisko"),
                        rs.getString("wydzial"),
                        rs.getString("kierunek"),
                        new Account(
                                rs.getInt("id_konta"),
                                rs.getString("login"),
                                rs.getString("haslo"),
                                rs.getString("email"),
                                rs.getDate("dataUtworzenia"),
                                rs.getInt("id_uprawnienia")
                        )));
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return resultList;
    }

    public int remove(ReaderDto reader) {
        try {
            PreparedStatement ps;
            ps = conn.prepareStatement("DELETE FROM uzytkownik_student_czytelnik " +
                    "WHERE id_czytelnika=?;");
            ps.setInt(1, reader.getReaderId());

            return ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        }

        return 0;
    }

    public int update(ReaderDto reader) {
        try {
            conn.setAutoCommit(false);
            AccountDao accountDao = new AccountDao(conn);

            int accountId = accountDao.update(reader.getAccount());

            if (accountId != 1) {
                throw new SQLException("");
            }

            PreparedStatement ps;
            ps = conn.prepareStatement("UPDATE uzytkownik_student_czytelnik SET pesel=?, imie=?, nazwisko=?, wydzial=?, kierunek=?, id_konta=? " +
                    "WHERE id_czytelnika=?;");
            int iterator = 1;
            ps.setString(iterator++, reader.getPesel());
            ps.setString(iterator++, reader.getName());
            ps.setString(iterator++, reader.getSurname());
            ps.setString(iterator++, reader.getFaculty());
            ps.setString(iterator++, reader.getSubject());
            ps.setInt(iterator++, reader.getAccount().getAccountId());
            ps.setInt(iterator++, reader.getReaderId());

            logger.info(ps.toString());
            int rowCount = ps.executeUpdate();

            if (rowCount == 1) {
                conn.commit();
                conn.setAutoCommit(true);
                return rowCount;
            }

            conn.rollback();
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            logger.error(e);
            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (SQLException ignored) { }
        }

        return 0;
    }


    public int add(String pesel, String name, String surname, String faculty, String subject,
                   String login, String password, Optional<String> email, Date createDate, int permissionId) throws SQLException {
        try {
            conn.setAutoCommit(false);
            AccountDao accountDao = new AccountDao(conn);

            int accountId = accountDao.createAndGetId(login, password, email, createDate, permissionId);

            if (accountId < 0) {
                throw new SQLException("");
            }

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

            int rowCount = ps.executeUpdate();
            if (rowCount > 0) {
                conn.commit();
                return rowCount;
            }

            conn.setAutoCommit(true);
        } catch (SQLException e) {
            logger.error(e);
            conn.rollback();
        }
        conn.rollback();


        return 0;
    }

    public List<ReaderDto> getByMap(Map<String, String> map) {
        if (map.isEmpty()) {
            return getAll();
        }

        List<ReaderDto> resultList =  new ArrayList<>();
        PreparedStatement ps;
        try {
//            MapUtils.renameKey(map, "name", "imie");
//            MapUtils.renameKey(map, "surname", "nazwisko");

            ps = conn.prepareStatement("SELECT id_czytelnika, pesel, imie, nazwisko, wydzial, " +
                    "kierunek, id_konta, login, haslo, email, dataUtworzenia, id_uprawnienia " +
                    "FROM uzytkownik_student_czytelnik NATURAL JOIN konto " +
                    "WHERE imie LIKE ? AND nazwisko LIKE ? AND login LIKE ?;");

            ps.setNString(1, '%' + map.getOrDefault("name", "") + '%');
            ps.setNString(2, '%' + map.getOrDefault("surname", "") + '%');
            ps.setNString(3, '%' + map.getOrDefault("login", "") + '%');

            logger.info(ps.toString());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resultList.add(new ReaderDto(
                        rs.getInt("id_czytelnika"),
                        rs.getString("pesel"),
                        rs.getString("imie"),
                        rs.getString("nazwisko"),
                        rs.getString("wydzial"),
                        rs.getString("kierunek"),
                        new Account(
                                rs.getInt("id_konta"),
                                rs.getString("login"),
                                rs.getString("haslo"),
                                rs.getString("email"),
                                rs.getDate("dataUtworzenia"),
                                rs.getInt("id_uprawnienia")
                        )));
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return resultList;
    }
}
