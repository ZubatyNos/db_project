package ui;

import rdg.Account;
import rdg.CharacterClass;

public class CharacterClassPrinter {
    private static final CharacterClassPrinter INSTANCE = new CharacterClassPrinter();

    public static CharacterClassPrinter getInstance() { return INSTANCE; }

    private CharacterClassPrinter() { }

    public void printTableHeaders() {
        System.out.println("*".repeat(78));
        System.out.printf("* %-10s", "id");
        System.out.printf(" * %-10s", "name");
        System.out.printf(" * %-14s", "max_hp_per_lvl");
        System.out.printf(" * %-14s", "def_per_lvl");
        System.out.printf(" * %-14s *%n", "power_per_lvl");
        System.out.println("*".repeat(78));

    }

    public void print(CharacterClass characterClass) {
        if (characterClass == null) {
            throw new NullPointerException("account cannot be null");
        }
        System.out.printf("* %-10s", characterClass.getId());
        System.out.printf(" * %-10s", characterClass.getName());
        System.out.printf(" * %-14s", characterClass.getMaxHealthPerLevel());
        System.out.printf(" * %-14s", characterClass.getDefensePerLevel());
        System.out.printf(" * %-14s *%n", characterClass.getPowerPerLevel());
    }
}
