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
public class RaceFinder {

    private static final RaceFinder INSTANCE = new RaceFinder();

    public static RaceFinder getInstance() { return INSTANCE; }
    private RaceFinder() { }


    public Race findById(int id) throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement(
                "SELECT * FROM races WHERE id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Race race = load(r);
                    return race;
                } else {
                    return null;
                }
            }
        }
    }

    public List<Race> findAll() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM races")) {
            try (ResultSet r = s.executeQuery()) {
                List<Race> elements = new ArrayList<>();

                while (r.next()) elements.add(load(r));

                return elements;
            }
        }
    }

    public Race load(ResultSet r) throws SQLException {
        Race s = new Race();
        s.setId(r.getInt("id"));
        s.setName(r.getString("name"));
        return s;
    }
}
