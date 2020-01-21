package pl.edu.pk.biblioteka.dto.dao;

import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.Dao;
import pl.edu.pk.biblioteka.data.Account;
import pl.edu.pk.biblioteka.data.Ebook;
import pl.edu.pk.biblioteka.data.dao.AccountDao;
import pl.edu.pk.biblioteka.dto.BookDto;
import pl.edu.pk.biblioteka.dto.EbookDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EbookDao extends Dao<EbookDto> {
    private static final Logger logger = Logger.getLogger(EbookDao.class.getName());

    public Optional<EbookDto> get(int copyId) {
        BookDao bookDao = new BookDao();
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement("SELECT id_ibuka, id_ksiazki FROM ibuk " +
                    "WHERE id_ibuka = ? ;");
            ps.setInt(1, copyId);

            logger.info(ps.toString());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Optional<BookDto> book = bookDao.get(rs.getInt("id_ksiazki"));
                if (book.isPresent()) {
                    return Optional.of(new EbookDto(
                            rs.getInt("id_ibuka"),
                            book.get()));
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            bookDao.close();
        }

        return Optional.empty();
    }
}
