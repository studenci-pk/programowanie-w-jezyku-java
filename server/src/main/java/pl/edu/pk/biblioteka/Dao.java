package pl.edu.pk.biblioteka;

import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.utils.DatabaseConnector;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Klasa przehcowująca wspólne cechy wszystkich DAO
 * @param <T> typ na jakim działa DAO
 */
public abstract class Dao<T> {
    protected Connection conn;
    private static final Logger logger = Logger.getLogger(Dao.class.getName());

    /**
     * Kontruktor tworzący sam obiekt 'connection'
     */
    public Dao() {
        conn = DatabaseConnector.getConnection();
    }

    /**
     * Konstruktor Dao, pozwalający podać z zewnątrz obiekt 'connection'
     * @param connection obiekt reprezentujący połączenie z bazą
     */
    public Dao(Connection connection) {
        conn = connection;
    }

    /**
     Funkcja zamykająca połączenie z bazą
     */
    public void close() { //
        try {
            conn.close();
        } catch (SQLException e) {
            logger.error(e);
        }
    }

    /**
     * Nadpisany 'destruktor', ktory dodatkowo zamyka polaczenie
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
}
