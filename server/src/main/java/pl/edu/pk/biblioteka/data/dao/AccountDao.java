package pl.edu.pk.biblioteka.data.dao;

import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.Dao;
import pl.edu.pk.biblioteka.data.Account;
import pl.edu.pk.biblioteka.utils.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountDao extends Dao<Account> {
    private static final Logger logger = Logger.getLogger(AccountDao.class.getName());

    public AccountDao() {
        super();
    }

    public AccountDao(Connection connection) {
        super(connection);
    }

    public int createAndGetId(String login, String password, Optional<String> email, Date createDate, int permissionId) {
        try {
            PreparedStatement ps;
            ps = conn.prepareStatement("INSERT INTO konto(" +
                    "login, haslo, email, dataUtworzenia, id_uprawnienia) " +
                    "VALUES (?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
            int iterator = 1;
            ps.setString(iterator++, login);
            ps.setString(iterator++, password);
            ps.setNString(iterator++, email.orElse(null));
            ps.setDate(iterator++, createDate);
            ps.setInt(iterator++, permissionId);

            int rowCount = ps.executeUpdate();

            if (rowCount == 1) {
                ResultSet resultSet = ps.getGeneratedKeys();
                if (resultSet.next()) {
                    int accountId = resultSet.getInt(1);
                    logger.info("accountId: "+accountId);
                    return accountId;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return -1;
    }

    public int add(Account account) {
        try {
            PreparedStatement ps;
            ps = conn.prepareStatement("INSERT INTO konto(" + //id_konta,
                    "login, haslo, email, dataUtworzenia, id_uprawnienia) " +
                    "VALUES (?, ?, ?, ?, ?);"); // ?,
            int iterator = 1;
//            ps.setInt(iterator++, account.getAccount());
//            ps.setInt(iterator++, Integer.valueOf(account.getPesel()));
            ps.setString(iterator++, account.getLogin());
            ps.setString(iterator++, account.getPassword());
            ps.setString(iterator++, account.getEmail());
            ps.setDate(iterator++, account.getCreateDate());
            ps.setInt(iterator++, account.getPermissionId());

            return ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        }

        return 0;
    }

    public Optional<Account> get(int accountId) {
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT id_konta, login, haslo, email, dataUtworzenia, id_uprawnienia " +
                    "FROM konto WHERE id_konta=?;");
            ps.setInt(1, accountId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return Optional.of(new Account(
                        rs.getInt("id_konta"),
                        rs.getString("login"),
                        rs.getString("haslo"),
                        rs.getNString("email"),
                        rs.getDate("dataUtworzenia"),
                        rs.getInt("id_uprawnienia")));
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return Optional.empty();
    }

    public List<Account> getAll() {
        List<Account> resultList =  new ArrayList<>();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT id_konta, login, haslo, email, dataUtworzenia, id_uprawnienia " +
                    "FROM konto;");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resultList.add(new Account(
                        rs.getInt("id_konta"),
                        rs.getString("login"),
                        rs.getString("haslo"),
                        rs.getNString("email"),
                        rs.getDate("dataUtworzenia"),
                        rs.getInt("id_uprawnienia")));
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return resultList;
    }

    public int update(Account account) {
        try {
            PreparedStatement ps;
            ps = conn.prepareStatement("UPDATE konto SET " +
                    "id_konta=?, login=?, haslo=?, email=?, dataUtworzenia=?, id_uprawnienia=? " +
                    "WHERE id_konta=?;");
            int iterator = 1;
            ps.setInt(iterator++, account.getAccountId());
            ps.setString(iterator++, account.getLogin());
            ps.setString(iterator++, account.getPassword());
            ps.setNString(iterator++, account.getEmail());
            ps.setDate(iterator++, account.getCreateDate());
            ps.setInt(iterator++, account.getPermissionId());
            ps.setInt(iterator++, account.getAccountId());

            return ps.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        }

        return 0;
    }

    public Optional<Account> get(String login, String password) {
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT id_konta, login, haslo, email, dataUtworzenia, id_uprawnienia " +
                    "FROM konto WHERE BINARY login=? AND haslo=?;");
            ps.setString(1, login);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(new Account(
                        rs.getInt("id_konta"),
                        rs.getString("login"),
                        rs.getString("haslo"),
                        rs.getNString("email"),
                        rs.getDate("dataUtworzenia"),
                        rs.getInt("id_uprawnienia")));
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return Optional.empty();
    }

    public Optional<Account> get(String login) {
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT id_konta, login, haslo, email, dataUtworzenia, id_uprawnienia " +
                    "FROM konto WHERE login=?;");
            ps.setString(1, login);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(new Account(
                        rs.getInt("id_konta"),
                        rs.getString("login"),
                        rs.getString("haslo"),
                        rs.getNString("email"),
                        rs.getDate("dataUtworzenia"),
                        rs.getInt("id_uprawnienia")));
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return Optional.empty();
    }
}
