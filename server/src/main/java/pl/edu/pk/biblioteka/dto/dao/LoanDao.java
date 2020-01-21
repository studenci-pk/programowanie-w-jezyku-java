package pl.edu.pk.biblioteka.dto.dao;

import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.Dao;
import pl.edu.pk.biblioteka.data.Account;
import pl.edu.pk.biblioteka.data.Charge;
import pl.edu.pk.biblioteka.data.Copy;
import pl.edu.pk.biblioteka.data.ReservationStatus;
import pl.edu.pk.biblioteka.dto.LoanDto;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public class LoanDao extends Dao<LoanDto> {
    private static final Logger logger = Logger.getLogger(LoanDao.class.getName());

    public LoanDao() {
        super();
    }

    public LoanDao(Connection connection) {
        super(connection);
    }

    public int add(Copy copy, Account account, ReservationStatus reservationStatus, Charge charge,
                   Date reservationDate, Date expireDate, String pickupPoint, Date startDate, Date endDate) {
        return add(copy.getCopyId(), account.getAccountId(), reservationStatus.getReservationStatusId(), charge.getChargeId(),
                reservationDate, expireDate, pickupPoint, startDate, endDate);
    }

    public int add(int copyId, int accountId, int reservationStatusId, int chargeId,
                   Date reservationDate, Date expireDate, String pickupPoint, Date startDate, Date endDate) {
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("INSERT INTO wypozyczenie_zwrot (" +
                            "id_egzemplarza, " +
                            "id_konta, " +
                            "id_StatusRezerwacji, " +
                            "id_oplaty, " +
                            "dataRezerwacji, " +
                            "dataWygasnieciaRezerwacji, " +
                            "miejsceOdbioru, " +
                            "dataWypozyczenia, " +
                            "dataZwrotu, " +
                            ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            int iterator = 1;
            ps.setInt(iterator++, copyId);
            ps.setInt(iterator++, accountId);
            ps.setInt(iterator++, reservationStatusId);
            ps.setInt(iterator++, chargeId);
            ps.setDate(iterator++, reservationDate);
            ps.setDate(iterator++, expireDate);
            ps.setString(iterator++, pickupPoint);
            ps.setDate(iterator++, startDate);
            ps.setDate(iterator++, endDate);

            logger.info(ps.toString());

            int rowCount = ps.executeUpdate();
            if (rowCount > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    int loadId = keys.getInt(1);
                    logger.info("loadId: " + loadId);
                    return loadId;
                }
            }

        } catch (SQLException e) {
            logger.error(e);
        }

        return -1;
    }

    public Optional<LoanDto> get(int loanId) {
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT " +
                    "wz.id_wypozyczenia_zwrotu, " +
                    "wz.id_egzemplarza, " +
                    "wz.id_konta, " +
                    "wz.id_StatusRezerwacji, " +
                    "wz.id_oplaty, " +
                    "wz.dataRezerwacji, " +
                    "wz.dataWygasnieciaRezerwacji, " +
                    "wz.miejsceOdbioru, " +
                    "wz.dataWypozyczenia, " +
                    "wz.dataZwrotu, " +
                    "e.id_ksiazki, " +
                    "e.wycofany, " +
                    "k.login," +
                    "k.haslo," +
                    "k.email," +
                    "k.dataUtworzenia," +
                    "k.id_uprawnienia," +
                    "k.dataUtworzenia," +
                    "sr.statusRezerwacji ," +
                    "o.rodzaj, " +
                    "o.opis, " +
                    "o.kwota " +
                    "FROM wypozyczenie_zwrot wz " +
                    "JOIN egzemplarz e ON (wz.id_egzemplarza=e.id_egzemplarza) " +
                    "JOIN konto k ON (wz.id_konta=k.id_konta) " +
                    "LEFT JOIN statusrezerwacji sr ON (wz.id_StatusRezerwacji=sr.id_statusRezerwacji) " +
                    "LEFT JOIN oplata o ON (wz.id_oplaty=o.id_oplaty) " +
                    "WHERE wz.id_wypozyczenia_zwrotu=?;");
            ps.setInt(1, loanId);

            logger.info(ps.toString());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Copy copy = null;
                int copyId = rs.getInt("wz.id_egzemplarza");
                if (!rs.wasNull()) {
                    copy = new Copy(copyId,
                            rs.getInt("e.id_ksiazki"),
                            rs.getBoolean("e.wycofany"),
                            rs.getInt("wz.id_StatusRezerwacji"));
                }

                Account account = null;
                int accountId = rs.getInt("wz.id_konta");
                if (!rs.wasNull()) {
                    account = new Account(accountId,
                            rs.getString("k.login"),
                            rs.getString("k.haslo"),
                            rs.getNString("k.email"),
                            rs.getDate("k.dataUtworzenia"),
                            rs.getInt("k.id_uprawnienia")
                    );
                }

                ReservationStatus reservationStatus = null;
                int reservationStatusId =  rs.getInt("wz.id_StatusRezerwacji");
                if (!rs.wasNull()) {
                    reservationStatus = new ReservationStatus(reservationStatusId,
                            rs.getString("sr.statusRezerwacji")
                    );
                }

                Charge charge = null;
                int chargeId = rs.getInt("wz.id_oplaty");
                if (!rs.wasNull()) {
                    charge = new Charge(chargeId,
                            rs.getString("o.rodzaj"),
                            rs.getString("o.opis"),
                            rs.getDouble("o.kwota")
                    );
                }

                return Optional.of(new LoanDto(
                        rs.getInt("wz.id_wypozyczenia_zwrotu"),
                        copy,
                        account,
                        reservationStatus,
                        charge,
                        rs.getDate("wz.dataRezerwacji"),
                        rs.getDate("wz.dataWygasnieciaRezerwacji"),
                        rs.getString("wz.miejsceOdbioru"),
                        rs.getDate("wz.dataWypozyczenia"),
                        rs.getDate("wz.dataZwrotu")
                ));
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return Optional.empty();
    }

    public List<LoanDto> getAll() {
        List<LoanDto> resultList =  new ArrayList<>();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT " +
                    "wz.id_wypozyczenia_zwrotu, " +
                    "wz.id_egzemplarza, " +
                    "wz.id_konta, " +
                    "wz.id_StatusRezerwacji, " +
                    "wz.id_oplaty, " +
                    "wz.dataRezerwacji, " +
                    "wz.dataWygasnieciaRezerwacji, " +
                    "wz.miejsceOdbioru, " +
                    "wz.dataWypozyczenia, " +
                    "wz.dataZwrotu, " +
                    "e.id_ksiazki, " +
                    "e.wycofany, " +
                    "k.login," +
                    "k.haslo," +
                    "k.email," +
                    "k.dataUtworzenia," +
                    "k.id_uprawnienia," +
                    "k.dataUtworzenia," +
                    "sr.statusRezerwacji ," +
                    "o.rodzaj, " +
                    "o.opis, " +
                    "o.kwota " +
                    "FROM wypozyczenie_zwrot wz " +
                    "JOIN egzemplarz e ON (wz.id_egzemplarza=e.id_egzemplarza) " +
                    "JOIN konto k ON (wz.id_konta=k.id_konta) " +
                    "LEFT JOIN statusrezerwacji sr ON (wz.id_StatusRezerwacji=sr.id_statusRezerwacji) " +
                    "LEFT JOIN oplata o ON (wz.id_oplaty=o.id_oplaty) ;");

            logger.info(ps.toString());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resultList.add(new LoanDto(
                    rs.getInt("wz.id_wypozyczenia_zwrotu"),
                        new Copy(
                                rs.getInt("wz.id_egzemplarza"),
                                rs.getInt("e.id_ksiazki"),
                                rs.getBoolean("e.wycofany"),
                                rs.getInt("wz.id_StatusRezerwacji")
                        ),
                        new Account(
                            rs.getInt("wz.id_konta"),
                            rs.getString("k.login"),
                            rs.getString("k.haslo"),
                            rs.getNString("k.email"),
                            rs.getDate("k.dataUtworzenia"),
                            rs.getInt("k.id_uprawnienia")
                        ),
                        new ReservationStatus(
                            rs.getInt("wz.id_StatusRezerwacji"),
                            rs.getString("sr.statusRezerwacji")
                        ),
                        new Charge(
                            rs.getInt("wz.id_oplaty"),
                            rs.getString("o.rodzaj"),
                            rs.getString("o.opis"),
                            rs.getDouble("o.kwota")
                        ),
                        rs.getDate("wz.dataRezerwacji"),
                        rs.getDate("wz.dataWygasnieciaRezerwacji"),
                        rs.getString("wz.miejsceOdbioru"),
                        rs.getDate("wz.dataWypozyczenia"),
                        rs.getDate("wz.dataZwrotu")
                ));
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return resultList;
    }

    public int update(LoanDto loanDto) {
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("UPDATE wypozyczenie_zwrot SET " +
                            "id_egzemplarza=?, " +
                            "id_konta=?, " +
                            "id_StatusRezerwacji=?, " +
                            "id_oplaty=?, " +
                            "dataRezerwacji=?, " +
                            "dataWygasnieciaRezerwacji=?, " +
                            "miejsceOdbioru=?, " +
                            "dataWypozyczenia=?, " +
                            "dataZwrotu=? " +
                            "WHERE id_wypozyczenia_zwrotu=? ;");
            int iterator = 1;

            Copy copy = loanDto.getCopy();
            Account account = loanDto.getAccount();
            ReservationStatus reservationStatus = loanDto.getReservationStatus();
            Charge charge = loanDto.getCharge();

            if (copy != null)
                ps.setInt(iterator++, copy.getCopyId());
            else
                ps.setNull(iterator++, Types.INTEGER);

            if (account != null)
                ps.setInt(iterator++, account.getAccountId());
            else
                ps.setNull(iterator++, Types.INTEGER);

            if (reservationStatus != null)
                ps.setInt(iterator++, reservationStatus.getReservationStatusId());
            else
                ps.setNull(iterator++, Types.INTEGER);

            if (charge != null)
                ps.setInt(iterator++, charge.getChargeId());
            else
                ps.setNull(iterator++, Types.INTEGER);

            ps.setDate(iterator++, loanDto.getReservationDate());
            ps.setDate(iterator++, loanDto.getExpireDate());
            ps.setNString(iterator++, loanDto.getPickupPoint());
            ps.setDate(iterator++, loanDto.getStartDate());
            ps.setDate(iterator++, loanDto.getEndDate());
            ps.setInt(iterator++, loanDto.getLoanId());

            logger.info(ps.toString());

            int rowCount = ps.executeUpdate();
            if (rowCount > 0) {
                return rowCount;
            }

        } catch (SQLException e) {
            logger.error(e);
        }

        return -1;
    }

    public int add(Integer copyId, Integer accountId) {
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("INSERT INTO wypozyczenie_zwrot (" +
                            "id_egzemplarza, " +
                            "id_konta, " +
                            "id_StatusRezerwacji, " +
                            "id_oplaty, " +
                            "dataRezerwacji, " +
                            "dataWygasnieciaRezerwacji, " +
                            "miejsceOdbioru, " +
                            "dataWypozyczenia, " +
                            "dataZwrotu " +
                            ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS);

            LocalDate now = LocalDate.now();
            LocalDate nextTwoWeeks = LocalDate.now().plusWeeks(2);
            LocalDate nextTwoMonths = LocalDate.now().plusMonths(2);

            int iterator = 1;
            ps.setInt(iterator++, copyId);
            ps.setInt(iterator++, accountId);
            ps.setInt(iterator++, ReservationStatus.RESERVED);
            ps.setNull(iterator++, Types.INTEGER);
            ps.setDate(iterator++, Date.valueOf(now));
            ps.setDate(iterator++, Date.valueOf(nextTwoWeeks));
            ps.setNString(iterator++, null);
            ps.setDate(iterator++, null);
            ps.setDate(iterator++, Date.valueOf(nextTwoMonths));

            logger.info(ps.toString());

            int rowCount = ps.executeUpdate();
            if (rowCount > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    int loadId = keys.getInt(1);
                    logger.info("loadId: " + loadId);
                    return loadId;
                }
            }

        } catch (SQLException e) {
            logger.error(e);
        }

        return -1;
    }

    public List<LoanDto> getByAccountId(int accountId) {
        List<LoanDto> loans = new ArrayList<>();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT " +
                    "wz.id_wypozyczenia_zwrotu, " +
                    "wz.id_egzemplarza, " +
                    "wz.id_konta, " +
                    "wz.id_StatusRezerwacji, " +
                    "wz.id_oplaty, " +
                    "wz.dataRezerwacji, " +
                    "wz.dataWygasnieciaRezerwacji, " +
                    "wz.miejsceOdbioru, " +
                    "wz.dataWypozyczenia, " +
                    "wz.dataZwrotu, " +
                    "e.id_ksiazki, " +
                    "e.wycofany, " +
                    "k.login," +
                    "k.haslo," +
                    "k.email," +
                    "k.dataUtworzenia," +
                    "k.id_uprawnienia," +
                    "k.dataUtworzenia," +
                    "sr.statusRezerwacji ," +
                    "o.rodzaj, " +
                    "o.opis, " +
                    "o.kwota " +
                    "FROM wypozyczenie_zwrot wz " +
                    "JOIN egzemplarz e ON (wz.id_egzemplarza=e.id_egzemplarza) " +
                    "JOIN konto k ON (wz.id_konta=k.id_konta) " +
                    "LEFT JOIN statusrezerwacji sr ON (wz.id_StatusRezerwacji=sr.id_statusRezerwacji) " +
                    "LEFT JOIN oplata o ON (wz.id_oplaty=o.id_oplaty) " +
                    "WHERE wz.id_konta=?;");
            ps.setInt(1, accountId);

            logger.info(ps.toString());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                loans.add(new LoanDto(
                        rs.getInt("wz.id_wypozyczenia_zwrotu"),
                        new Copy(
                                rs.getInt("wz.id_egzemplarza"),
                                rs.getInt("e.id_ksiazki"),
                                rs.getBoolean("e.wycofany"),
                                rs.getInt("wz.id_StatusRezerwacji")
                        ),
                        new Account(
                                rs.getInt("wz.id_konta"),
                                rs.getString("k.login"),
                                rs.getString("k.haslo"),
                                rs.getNString("k.email"),
                                rs.getDate("k.dataUtworzenia"),
                                rs.getInt("k.id_uprawnienia")
                        ),
                        new ReservationStatus(
                                rs.getInt("wz.id_StatusRezerwacji"),
                                rs.getString("sr.statusRezerwacji")
                        ),
                        new Charge(
                                rs.getInt("wz.id_oplaty"),
                                rs.getString("o.rodzaj"),
                                rs.getString("o.opis"),
                                rs.getDouble("o.kwota")
                        ),
                        rs.getDate("wz.dataRezerwacji"),
                        rs.getDate("wz.dataWygasnieciaRezerwacji"),
                        rs.getString("wz.miejsceOdbioru"),
                        rs.getDate("wz.dataWypozyczenia"),
                        rs.getDate("wz.dataZwrotu")
                ));
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return loans;
    }
}
