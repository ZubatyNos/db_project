package rdg;

import main.DbContext;
import ts.MyException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Rastislav Urbanek
 */
public class GameCharacterFinder {
    private static final GameCharacterFinder INSTANCE = new GameCharacterFinder();
    public static GameCharacterFinder getInstance() { return INSTANCE; }
    private GameCharacterFinder() { }

    public List<GameCharacter> findAllOfAccountId(int accountId) throws SQLException, MyException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM characters WHERE account_id = ?")) {
            s.setInt(1, accountId);
            try (ResultSet r = s.executeQuery()) {

                List<GameCharacter> elements = new ArrayList<>();

                while (r.next()) elements.add(load(r));

                return elements;
            }
        }
    }

    public List<GameCharacter> findAllOnPage(int n) throws SQLException, MyException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM characters OFFSET ? * ? LIMIT ?")) {
            s.setInt(1, n);
            int entriesPerPage = 50;
            s.setInt(2, entriesPerPage);
            s.setInt(3, entriesPerPage);
            try (ResultSet r = s.executeQuery()) {

                List<GameCharacter> elements = new ArrayList<>();

                while (r.next()) elements.add(load(r));

                return elements;
            }
        }
    }


    public GameCharacter findById(int id) throws SQLException, MyException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(
                "SELECT * FROM characters WHERE id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    GameCharacter c = load(r);
                    return c;
                } else {
                    return null;
                }
            }
        }
    }

    public List<GameCharacter> findBountyHunters() throws SQLException, MyException {
        String sql = "with best_bountyHunter as (\n" +
                "    SELECT winner_id, count(*)\n" +
                "    FROM fights f\n" +
                "    join characters c on c.id = f.winner_id\n" +
                "    where current_date - interval '1 month' <= f.date_of_fight and f.date_of_fight  <= current_date\n" +
                "    group by winner_id\n" +
                "    order by count(*) DESC\n" +
                "    limit 1\n" +
                ")\n" +
                "\n" +
                "SELECT c.*\n" +
                "    FROM ((select\n" +
                "        winner_id,\n" +
                "        count(*)\n" +
                "    FROM fights f\n" +
                "             join characters c2 on f.winner_id = c2.id and f.loser_id = (SELECT winner_id from best_bountyHunter)\n" +
                "    where current_date - interval '1 month' <= f.date_of_fight and f.date_of_fight <= current_date\n" +
                "    group by winner_id\n" +
                "    order by count(*) DESC\n" +
                "    LIMIT 3)\n" +
                "    UNION ALL\n" +
                "    (select * from best_bountyHunter)) as tmp\n" +
                "    join characters c on c.id = winner_id;";

        try (PreparedStatement s = DbContext.getConnection().prepareStatement(sql)) {
            try (ResultSet r = s.executeQuery()) {

                List<GameCharacter> elements = new ArrayList<>();

                while (r.next()) elements.add(load(r));

                return elements;
            }
        }
    }

    public GameCharacter load(ResultSet r) throws SQLException, MyException {
        GameCharacter c = new GameCharacter();
        c.setId(r.getInt("id"));
        c.setName(r.getString("name"));
        c.setLevel(r.getInt("level"));
        c.setExperience(r.getInt("experience"));
        c.setCurrentHealth(r.getInt("current_health"));
        c.setGender(r.getString("gender"));
        c.setHairType(r.getInt("hair_type"));
        c.setBodyType(r.getInt("body_type"));
        c.setCharacterClassId(r.getInt("class_id"));
        c.setRaceId(r.getInt("race_id"));
        c.setAccountId(r.getInt("account_id"));
        return c;
    }
}
