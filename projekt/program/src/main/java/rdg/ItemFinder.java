package rdg;

import main.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemFinder {
    private static final ItemFinder INSTANCE = new ItemFinder();
    public static ItemFinder getInstance() { return INSTANCE; }
    private ItemFinder() { }

    public Item findById(int id) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(
                "SELECT * FROM items WHERE id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Item c = load(r);

                    if (r.next()) {
                        throw new RuntimeException("Move than one row was returned");
                    }

                    return c;
                } else {
                    return null;
                }
            }
        }
    }

    private Item load(ResultSet r) throws SQLException {
        Item c = new Item();
        c.setId(r.getInt("id"));
        c.setName(r.getString("name"));
        c.setMaxHealthModifier(r.getInt("max_health_modifier"));
        c.setDefenseModifier(r.getInt("defense_modifier"));
        c.setPowerModifier(r.getInt("power_modifier"));
        return c;
    }
}
