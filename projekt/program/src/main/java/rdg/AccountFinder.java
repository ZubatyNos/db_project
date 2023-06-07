package rdg;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.DbContext;

/**
 *
 * @author Rastislav Urbanek
 */
public class AccountFinder {

    private static final AccountFinder INSTANCE = new AccountFinder();

    public static AccountFinder getInstance() { return INSTANCE; }
    private AccountFinder() { }

    public Account findById(int id) throws SQLException {

        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM accounts WHERE id = ?")) {
            s.setInt(1, id);

            try (ResultSet r = s.executeQuery()) {
                if (r.next()) {
                    Account a = load(r);

                    return a;
                } else {
                    return null;
                }
            }
        }
    }

    public List<Account> findAllOnPage(int n) throws SQLException {
        try (PreparedStatement s = DbContext.getConnection().prepareStatement("SELECT * FROM accounts OFFSET ? * ? LIMIT ?")) {
            s.setInt(1, n);
            int entriesPerPage = 50;
            s.setInt(2, entriesPerPage);
            s.setInt(3, entriesPerPage);
            try (ResultSet r = s.executeQuery()) {
                List<Account> elements = new ArrayList<>();

                while (r.next()) elements.add(load(r));

                return elements;
            }
        }
    }

    public Account load(ResultSet r) throws SQLException {
        Account a = new Account();
        a.setId(r.getInt("id"));
        a.setUsername(r.getString("username"));
        a.setMail(r.getString("mail"));
        a.setPassword(r.getString("password"));
        a.setCredits(r.getInt("credits"));
        return a;
    }

}
