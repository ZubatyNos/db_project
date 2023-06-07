package rdg;

import main.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author Rastislav Urbanek
 */
public class EffectiveAttributesFinder {
    private static final EffectiveAttributesFinder INSTANCE = new EffectiveAttributesFinder();

    public static EffectiveAttributesFinder getInstance() { return INSTANCE; }
    private EffectiveAttributesFinder() { }

    public EffectiveAttributes findById(int id) throws SQLException {
        String sql = "SELECT\n" +
                "        cl.max_health_per_level * level + coalesce(tmp.hp, 0) as max_hp,\n" +
                "        cl.defense_per_level * level + coalesce(tmp.def, 0) as def,\n" +
                "        cl.power_per_level * level + coalesce(tmp.pow, 0) as pow\n" +
                "FROM\n" +
                "       (select\n" +
                "            level as level,\n" +
                "            class_id,\n" +
                "            sum(max_health_modifier)::integer as hp,\n" +
                "            sum(defense_modifier)::integer as def,\n" +
                "            sum(power_modifier)::integer as pow\n" +
                "            from characters c\n" +
                "                     left join character_items c_i on c.id = c_i.character_id and c_i.equipped = true\n" +
                "                     left join items i on i.id = c_i.item_id\n" +
                "            where c.id = ?\n" +
                "            group by c.id, level, class_id\n" +
                "       ) as tmp\n" +
                "JOIN classes cl on tmp.class_id = cl.id";

        try (PreparedStatement s = DbContext.getConnection().prepareStatement(sql)) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    EffectiveAttributes a = load(r);

                    return a;
                } else {
                    return null;
                }
            }
        }
    }

    public EffectiveAttributes load(ResultSet r) throws SQLException {
        EffectiveAttributes a = new EffectiveAttributes();
        a.setMaxHealth(r.getInt("max_hp"));
        a.setDefense(r.getInt("def"));
        a.setPower(r.getInt("pow"));
        return a;
    }

}
