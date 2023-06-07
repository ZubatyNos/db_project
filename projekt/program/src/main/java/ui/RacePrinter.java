package ui;


import rdg.Account;
import rdg.Race;

/**
 *
 * @author Rastislav Urbanek
 */
public class RacePrinter {

    private static final RacePrinter INSTANCE = new RacePrinter();

    public static RacePrinter getInstance() { return INSTANCE; }

    private RacePrinter() { }

    public void printTableHeaders() {
        System.out.println("*".repeat(37));
        System.out.printf("* %-10s", "id");
        System.out.printf(" * %-20s *%n", "name");
        System.out.println("*".repeat(37));

    }

    public void print(Race race) {
        if (race == null) {
            throw new NullPointerException("race cannot be null");
        }
        System.out.printf("* %-10s", race.getId());
        System.out.printf(" * %-20s *%n", race.getName());
    }
}
