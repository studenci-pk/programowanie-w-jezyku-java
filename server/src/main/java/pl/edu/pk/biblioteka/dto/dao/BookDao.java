package pl.edu.pk.biblioteka.dto.dao;

import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.Dao;
import pl.edu.pk.biblioteka.data.Author;
import pl.edu.pk.biblioteka.data.Department;
import pl.edu.pk.biblioteka.data.Publisher;
import pl.edu.pk.biblioteka.dto.BookDto;
import pl.edu.pk.biblioteka.utils.DatabaseConnector;
import pl.edu.pk.biblioteka.utils.MapUtils;

import java.sql.*;
import java.util.*;

public class BookDao extends Dao<BookDto> {
    private static final Logger logger = Logger.getLogger(BookDao.class.getName());

    public Optional<BookDto> get(int bookId) {
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT * " +
                    "FROM ksiazka " +
                    "JOIN autor ON (ksiazka.id_wydawnictwa=autor.id_autora) " +
                    "JOIN wydawnictwo ON (ksiazka.id_wydawnictwa=wydawnictwo.id_wydawnictwa) " +
                    "JOIN dzial ON (ksiazka.id_dzialu=dzial.id_dzialu) " +
                    "WHERE id_ksiazki=?;");
            ps.setInt(1, bookId);

            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return Optional.of(new BookDto(
                        rs.getInt("id_ksiazki"),
                        rs.getString("sygnatura"),
                        new Author(
                                rs.getInt("id_autora"),
                                rs.getString("imie"),
                                rs.getString("nazwisko"),
                                rs.getString("krajPochodzenia")
                        ),
                        new Publisher(
                                rs.getInt("id_wydawnictwa"),
                                rs.getString("nazwa"),
                                rs.getString("miejscowosc"),
                                rs.getString("kraj")
                        ),
                        new Department(
                                rs.getInt("id_dzialu"),
                                rs.getString("nazwa")
                        ),
                        rs.getString("tytul"),
                        rs.getString("kategoria"),
                        rs.getString("slowoKlucz"),
                        rs.getBoolean("wycofana")
                ));
            }

        } catch (SQLException e) {
            logger.error(e);
        }

        return Optional.empty();
    }

    public List<BookDto> getAll() {
        List<BookDto> books = new ArrayList<>();
        PreparedStatement ps;

        try {
            String sqlStatement = "SELECT " +
                    "k.id_ksiazki, k.sygnatura, k.id_autora, a.imie, a.nazwisko, a.krajPochodzenia, " +
                    "k.id_wydawnictwa, w.nazwa, w.miejscowosc, w.kraj, k.id_dzialu, d.nazwa, " +
                    "k.tytul, k.kategoria, k.slowoKlucz, k.wycofana " +
                    "FROM ksiazka k " +
                    "JOIN autor a ON (k.id_autora=a.id_autora) " +
                    "JOIN wydawnictwo w ON (k.id_wydawnictwa=w.id_wydawnictwa) " +
                    "JOIN dzial d ON (k.id_dzialu=d.id_dzialu) ;";
            ps = conn.prepareStatement(sqlStatement);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                books.add(new BookDto(
                        rs.getInt("k.id_ksiazki"),
                        rs.getString("k.sygnatura"),
                        new Author(
                                rs.getInt("k.id_autora"),
                                rs.getString("a.imie"),
                                rs.getString("a.nazwisko"),
                                rs.getString("a.krajPochodzenia")
                        ),
                        new Publisher(
                                rs.getInt("k.id_wydawnictwa"),
                                rs.getString("w.nazwa"),
                                rs.getString("w.miejscowosc"),
                                rs.getString("w.kraj")
                        ),
                        new Department(
                                rs.getInt("k.id_dzialu"),
                                rs.getString("d.nazwa")
                        ),
                        rs.getString("k.tytul"),
                        rs.getString("k.kategoria"),
                        rs.getString("k.slowoKlucz"),
                        rs.getBoolean("k.wycofana")
                ));
            }

        } catch (SQLException e) {
            logger.error(e);
        }

        return books;
    }

    public int update(BookDto book) {
        PreparedStatement ps;

        try {
            ps = conn.prepareStatement("UPDATE ksiazka " +
                    "SET sygnatura=?, id_autora=?, id_wydawnictwa=?, id_dzialu=?, tytul=?, kategoria=?, slowoKlucz=?, wycofana=?  " +
                    "WHERE id_ksiazki=?;");
            int iterator = 1;
            ps.setString(iterator++, book.getSignature());
            ps.setInt(iterator++, book.getAuthor().getAuthorId());
            ps.setInt(iterator++, book.getPublisher().getPublisherId());
            ps.setInt(iterator++, book.getDepartment().getDepartmentId());
            ps.setString(iterator++, book.getTitle());
            ps.setString(iterator++, book.getCategory());
            ps.setString(iterator++, book.getKeywords());
            ps.setBoolean(iterator++, book.isWithdrawn());
            ps.setInt(iterator++, book.getBookId());

            logger.info(ps.toString());

            int rowCount = ps.executeUpdate();
            if (rowCount > 0) {
                logger.info("rowCount: " + rowCount);
                return rowCount;
            }

        } catch (SQLException e) {
            logger.error(e);
        }

        return -1;
    }

    public List<BookDto> get(String title) {
        List<BookDto> books = new ArrayList<>();
        PreparedStatement ps;

        try {
            ps = conn.prepareStatement("SELECT * " +
                    "FROM ksiazka " +
                    "JOIN autor ON (ksiazka.id_wydawnictwa=autor.id_autora) " +
                    "JOIN wydawnictwo ON (ksiazka.id_wydawnictwa=wydawnictwo.id_wydawnictwa) " +
                    "JOIN dzial ON (ksiazka.id_dzialu=dzial.id_dzialu) " +
                    "WHERE tytul=? ;", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, "%" + title + "%");

            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                books.add(new BookDto(
                        rs.getInt("id_ksiazki"),
                        rs.getString("sygnatura"),
                        new Author(
                                rs.getInt("id_autora"),
                                rs.getString("imie"),
                                rs.getString("nazwisko"),
                                rs.getString("krajPochodzenia")
                        ),
                        new Publisher(
                                rs.getInt("id_wydawnictwa"),
                                rs.getString("nazwa"),
                                rs.getString("miejscowosc"),
                                rs.getString("kraj")
                        ),
                        new Department(
                                rs.getInt("id_dzialu"),
                                rs.getString("nazwa")
                        ),
                        rs.getString("tytul"),
                        rs.getString("kategoria"),
                        rs.getString("slowoKlucz"),
                        rs.getBoolean("wycofana")
                ));
            }

        } catch (SQLException e) {
            logger.error(e);
        }

        return books;
    }

    public int add(String signature, int authorId, int publisherId, int departmentId, String title, String category, String keywords) {
        PreparedStatement ps;

        try {
            ps = conn.prepareStatement("INSERT INTO ksiazka " +
                    "(sygnatura, id_autora, id_wydawnictwa, id_dzialu, tytul, kategoria, slowoKlucz) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
            int iterator = 1;
            ps.setString(iterator++, signature);
            ps.setInt(iterator++, authorId);
            ps.setInt(iterator++, publisherId);
            ps.setInt(iterator++, departmentId);
            ps.setString(iterator++, title);
            ps.setString(iterator++, category);
            ps.setString(iterator++, keywords);

            int rowCount = ps.executeUpdate();
            logger.info("rowCount: " + rowCount);

            if (rowCount == 1) {
                ResultSet resultSet = ps.getGeneratedKeys();
                if (resultSet.next()) {
                    int bookId = resultSet.getInt(1);
                    logger.info("bookId: " + bookId);
                    return bookId;
                }
            }

        } catch (SQLException e) {
            logger.error(e);
        }

        return -1;
    }

    public List<BookDto> getAll(int limit) {
        List<BookDto> books = new ArrayList<>();
        PreparedStatement ps;

        try {
            ps = conn.prepareStatement("SELECT * " +
                    "FROM ksiazka " +
                    "JOIN autor ON (ksiazka.id_wydawnictwa=autor.id_autora) " +
                    "JOIN wydawnictwo ON (ksiazka.id_wydawnictwa=wydawnictwo.id_wydawnictwa) " +
                    "JOIN dzial ON (ksiazka.id_dzialu=dzial.id_dzialu) " +
                    "LIMIT ?;");
            ps.setInt(1, limit);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                books.add(new BookDto(
                        rs.getInt("id_ksiazki"),
                        rs.getString("sygnatura"),
                        new Author(
                                rs.getInt("id_autora"),
                                rs.getString("imie"),
                                rs.getString("nazwisko"),
                                rs.getString("krajPochodzenia")
                        ),
                        new Publisher(
                                rs.getInt("id_wydawnictwa"),
                                rs.getString("nazwa"),
                                rs.getString("miejscowosc"),
                                rs.getString("kraj")
                        ),
                        new Department(
                                rs.getInt("id_dzialu"),
                                rs.getString("nazwa")
                        ),
                        rs.getString("tytul"),
                        rs.getString("kategoria"),
                        rs.getString("slowoKlucz"),
                        rs.getBoolean("wycofana")
                ));
            }

        } catch (SQLException e) {
            logger.error(e);
        }

        return books;
    }

    public int update(int bookId, int authorId, int publisherId, int departmentId, String title, String category, String keywords) {
        PreparedStatement ps;

        try {
            ps = conn.prepareStatement("UPDATE ksiazka " +
                    "SET id_autora=?, id_wydawnictwa=?, id_dzialu=?, tytul=?, kategoria=?, slowoKlucz=?  " +
                    "WHERE id_ksiazki=?;");
            int iterator = 1;
            ps.setInt(iterator++, authorId);
            ps.setInt(iterator++, publisherId);
            ps.setInt(iterator++, departmentId);
            ps.setString(iterator++, title);
            ps.setString(iterator++, category);
            ps.setString(iterator++, keywords);
            ps.setInt(iterator, bookId);

            int rowCount = ps.executeUpdate();

            logger.info("rowCount: " + rowCount);
            if (rowCount > 0) {
                return rowCount;
            }

        } catch (SQLException e) {
            logger.error(e);
        }

        return -1;
    }

    public List<BookDto> getByMap(Map<String, String> map) {

        if (map.isEmpty()) {
            return getAll();
        }

        List<BookDto> resultList =  new ArrayList<>();
        PreparedStatement ps;
        try {
            logger.info(map.toString());
//            MapUtils.renameKey(map, "signature", "k.sygnatura");
//            MapUtils.renameKey(map, "publisher", "w.nazwa");
//            MapUtils.renameKey(map, "department", "d.nazwa");
//            MapUtils.renameKey(map, "title", "k.tytul");
//            MapUtils.renameKey(map, "category", "k.kategoria");
//            MapUtils.renameKey(map, "keywords", "k.slowoKlucz");
//            MapUtils.renameKey(map, "author", "a.imie");

            ps = conn.prepareStatement("SELECT " +
                    "k.id_ksiazki, k.sygnatura, k.id_autora, a.imie, a.nazwisko, a.krajPochodzenia, " +
                    "k.id_wydawnictwa, w.nazwa, w.miejscowosc, w.kraj, k.id_dzialu, d.nazwa, " +
                    "k.tytul, k.kategoria, k.slowoKlucz, k.wycofana " +
                    "FROM ksiazka k " +
                    "JOIN autor a ON (k.id_autora=a.id_autora) " +
                    "JOIN wydawnictwo w ON (k.id_wydawnictwa=w.id_wydawnictwa) " +
                    "JOIN dzial d ON (k.id_dzialu=d.id_dzialu) " +
                    "WHERE k.sygnatura LIKE ? AND " +
                    "      w.nazwa LIKE ? AND " +
                    "      d.nazwa LIKE ? AND " +
                    "      k.tytul LIKE ? AND " +
                    "      k.kategoria LIKE ? AND " +
                    "      k.slowoKlucz LIKE ? AND " +
                    "      (? LIKE CONCAT('%', a.imie, '%') OR " +
                    "      ? LIKE CONCAT('%', a.nazwisko, '%') OR " +
                    "      a.imie LIKE ? OR a.nazwisko LIKE ?) ;");

            int iterator = 1;
            ps.setNString(iterator++, '%' + map.getOrDefault("signature", "") + '%');
            ps.setNString(iterator++, '%' + map.getOrDefault("publisher", "") + '%');
            ps.setNString(iterator++, '%' + map.getOrDefault("department", "") + '%');
            ps.setNString(iterator++, '%' + map.getOrDefault("title", "") + '%');
            ps.setNString(iterator++, '%' + map.getOrDefault("category", "") + '%');
            ps.setNString(iterator++, '%' + map.getOrDefault("keywords", "") + '%');
            ps.setNString(iterator++, '%' + map.getOrDefault("author", "") + '%');
            ps.setNString(iterator++, '%' + map.getOrDefault("author", "") + '%');
            ps.setNString(iterator++, '%' + map.getOrDefault("author", "") + '%');
            ps.setNString(iterator++, '%' + map.getOrDefault("author", "") + '%');

            logger.info(ps.toString());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resultList.add(new BookDto(
                        rs.getInt("k.id_ksiazki"),
                        rs.getString("k.sygnatura"),
                        new Author(
                                rs.getInt("k.id_autora"),
                                rs.getString("a.imie"),
                                rs.getString("a.nazwisko"),
                                rs.getString("a.krajPochodzenia")
                        ),
                        new Publisher(
                                rs.getInt("k.id_wydawnictwa"),
                                rs.getString("w.nazwa"),
                                rs.getString("w.miejscowosc"),
                                rs.getString("w.kraj")
                        ),
                        new Department(
                                rs.getInt("k.id_dzialu"),
                                rs.getString("d.nazwa")
                        ),
                        rs.getString("k.tytul"),
                        rs.getString("k.kategoria"),
                        rs.getString("k.slowoKlucz"),
                        rs.getBoolean("wycofana")
                ));
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return resultList;
    }

    public boolean remove(BookDto book) {
        return remove(book.getBookId());
    }

    public boolean remove(int bookId) {
        PreparedStatement ps;

        try {
            ps = conn.prepareStatement("DELETE FROM ksiazka " +
                    "WHERE id_ksiazki = ?;");
            ps.setInt(1, bookId);

            int rowCount = ps.executeUpdate();

            logger.info("rowCount: " + rowCount);
            if (rowCount > 0) {
                return true;
            }

        } catch (SQLException e) {
            logger.error(e);
        }

        return false;
    }

    public Optional<String> getMaxSignature() {
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT MAX(sygnatura) FROM ksiazka;");

            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return Optional.ofNullable(rs.getString(1));
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return Optional.empty();
    }
}
