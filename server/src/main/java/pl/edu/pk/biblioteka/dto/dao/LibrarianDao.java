package pl.edu.pk.biblioteka.dto.dao;

import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.Dao;
import pl.edu.pk.biblioteka.data.Account;
import pl.edu.pk.biblioteka.data.dao.AccountDao;
import pl.edu.pk.biblioteka.dto.LibrarianDto;
import pl.edu.pk.biblioteka.utils.DatabaseConnector;
import pl.edu.pk.biblioteka.utils.MapUtils;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class LibrarianDao extends Dao<LibrarianDto> {
    private static final Logger logger = Logger.getLogger(LibrarianDao.class.getName());

    public int add(String pesel, String name, String surname, Optional<String> nickname, Account account) {
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
            ps.setInt(iterator++, account.getAccountId());

            return ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        }

        return 0;
    }

    public int add(LibrarianDto librarian) {
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
            ps.setInt(iterator++, librarian.getAccount().getAccountId());

            return ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        }

        return 0;
    }

    public Optional<LibrarianDto> get(int librarianId) {
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT pesel, imie, nazwisko, nickname, id_konta, login, haslo, email, dataUtworzenia, id_uprawnienia " +
                    "FROM pracownik_bibliotekarz NATURAL JOIN konto WHERE id_pracownika=?;");
            ps.setInt(1, librarianId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(new LibrarianDto(
                        librarianId,
                        rs.getString("pesel"),
                        rs.getString("imie"),
                        rs.getString("nazwisko"),
                        Optional.ofNullable(rs.getNString("nickname")),
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

    public Optional<LibrarianDto> getByAccountId(int accountId) {
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT id_pracownika, pesel, imie, nazwisko, nickname, id_konta, login, haslo, email, dataUtworzenia, id_uprawnienia " +
                    "FROM pracownik_bibliotekarz NATURAL JOIN konto WHERE id_konta=?;");
            ps.setInt(1, accountId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(new LibrarianDto(
                        rs.getInt("id_pracownika"),
                        rs.getString("pesel"),
                        rs.getString("imie"),
                        rs.getString("nazwisko"),
                        Optional.ofNullable(rs.getNString("nickname")),
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

    public List<LibrarianDto> getAll() {
        List<LibrarianDto> resultList =  new ArrayList<>();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT id_pracownika, pesel, imie, nazwisko, nickname, id_konta, login, haslo, email, dataUtworzenia, id_uprawnienia " +
                    "FROM pracownik_bibliotekarz NATURAL JOIN konto;");

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                resultList.add(new LibrarianDto(
                        rs.getInt("id_pracownika"),
                        rs.getString("pesel"),
                        rs.getString("imie"),
                        rs.getString("nazwisko"),
                        Optional.ofNullable(rs.getNString("nickname")),
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

    public int remove(LibrarianDto librarian) {
        try {
            PreparedStatement ps;
            ps = conn.prepareStatement("DELETE FROM pracownik_bibliotekarz " +
                    "WHERE id_pracownika=?;");
            ps.setInt(1, librarian.getLibrarianId());

            return ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        }

        return 0;
    }

    public int update(LibrarianDto librarian) {
        try {
            conn.setAutoCommit(false);
            AccountDao accountDao = new AccountDao(conn);

            int accountId = accountDao.update(librarian.getAccount());

            if (accountId != 1) {
                throw new SQLException("");
            }

            PreparedStatement ps;
            ps = conn.prepareStatement("UPDATE pracownik_bibliotekarz SET pesel=?, imie=?, nazwisko=?, nickname=?, id_konta=? " +
                    "WHERE id_pracownika=?;");
            int iterator = 1;
            ps.setString(iterator++, librarian.getPesel());
            ps.setString(iterator++, librarian.getName());
            ps.setString(iterator++, librarian.getSurname());
            ps.setNString(iterator++, librarian.getNickname().orElse(null));
            ps.setInt(iterator++, librarian.getAccount().getAccountId());
            ps.setInt(iterator++, librarian.getLibrarianId());

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

    public int add(String pesel, String name, String surname,
                   String login, String password, Optional<String> email, Date createDate, int permissionId) throws SQLException {
        try {
            conn.setAutoCommit(false);
            AccountDao accountDao = new AccountDao(conn);

            int accountId = accountDao.createAndGetId(login, password, email, createDate, permissionId);

            if (accountId < 0) {
                throw new SQLException("");
            }

            PreparedStatement ps;
            ps = conn.prepareStatement("INSERT INTO pracownik_bibliotekarz(" +
                    "pesel, imie, nazwisko, nickname, id_konta) " +
                    "VALUES (?, ?, ?, 'nickname', ?);");
            int iterator = 1;
            ps.setString(iterator++, pesel);
            ps.setString(iterator++, name);
            ps.setString(iterator++, surname);
            ps.setInt(iterator++, accountId);

            int rowCount = ps.executeUpdate();
            if (rowCount == 1) {
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


    public List<LibrarianDto> getByMap(Map<String, String> map) {
        if (map.isEmpty()) {
            return getAll();
        }

        List<LibrarianDto> resultList =  new ArrayList<>();
        PreparedStatement ps;
        try {
//            MapUtils.renameKey(map, "name", "imie");
//            MapUtils.renameKey(map, "surname", "nazwisko");

            ps = conn.prepareStatement("SELECT id_pracownika, pesel, imie, nazwisko, nickname, " +
                    "id_konta, login, haslo, email, dataUtworzenia, id_uprawnienia " +
                    "FROM pracownik_bibliotekarz NATURAL JOIN konto " +
                    "WHERE imie LIKE ? AND nazwisko LIKE ? AND login LIKE ?;");

            ps.setNString(1, '%' + map.getOrDefault("name", "") + '%');
            ps.setNString(2, '%' + map.getOrDefault("surname", "") + '%');
            ps.setNString(3, '%' + map.getOrDefault("login", "") + '%');

            logger.info(ps.toString());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resultList.add(new LibrarianDto(
                        rs.getInt("id_pracownika"),
                        rs.getString("pesel"),
                        rs.getString("imie"),
                        rs.getString("nazwisko"),
                        Optional.ofNullable(rs.getNString("nickname")),
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