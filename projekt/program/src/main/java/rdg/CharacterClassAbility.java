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
public class CharacterClassAbility {
    private int abilityId;
    private int classId;

    public int getAbilityId() { return abilityId; }
    public void setAbilityId(int abilityId) { this.abilityId = abilityId; }
    public int getClassId() { return classId; }
    public void setClassId(int classId) { this.classId = classId; }

    public void insert() throws SQLException {
        String sql = "INSERT INTO class_abilities (ability_id, class_id) " +
                "VALUES (?,?)";
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(sql)) {
            s.setInt(1, abilityId);
            s.setInt(2, classId);

            s.executeUpdate();

        }
    }

    public void delete() throws SQLException {
        String sql = "DELETE FROM class_abilities WHERE ability_id = ? and class_id = ? ";
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(sql)) {
            s.setInt(1, abilityId);
            s.setInt(2, classId);

            s.executeUpdate();
        }
    }
}
