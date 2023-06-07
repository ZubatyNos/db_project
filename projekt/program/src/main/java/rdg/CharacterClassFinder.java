package rdg;

import main.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Rastislav Urbanek
 */
public class CharacterClassFinder {
    private static final CharacterClassFinder INSTANCE = new CharacterClassFinder();
    public static CharacterClassFinder getInstance() { return INSTANCE; }
    private CharacterClassFinder() { }

    public CharacterClass findById(int id) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(
                "SELECT * FROM classes WHERE id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    CharacterClass c = load(r);

                    return c;
                } else {
                    return null;
                }
            }
        }
    }




    public Set<CharacterClass> findByRaceId(int raceId) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(
                "SELECT * FROM classes c JOIN race_classes rc on c.id = rc.class_id WHERE rc.race_id = ?")) {
            s.setInt(1, raceId);

            try (ResultSet r = s.executeQuery()) {
                Set<CharacterClass> elements = new HashSet<>();

                while (r.next()) elements.add(load(r));

                return elements;
            }
        }
    }

    public List<CharacterClass> findAll() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM classes ORDER BY id")) {
            try (ResultSet r = s.executeQuery()) {

                List<CharacterClass> elements = new ArrayList<>();

                while (r.next()) elements.add(load(r));

                return elements;
            }
        }
    }

    private CharacterClass load(ResultSet r) throws SQLException {
        CharacterClass c = new CharacterClass();
        c.setId(r.getInt("id"));
        c.setName(r.getString("name"));
        c.setMaxHealthPerLevel(r.getInt("max_health_per_level"));
        c.setDefensePerLevel(r.getInt("defense_per_level"));
        c.setPowerPerLevel(r.getInt("power_per_level"));
        return c;
    }
}
