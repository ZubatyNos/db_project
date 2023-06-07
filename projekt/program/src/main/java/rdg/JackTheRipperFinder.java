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
public class JackTheRipperFinder {
    private static final JackTheRipperFinder INSTANCE = new JackTheRipperFinder();

    public static JackTheRipperFinder getInstance() { return INSTANCE; }

    private JackTheRipperFinder() { }

    public List<JackTheRipper> findAllWeekly() throws SQLException {
        String sql = "select DISTINCT on (extract(week from date_of_fight))\n" +
                "    winner_id,\n" +
                "    count(winner_id) as all_defeated,\n" +
                "    extract(week from date_of_fight) as week,\n" +
                "    count(c.gender) filter (WHERE c.gender = 'F') as female_defeated,\n" +
                "    count(c.gender) filter (WHERE c.gender = 'M') as male_defeated\n" +
                "from fights\n" +
                "         join characters c on c.id = loser_id\n" +
                "group by (winner_id, week)\n" +
                "order by week, all_defeated desc;";

        try (PreparedStatement s = DbContext.getConnection().prepareStatement(sql)) {
            try (ResultSet r = s.executeQuery()) {

                List<JackTheRipper> elements = new ArrayList<>();

                while (r.next()) {
                    JackTheRipper j = new JackTheRipper();
                    j.setWinnerId(r.getInt("winner_id"));
                    j.setAllDefeated(r.getInt("all_defeated"));
                    j.setMaleDefeated(r.getInt("male_defeated"));
                    j.setFemaleDefeated(r.getInt("female_defeated"));
                    j.setWeek(r.getInt("week"));
                    j.setMonth(null);
                    elements.add(j);
                }
                return elements;
            }
        }
    }

    public List<JackTheRipper> findAllMonthly() throws SQLException {
        String sql = "select DISTINCT on (extract(month from date_of_fight))\n" +
                "    winner_id,\n" +
                "    count(winner_id) as all_defeated,\n" +
                "    extract(month from date_of_fight) as month,\n" +
                "    count(c.gender) filter (WHERE c.gender = 'F') as female_defeated,\n" +
                "    count(c.gender) filter (WHERE c.gender = 'M') as male_defeated\n" +
                "from fights\n" +
                "join characters c on c.id = loser_id\n" +
                "group by (winner_id, month)\n" +
                "order by month, all_defeated desc;";
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(sql)) {
            try (ResultSet r = s.executeQuery()) {

                List<JackTheRipper> elements = new ArrayList<>();

                while (r.next()) {
                    JackTheRipper j = new JackTheRipper();
                    j.setWinnerId(r.getInt("winner_id"));
                    j.setAllDefeated(r.getInt("all_defeated"));
                    j.setMaleDefeated(r.getInt("male_defeated"));
                    j.setFemaleDefeated(r.getInt("female_defeated"));
                    j.setWeek(null);
                    j.setMonth(r.getInt("month"));
                    elements.add(j);
                }
                return elements;
            }
        }
    }
}
