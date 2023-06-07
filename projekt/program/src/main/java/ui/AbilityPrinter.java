package ui;

import rdg.Ability;

public class AbilityPrinter {
    private static final AbilityPrinter INSTANCE = new AbilityPrinter();

    public static AbilityPrinter getInstance() { return INSTANCE; }

    private AbilityPrinter() { }

    public void printTableHeaders() {
        System.out.println("*".repeat(63));
        System.out.printf("* %-10s", "id");
        System.out.printf(" * %-15s", "name");
        System.out.printf(" * %-15s", "power_factor");
        System.out.printf(" * %-10s *%n", "healing");
        System.out.println("*".repeat(63));

    }

    public void print(Ability ability) {
        if (ability == null) {
            throw new NullPointerException("ability cannot be null");
        }
        System.out.printf("* %-10s", ability.getId());
        System.out.printf(" * %-15s", ability.getName());
        System.out.printf(" * %-15s", ability.getPowerFactor());
        System.out.printf(" * %-10s *%n", ability.isHealing());
    }
}
