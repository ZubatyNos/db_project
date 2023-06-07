package ui;

import rdg.GameCharacter;

/**
 *
 * @author Rastislav Urbanek
 */
public class GameCharacterPrinter {

    private static final GameCharacterPrinter INSTANCE = new GameCharacterPrinter();

    public static GameCharacterPrinter getInstance() {
        return INSTANCE;
    }

    private GameCharacterPrinter() {
    }

    public void printTableHeaders() {
        System.out.println("*".repeat(127));
        System.out.printf("* %-8s", "id");
        System.out.printf(" * %-20s", "name");
        System.out.printf(" * %-3s", "lvl");
        System.out.printf(" * %-6s", "exp");
        System.out.printf(" * %-6s", "hp");
        System.out.printf(" * %-6s", "gender");
        System.out.printf(" * %-9s", "hair_type");
        System.out.printf(" * %-9s", "body_type");
        System.out.printf(" * %-8s", "class_id");
        System.out.printf(" * %-8s", "race_id");
        System.out.printf(" * %-10s *%n", "account_id");
        System.out.println("*".repeat(127));

    }

    public void print(GameCharacter gameCharacter) {
        if (gameCharacter == null) {
            throw new NullPointerException("gameCharacter cannot be null");
        }
        System.out.printf("* %-8s", gameCharacter.getId());
        System.out.printf(" * %-20s", gameCharacter.getName());
        System.out.printf(" * %-3s", gameCharacter.getLevel());
        System.out.printf(" * %-6s", gameCharacter.getExperience());
        System.out.printf(" * %-6s", gameCharacter.getCurrentHealth());
        System.out.printf(" * %-6s", gameCharacter.getGender());
        System.out.printf(" * %-9s", gameCharacter.getHairType());
        System.out.printf(" * %-9s", gameCharacter.getBodyType());
        System.out.printf(" * %-8s", gameCharacter.getCharacterClassId());
        System.out.printf(" * %-8s", gameCharacter.getRaceId());
        System.out.printf(" * %-10s *%n", gameCharacter.getAccountId());

    }
}