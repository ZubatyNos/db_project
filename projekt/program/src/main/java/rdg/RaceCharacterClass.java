package rdg;

import main.DbContext;

import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 *
 * @author Rastislav Urbanek
 */
public class RaceCharacterClass {
    private int classId;
    private int raceId;

    public int getClassId() { return classId; }
    public void setClassId(int classId) { this.classId = classId; }
    public int getRaceId() { return raceId; }
    public void setRaceId(int raceId) { this.raceId = raceId; }

    public void insert() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(
                "INSERT INTO race_classes (class_id, race_id) VALUES (?, ?)")) {
            s.setInt(1, classId);
            s.setInt(2, raceId);

            s.executeUpdate();
        }
    }

    public void delete() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(
                "DELETE FROM race_classes WHERE class_id = ? and race_id = ?")) {
            s.setInt(1, classId);
            s.setInt(2, raceId);

            s.executeUpdate();
        }
    }
}
