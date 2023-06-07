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

public class AbilityFinder {

    private static final AbilityFinder INSTANCE = new AbilityFinder();

    public static AbilityFinder getInstance() { return INSTANCE; }
    private AbilityFinder() { }


    public List<Ability> findByCharacterClassId(int characterClassId) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(
                "SELECT * FROM abilities a JOIN class_abilities c_a ON c_a.ability_id = a.id WHERE c_a.class_id = ? ORDER BY id")) {
                s.setInt(1, characterClassId);
            try (ResultSet r = s.executeQuery()) {
                List<Ability> elements = new ArrayList<>();

                while (r.next()) elements.add(load(r));

                return elements;
            }
        }
    }

    public Ability load(ResultSet r) throws SQLException {
        Ability a = new Ability();
        a.setId(r.getInt("id"));
        a.setName(r.getString("name"));
        a.setPowerFactor(r.getFloat("power_factor"));
        a.setHealing(r.getBoolean("healing"));
        return a;
    }
}
