package ui;

import rdg.Account;
import rdg.TransactionHistory;
/**
 *
 * @author Rastislav Urbanek
 */
public class TransactionHistoryPrinter {

    private static final TransactionHistoryPrinter INSTANCE = new TransactionHistoryPrinter();

    public static TransactionHistoryPrinter getInstance() { return INSTANCE; }

    private TransactionHistoryPrinter() { }

    public void printTableHeaders() {
        System.out.println("*".repeat(112));
        System.out.printf("* %-10s", "id");
        System.out.printf("* %-20s", "credits");
        System.out.printf("* %-30s", "description");
        System.out.printf("* %-20s", "service_id");
        System.out.printf("* %-20s *%n", "account_id");
        System.out.println("*".repeat(112));
    }

    public void print(TransactionHistory th) {
        if (th == null) {
            throw new NullPointerException("account cannot be null");
        }
        System.out.printf("* %-10s", th.getId());
        System.out.printf("* %-20s", th.getCredits());
        System.out.printf("* %-30s", th.getDescription());
        System.out.printf("* %-20s", th.getServiceId());
        System.out.printf("* %-20s *%n", th.getAccountId());
    }
}
