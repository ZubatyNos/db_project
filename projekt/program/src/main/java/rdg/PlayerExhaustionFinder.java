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
public class PlayerExhaustionFinder {
    private static final PlayerExhaustionFinder INSTANCE = new PlayerExhaustionFinder();

    public static PlayerExhaustionFinder getInstance() { return INSTANCE; }
    private PlayerExhaustionFinder() { }

    public List<PlayerExhaustion> findFirstX(int x) throws SQLException {

        try (PreparedStatement drop = DbContext.getConnection().prepareStatement("drop sequence if exists percentil;\n" +
                "create sequence percentil;\n" +
                "\n" +
                "create or replace function overall(dummy integer) returns integer language sql immutable as\n" +
                "$$\n" +
                "    select count(*)::integer\n" +
                "    from fights f\n" +
                "    where current_date - interval '7 day' <= f.date_of_fight and f.date_of_fight  <= current_date\n" +
                "$$;\n")) {
            drop.executeUpdate();
        }

        String sql =  "\n" +
                "with best_day as (\n" +
                "    select DISTINCT ON (account_id)\n" +
                "        account_id,\n" +
                "        extract(day from date_of_fight) as day,\n" +
                "        count(*)\n" +
                "    from fights f\n" +
                "    join characters c on c.id = f.winner_id\n" +
                "    where current_date - make_interval(0,0,0,7 + extract(day from date_of_fight)::integer % 7) <= f.date_of_fight\n" +
                "      and f.date_of_fight  <= current_date - make_interval(0,0,0,extract(day from date_of_fight)::integer % 7)\n" +
                "    group by (c.account_id, day)\n" +
                "    order by account_id, count(*) desc\n" +
                "),\n" +
                "best_hour as (\n" +
                "    select DISTINCT ON (account_id)\n" +
                "        account_id,\n" +
                "        extract(hour from date_of_fight) as hour,\n" +
                "        count(*)\n" +
                "    from fights f\n" +
                "    join characters c on c.id = f.winner_id\n" +
                "    where current_date - make_interval(0,0,0,7 + extract(day from date_of_fight)::integer % 7) <= f.date_of_fight\n" +
                "      and f.date_of_fight  <= current_date - make_interval(0,0,0,extract(day from date_of_fight)::integer % 7)\n" +
                "    group by (c.account_id, hour)\n" +
                "    order by account_id, count(*) desc\n" +
                ")\n" +
                "select\n" +
                "    tmp.account_id,\n" +
                "    tmp.kills,\n" +
                "    tmp.best_hour,\n" +
                "    tmp.best_day,\n" +
                "    (n < overall(0)/10) as percentil90\n" +
                "from (\n" +
                "select\n" +
                "    c.account_id,\n" +
                "    count(*) as kills,\n" +
                "    bd.day as best_day,\n" +
                "    bh.hour as best_hour,\n" +
                "    nextval('percentil') as n\n" +
                "    from fights f\n" +
                "    join characters c on f.winner_id = c.id\n" +
                "    join best_day bd on c.account_id = bd.account_id\n" +
                "    join best_hour bh on c.account_id = bh.account_id\n" +
                "      where current_date - make_interval(0,0,0,7 + extract(day from date_of_fight)::integer % 7) <= f.date_of_fight\n" +
                "      and f.date_of_fight  <= current_date - make_interval(0,0,0,extract(day from date_of_fight)::integer % 7)\n" +
                "    group by (c.account_id, bd.day, bh.hour)\n" +
                "order by count(*) desc ) as tmp\n" +
                "limit ?;";
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(sql)) {
            s.setInt(1, x);
            try (ResultSet r = s.executeQuery()) {

                List<PlayerExhaustion> elements = new ArrayList<>();

                while (r.next()) {
                    elements.add(load(r));
                }
                try (PreparedStatement drop = DbContext.getConnection().prepareStatement("drop function overall(dummy integer);\n" +
                        "drop sequence percentil;")) {
                    drop.executeUpdate();
                }
                return elements;
            }
        }
    }

    private PlayerExhaustion load(ResultSet r) throws SQLException {
        PlayerExhaustion p = new PlayerExhaustion();
        p.setAccountId(r.getInt("account_id"));
        p.setKills(r.getInt("kills"));
        p.setBestDay(r.getInt("best_day"));
        p.setBestHour(r.getInt("best_hour"));
        p.setPercentil90(r.getBoolean("percentil90"));
        return p;
    }
}
