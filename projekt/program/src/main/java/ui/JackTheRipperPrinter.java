package ui;

import rdg.JackTheRipper;

/**
 *
 * @author Rastislav Urbanek
 */
public class JackTheRipperPrinter {

    private static final JackTheRipperPrinter INSTANCE = new JackTheRipperPrinter();

    public static JackTheRipperPrinter getInstance() { return INSTANCE; }

    private JackTheRipperPrinter() { }

    public void printTableHeaders() {
        System.out.println("*".repeat(101));
        System.out.printf("* %-12s", "winner_id");
        System.out.printf(" * %-20s", "all_defeated");
        System.out.printf(" * %-20s", "female_defeated");
        System.out.printf(" * %-20s", "male_defeated");
        System.out.printf(" * %-5s", "week");
        System.out.printf(" * %-5s *%n", "month");
        System.out.println("*".repeat(101));
    }

    public void print(JackTheRipper jackTheRipper) {
        System.out.printf("* %-12s", jackTheRipper.getWinnerId());
        System.out.printf(" * %-20s", jackTheRipper.getAllDefeated());
        System.out.printf(" * %-20s", jackTheRipper.getFemaleDefeated());
        System.out.printf(" * %-20s", jackTheRipper.getMaleDefeated());
        System.out.printf(" * %-5s", jackTheRipper.getWeek());
        System.out.printf(" * %-5s *%n", jackTheRipper.getMonth());
    }
}
