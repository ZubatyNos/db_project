package rdg;

import main.DbContext;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CharacterItem {
    private int itemId;
    private int gameCharacterId;
    private boolean equipped;

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }
    public int getCharacterId() { return gameCharacterId; }
    public void setCharacterId(int gameCharacterId) { this.gameCharacterId = gameCharacterId; }
    public boolean isEquipped() { return equipped; }
    public void setEquipped(boolean equipped) { this.equipped = equipped; }

    public void insert() throws SQLException {
        String sql = "INSERT INTO character_items (character_id, item_id, equipped) " +
                "VALUES (?,?,?)";
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(sql)) {
            s.setInt(1, gameCharacterId);
            s.setInt(2, itemId);
            s.setBoolean(3, equipped);

            s.executeUpdate();
        }
    }
}
