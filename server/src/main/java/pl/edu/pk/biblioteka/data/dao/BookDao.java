package pl.edu.pk.biblioteka.data.dao;

import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.Dao;
import pl.edu.pk.biblioteka.data.BookDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDao extends Dao<BookDto> {
    private static final Logger logger = Logger.getLogger(BookDao.class.getName());

    public int add(BookDto book) {
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("INSERT INTO ksiazka " +
                    "(sygnatura, id_autora, id_wydawnictwa, id_dzialu, tytul, kategoria, slowoKlucz)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?);");
            int iterator = 1;
            ps.setString(iterator++, "");
            ps.setInt(iterator++, 1);
            ps.setInt(iterator++, 1);
            ps.setInt(iterator++, 1);
            ps.setString(iterator++, "");
            ps.setString(iterator++, "");
            ps.setString(iterator++, "");

            return ps.executeUpdate();

        } catch (SQLException e) {
            logger.error(e);
        }

        return 0;
    }

    public List<BookDto> get(String title) {
        List<BookDto> books = new ArrayList<>();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(
                    "SELECT k.id_ksiazki, k.sygnatura, CONCAT(a.imie, ' ', a.nazwisko), k.id_wydawnictwa, " +
                            "k.id_dzialu, k.tytul, k.kategoria, k.slowoKlucz, k.wycofana " +
                            "FROM ksiazka k NATURAL JOIN autor a WHERE tytul LIKE ?;");
            ps.setString(1, String.format("%c%s%c", '%', title, '%'));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                BookDto book = new BookDto(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3), //rs.getInt(3),
                        rs.getInt(4),
                        rs.getInt(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getBoolean(9));
                books.add(book);
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return books;
    }
}
