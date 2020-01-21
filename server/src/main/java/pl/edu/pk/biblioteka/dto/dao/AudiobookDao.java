package pl.edu.pk.biblioteka.dto.dao;


import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.Dao;
import pl.edu.pk.biblioteka.dto.AudiobookDto;
import pl.edu.pk.biblioteka.dto.AudiobookDto;
import pl.edu.pk.biblioteka.dto.BookDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AudiobookDao extends Dao<AudiobookDto> {
    private static final Logger logger = Logger.getLogger(AudiobookDao.class.getName());

    public Optional<AudiobookDto> get(int audiobookId) {
        PreparedStatement ps;
        BookDao bookDao = new BookDao();
        try {
            ps = conn.prepareStatement("SELECT id_audiobooka, id_ksiazki FROM audiobook " +
                    "WHERE id_audiobooka = ? ;");
            ps.setInt(1, audiobookId);

            logger.info(ps.toString());

            ResultSet rs = ps.executeQuery();


            if (rs.next()) {
                int bookId = rs.getInt("id_ksiazki");
                Optional<BookDto> book = bookDao.get(bookId);
                if (book.isPresent()) {
                    return Optional.of(new AudiobookDto(
                            audiobookId,
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
