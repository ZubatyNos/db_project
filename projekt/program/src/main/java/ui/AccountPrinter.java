package ui;

import rdg.Account;

/**
 *
 * @author Rastislav Urbanek
 */
public class AccountPrinter {

    private static final AccountPrinter INSTANCE = new AccountPrinter();

    public static AccountPrinter getInstance() { return INSTANCE; }

    private AccountPrinter() { }

    public void printTableHeaders() {
        System.out.println("*".repeat(106));
        System.out.printf("* %-10s", "id");
        System.out.printf(" * %-20s", "username");
        System.out.printf(" * %-20s", "mail");
        System.out.printf(" * %-20s", "password");
        System.out.printf(" * %-20s *%n", "credits");
        System.out.println("*".repeat(106));

    }

    public void print(Account account) {
        if (account == null) {
            throw new NullPointerException("account cannot be null");
        }
        System.out.printf("* %-10s", account.getId());
        System.out.printf(" * %-20s", account.getUsername());
        System.out.printf(" * %-20s", account.getMail());
        System.out.printf(" * %-20s", account.getPassword());
        System.out.printf(" * %-20s *%n", account.getCredits());
    }
}
