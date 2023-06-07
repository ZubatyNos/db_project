package rdg;

import main.DbContext;

import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 *
 * @author Rastislav Urbanek
 */
public class Ability {
    private int id;
    private String name;
    private float powerFactor;
    private boolean healing;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPowerFactor() { return powerFactor; }
    public void setPowerFactor(float powerFactor) { this.powerFactor = powerFactor; }
    public boolean isHealing() { return healing; }
    public void setHealing(boolean healing) { this.healing = healing; }



    public void delete() throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM accounts WHERE id = ?")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
    }


}
