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
public class TransactionHistory {
    private int id;
    private int credits;
    private String description;
    private int serviceId;
    private int accountId;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getServiceId() { return serviceId; }
    public void setServiceId(int service_id) { this.serviceId = service_id; }
    public int getAccountId() { return accountId; }
    public void setAccountId(int account_id) { this.accountId = account_id; }

    public void insert() throws SQLException {
        String sql = "INSERT INTO transaction_history (credits, description, service_id, account_id) " +
                "VALUES (?,?,?,?)";
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            s.setInt(1, credits);
            s.setString(2, description);
            s.setInt(3, serviceId);
            s.setInt(4, accountId);

            s.executeUpdate();

            try (ResultSet r = s.getGeneratedKeys()) {
                r.next();
                id = r.getInt(1);
            }
        }
    }
}
