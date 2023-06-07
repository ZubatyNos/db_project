package rdg;

import main.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Rastislav Urbanek
 */
public class CharacterClass {
    private int id;
    private String name;
    private int maxHealthPerLevel;
    private int defensePerLevel;
    private int powerPerLevel;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getMaxHealthPerLevel() { return maxHealthPerLevel; }
    public void setMaxHealthPerLevel(int maxHealthPerLevel) { this.maxHealthPerLevel = maxHealthPerLevel; }
    public int getDefensePerLevel() { return defensePerLevel; }
    public void setDefensePerLevel(int defensePerLevel) { this.defensePerLevel = defensePerLevel; }
    public int getPowerPerLevel() { return powerPerLevel; }
    public void setPowerPerLevel(int powerPerLevel) { this.powerPerLevel = powerPerLevel; }


    public void insert() throws SQLException {
        String sql = "INSERT INTO classes (name, max_health_per_level, defense_per_level, power_per_level) " +
                "VALUES (?,?,?,?)";
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            s.setString(1, name);
            s.setInt(2, maxHealthPerLevel);
            s.setInt(3, defensePerLevel);
            s.setInt(4, powerPerLevel);

            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }

    public void update() throws SQLException {
        if (name == null) {
            throw new IllegalStateException("A value is null");
        }

        try (PreparedStatement s = DbContext.getConnection().prepareStatement(
                "UPDATE classes SET name = ?, max_health_per_level = ?, defense_per_level = ?, power_per_level = ? WHERE id = ?")) {
            s.setString(1, name);
            s.setInt(2, maxHealthPerLevel);
            s.setInt(3, defensePerLevel);
            s.setInt(4, powerPerLevel);
            s.setInt(5, id);

            s.executeUpdate();
        }
    }

    public void delete() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM classes WHERE id = ?")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
    }
}
