package rdg;

import main.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Rastislav Urbanek
 */
public class ServiceFinder {

    private static final ServiceFinder INSTANCE = new ServiceFinder();

    public static ServiceFinder getInstance() { return INSTANCE; }
    private ServiceFinder() { }

    public List<Service> findAll() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(
                "SELECT * FROM services")) {
            try (ResultSet r = s.executeQuery()) {

                List<Service> elements = new ArrayList<>();

                while (r.next()) elements.add(load(r));
                return elements;
            }
        }
    }

    public Service findByType(String type) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(
                "SELECT * FROM services WHERE type = ?")) {
            s.setString(1, type);
            try (ResultSet r = s.executeQuery()) {
                return (r.next()) ? load(r) : null;
            }
        }
    }
    public Service load(ResultSet r) throws SQLException {
        Service s = new Service();
        s.setId(r.getInt("id"));
        s.setType(r.getString("type"));
        s.setCredits(r.getInt("credits"));
        return s;
    }
}
