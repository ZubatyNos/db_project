package ui;


import rdg.PlayerExhaustion;

public class PlayerExhaustionPrinter {
    private static final PlayerExhaustionPrinter INSTANCE = new PlayerExhaustionPrinter();

    public static PlayerExhaustionPrinter getInstance() { return INSTANCE; }

    private PlayerExhaustionPrinter() { }

    public void printTableHeaders() {
        System.out.println("*".repeat(68));
        System.out.printf("* %-10s", "account_id");
        System.out.printf(" * %-10s", "kills");
        System.out.printf(" * %-10s", "best_day");
        System.out.printf(" * %-10s", "best_hour");
        System.out.printf(" * %-12s *%n", "percentil90");
        System.out.println("*".repeat(68));

    }

    public void print(PlayerExhaustion p) {
        if (p == null) {
            throw new NullPointerException("Stat cannot be null");
        }
        System.out.printf("* %-10s", p.getAccountId());
        System.out.printf(" * %-10s", p.getKills());
        System.out.printf(" * %-10s", p.getBestDay());
        System.out.printf(" * %-10s", p.getBestHour());
        System.out.printf(" * %-12s *%n", p.isPercentil90());
    }
}
