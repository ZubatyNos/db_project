package rdg;

import main.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Item {
    private int id;
    private String name;
    private int maxHealthModifier;
    private int defenseModifier;
    private int powerModifier;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getMaxHealthModifier() { return maxHealthModifier; }
    public void setMaxHealthModifier(int maxHealthModifier) { this.maxHealthModifier = maxHealthModifier; }
    public int getDefenseModifier() { return defenseModifier; }
    public void setDefenseModifier(int defenseModifier) { this.defenseModifier = defenseModifier; }
    public int getPowerModifier() { return powerModifier; }
    public void setPowerModifier(int powerModifier) { this.powerModifier = powerModifier; }


    public void insert() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(
                "INSERT INTO items (name, max_health_modifier, defense_modifier, power_modifier) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            s.setString(1, name);
            s.setInt(2, maxHealthModifier);
            s.setInt(3, defenseModifier);
            s.setInt(4, powerModifier);

            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }
}
