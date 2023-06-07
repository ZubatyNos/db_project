package rdg;

import main.DbContext;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
/**
 *
 * @author Rastislav Urbanek
 */
public class Fight {
    private int winnerId;
    private int loserId;
    private Timestamp dateOfFight;

    public int getWinnerId() { return winnerId; }
    public void setWinnerId(int winnerId) { this.winnerId = winnerId; }
    public int getLoserId() { return loserId; }
    public void setLoserId(int loserId) { this.loserId = loserId; }
    public Timestamp getDateOfFight() { return dateOfFight; }
    public void setDateOfFight(Timestamp dateOfFight) { this.dateOfFight = dateOfFight; }

    public void insert() throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(
                "INSERT INTO fights (winner_id, loser_id, date_of_fight) VALUES (?,?,?)")) {
            s.setInt(1, winnerId);
            s.setInt(2, loserId);
            s.setTimestamp(3, dateOfFight);

            s.executeUpdate();

        }
    }


}
