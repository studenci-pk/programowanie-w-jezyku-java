package pl.edu.pk.biblioteka.dto.dao;

import org.apache.log4j.Logger;
import pl.edu.pk.biblioteka.Dao;
import pl.edu.pk.biblioteka.data.Cart;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDao extends Dao<Cart> {
    private static final Logger logger = Logger.getLogger(CartDao.class.getName());

    public List<String> getByAccountId(int accountId) {
        List<String> list = new ArrayList<>();
        //wyciaganie info z cart
        try (PreparedStatement ps = conn.prepareStatement("SELECT DISTINCT tytul FROM koszyk WHERE id_konta = ?;")) {
            ps.setInt(1, accountId);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {
                list.add(rs.getNString("tytul"));
            }
        } catch (SQLException e) {
            logger.error(e);
        }
    return list;
    }

    public int add(String tytul, int idKonta){
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO koszyk (id_konta, tytul) " +
                    "VALUES (?, ?);");
            int iterator = 1;
            ps.setInt(iterator++, idKonta);
            ps.setString(iterator++, tytul);

            int rowCount = ps.executeUpdate();

            if (rowCount == 1) {
                    return rowCount;
            }
        }catch (SQLException e) {
            logger.error(e);
        }

        return -1;
    }

    public boolean remove(String name, Integer accountId) {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM koszyk WHERE id_konta = ? AND tytul = ? ;");
            int iterator = 1;
            ps.setInt(iterator++, accountId);
            ps.setNString(iterator++, name);

            int rowCount = ps.executeUpdate();

            if (rowCount > 0) {
                return true;
            }
        }catch (SQLException e) {
            logger.error(e);
        }

        return false;
    }
}
