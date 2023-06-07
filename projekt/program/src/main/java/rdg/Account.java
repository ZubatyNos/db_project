
package rdg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import main.DbContext;

/**
 *
 * @author Rastislav Urbanek
 */
public class Account {

    private int id;
    private String username;
    private String mail;
    private String password;
    private int credits;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getMail() {
        return mail;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getCredits() {
        return credits;
    }
    public void setCredits(int credits) {
        this.credits = credits;
    }



    public void insert() throws SQLException {
        String sql = "INSERT INTO accounts (username, mail, password, credits) " +
                "VALUES (?,?,?,?)";
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            s.setString(1, username);
            s.setString(2, mail);
            s.setString(3, password);
            s.setInt(4, credits);

            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }

    public void update() throws SQLException {
        if (username == null || mail == null || password == null) {
            throw new IllegalStateException("A value is null");
        }

        try (PreparedStatement s = DbContext.getConnection().prepareStatement(
                "UPDATE accounts SET username = ?, mail = ?, password = ?, credits = ? WHERE id = ?")) {
            s.setString(1, username);
            s.setString(2, mail);
            s.setString(3, password);
            s.setInt(4, credits);
            s.setInt(5, id);

            s.executeUpdate();
        }
    }

    public void delete() throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("DELETE FROM accounts WHERE id = ?")) {
            s.setInt(1, id);

            s.executeUpdate();
        }
    }

}
