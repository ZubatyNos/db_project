package rdg;

import main.DbContext;
import ts.MyException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Rastislav Urbanek
 */
public class GameCharacter {

    private int id;
    private String name;
    private int level;
    private int experience;
    private int currentHealth;
    private String gender;
    private int hairType;
    private int bodyType;
    private int characterClassId;
    private int raceId;
    private int accountId;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getLevel() { return level; }
    public void setLevel(int level) throws MyException {
        if (level > 100)
            throw new MyException("Level too high");
        this.level = level;
    }
    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }
    public int getCurrentHealth() { return currentHealth; }
    public void setCurrentHealth(int currentHealth) { this.currentHealth = currentHealth; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public int getHairType() { return hairType; }
    public void setHairType(int hairType) { this.hairType = hairType; }
    public int getBodyType() { return bodyType; }
    public void setBodyType(int bodyType) { this.bodyType = bodyType; }
    public int getCharacterClassId() { return characterClassId; }
    public void setCharacterClassId(int characterClassId) { this.characterClassId = characterClassId; }
    public int getRaceId() { return raceId; }
    public void setRaceId(int raceId) { this.raceId = raceId; }
    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public static int calculateExp(int level) {
        return level * 100 + (level / 10) * 500 + (level / 25) * 5000;
    }

    public void insert() throws SQLException {
        String sql = "INSERT INTO characters (name, level, experience, current_health, gender, hair_type, body_type, class_id, race_id, account_id) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            loadPreparedStatement(s);

            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }

    public void update() throws SQLException {
        if (name == null || gender == null) {
            throw new IllegalStateException("a value is null");
        }
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(
                "UPDATE characters SET" +
                        " name = ?, level = ?, experience = ?, current_health = ?, gender = ?, hair_type = ?, body_type = ?, class_id = ?, race_id = ?, account_id = ? WHERE id = ?")) {
            loadPreparedStatement(s);
            s.setInt(11, id);
            s.executeUpdate();
        }
    }

    public void delete() throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM characters WHERE id = ?")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
    }

    public void loadPreparedStatement(PreparedStatement s) throws SQLException {
        s.setString(1, name);
        s.setInt(2, level);
        s.setInt(3, experience);
        s.setInt(4, currentHealth);
        s.setString(5, gender);
        s.setInt(6, hairType);
        s.setInt(7, bodyType);
        s.setInt(8, characterClassId);
        s.setInt(9, raceId);
        s.setInt(10, accountId);
    }

}
