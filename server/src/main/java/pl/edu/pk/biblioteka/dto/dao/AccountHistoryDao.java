package pl.edu.pk.biblioteka.dto.dao;

import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.Dao;
import pl.edu.pk.biblioteka.data.Account;
import pl.edu.pk.biblioteka.data.Charge;
import pl.edu.pk.biblioteka.data.Copy;
import pl.edu.pk.biblioteka.data.ReservationStatus;
import pl.edu.pk.biblioteka.data.dao.AccountDao;
import pl.edu.pk.biblioteka.dto.AccountHistoryDto;
import pl.edu.pk.biblioteka.dto.LoanDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountHistoryDao extends Dao<AccountHistoryDto> {
    private static final Logger logger = Logger.getLogger(AccountHistoryDao.class);

    public AccountHistoryDao() {
        super();
    }

    public AccountHistoryDao(Connection connection) {
        super(connection);
    }

    public int add(int accountId, int loanId) {
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("INSERT INTO historiawypozyczenzwrotow " +
                    "(id_konta, id_wypozyczenia_zwrotu) " +
                    "VALUES (?, ?) ;");
            ps.setInt(1, accountId);
            ps.setInt(2, loanId);

            int rowCount = ps.executeUpdate();
            logger.info("rowCount: " + rowCount);
            return rowCount;

        } catch (SQLException e) {
            logger.error(e);
        }

        return -1;
    }

    public int add(Account account, LoanDto loan) {
        return add(account.getAccountId(), loan.getLoanId());
    }

    public List<AccountHistoryDto> getByAccountId(Integer accountId) {
        List<AccountHistoryDto> loans = new ArrayList<>();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT " +
                    "id_konta, " +
                    "id_wypozyczenia_zwrotu " +
                    "FROM historiawypozyczenzwrotow " +
                    "WHERE id_konta=? ;");
            ps.setInt(1, accountId);

            logger.info(ps.toString());

            AccountDao accountDao = new AccountDao();
            LoanDao loanDao = new LoanDao();

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Optional<Account> account = accountDao.get(rs.getInt("id_konta"));
                Optional<LoanDto> loan = loanDao.get(rs.getInt("id_wypozyczenia_zwrotu"));

                if (account.isPresent() && loan.isPresent())
                loans.add(new AccountHistoryDto(account.get(), loan.get()));
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return loans;
    }
}
