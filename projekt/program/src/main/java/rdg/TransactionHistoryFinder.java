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
public class TransactionHistoryFinder {

    private static final TransactionHistoryFinder INSTANCE = new TransactionHistoryFinder();

    public static TransactionHistoryFinder getInstance() { return INSTANCE; }
    private TransactionHistoryFinder() { }

    public List<TransactionHistory> findAllByAccountId(int accountId) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement(
                "SELECT * FROM transaction_history WHERE account_id = ?")) {
            s.setInt(1, accountId);

            try (ResultSet r = s.executeQuery()) {
                List<TransactionHistory> elements = new ArrayList<>();

                while (r.next()) elements.add(load(r));

                return elements;
            }
        }
    }

    public TransactionHistory load(ResultSet r) throws SQLException {
        TransactionHistory th = new TransactionHistory();
        th.setId(r.getInt("id"));
        th.setCredits(r.getInt("credits"));
        th.setDescription(r.getString("description"));
        th.setAccountId(r.getInt("account_id"));
        th.setServiceId(r.getInt("service_id"));
        return th;
    }
}
