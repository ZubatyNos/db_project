package ui;

import rdg.*;
import ts.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Rastislav Urbanek
 */
public class MainMenu extends Menu {
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    @Override
    public void print() {
        System.out.println("***************************************");
        System.out.println("* ACCOUNTS                            *");
        System.out.println("***************************************");
        System.out.println("* A1.  show   all accounts            *");
        System.out.println("* A2.  show   account's transactions  *");
        System.out.println("* A3.  show   an account              *");
        System.out.println("* A4.  add    an account              *");
        System.out.println("* A5.  update an account              *");
        System.out.println("* A6.  delete an account              *");
        System.out.println("***************************************");
        System.out.println("* CHARACTERS                          *");
        System.out.println("***************************************");
        System.out.println("* C1.  show   all characters          *");
        System.out.println("* C2.  show   a character             *");
        System.out.println("* C3.  gift item to a character       *");
        System.out.println("* C4.  add    a character             *");
        System.out.println("* C5.  update a character             *");
        System.out.println("* C6.  delete a character             *");
        System.out.println("***************************************");
        System.out.println("* RACES                               *");
        System.out.println("***************************************");
        System.out.println("* R1.  show   all races               *");
        System.out.println("* R2.  add    a race                  *");
        System.out.println("* R3.  add a class to a race          *");
        System.out.println("* R4.  delete a class of a race       *");
        System.out.println("* R5.  delete a race                  *");
        System.out.println("***************************************");
        System.out.println("* CLASSES                             *");
        System.out.println("***************************************");
        System.out.println("* CL1. show all classes               *");
        System.out.println("* CL2. show a class                   *");
        System.out.println("* CL3. add a class                    *");
        System.out.println("* CL4. update a class                 *");
        System.out.println("* CL5. delete a class                 *");
        System.out.println("***************************************");
        System.out.println("*          COMPLEX DOMAIN OPS         *");
        System.out.println("***************************************");
        System.out.println("* CDO1. attack or heal a character    *");
        System.out.println("* CDO2. metamorphosis                 *");
        System.out.println("* CDO3. transfer character            *");
        System.out.println("* CDO4. change appearance             *");
        System.out.println("* CDO5. reward bounty hunter          *");
        System.out.println("***************************************");
        System.out.println("*             STATISTICS              *");
        System.out.println("***************************************");
        System.out.println("* S1. show exhaustion stats           *");
        System.out.println("* S2. show jack the ripper stats      *");
        System.out.println("***************************************");
        System.out.println("*          AUXILIARY FUNCTIONS        *");
        System.out.println("***************************************");
        System.out.println("* H1.   show character's stats        *");
        System.out.println("***************************************");
        System.out.println("* E. EXIT                             *");
        System.out.println("***************************************");
    }

    @Override
    public void handle(String option) {
        try {
            switch (option) {
                case "A1"  :   listAccounts(); break;
                case "A2"  :   showAccountTransactions(); break;
                case "A3"  :   showAccount(); break;
                case "A4"  :   addAccount(); break;
                case "A5"  :   updateAccount(); break;
                case "A6"  :   deleteAccount(); break;

                case "C1"  :   showGameCharacters(); break;
                case "C2"  :   showGameCharacter(); break;
                case "C3"  :   giftItemToGameCharacter(); break;
                case "C4"  :   addGameCharacter(); break;
                case "C5"  :   updateGameCharacter(); break;
                case "C6"  :   deleteGameCharacter(); break;

                case "R1"  :   showAllRaces(); break;
                case "R2"  :   addRace(); break;
                case "R3"  :   addClassToRace(); break;
                case "R4"  :   removeClassOfRace(); break;
                case "R5"  :   deleteRace(); break;

                case "CL1" : showAllCharacterClasses(); break;
                case "CL2" : showCharacterClass(); break;
                case "CL3" : addCharacterClass(); break;
                case "CL4" : updateCharacterClass(); break;
                case "CL5" : deleteCharacterClass(); break;


                case "CDO1":  simulateFight(); break;
                case "CDO2":   metamorphosis(); break;
                case "CDO3":   transferGameCharacter(); break;
                case "CDO4":   changeAppearance(); break;
                case "CDO5":  rewardBountyHunter(); break;

                case "S1"  :   exhaustionStats();   break;
                case "S2"  :   jackTheRipperStats(); break;

                case "H1"  : showGameCharacterStats(); break;

                case "E"   :   exit(); break;
                case "p"   :   print(); break;
                default:    {
                    System.out.println("Unknown option... type \"p\" to print commands");
                } break;
            }
        } catch (SQLException | IOException | MyException | NumberFormatException | AccountServiceException | MetamorphosisException | FightingException e) {
            System.out.println("error: " + e.getMessage());
        }
    }


    /**
     * COMPLEX OPERATION
     * simulates isolated attack or heal between two characters queried by the user
     * @throws IOException
     * @throws MyException
     * @throws SQLException
     * @throws FightingException
     */
    private void simulateFight() throws IOException, MyException, SQLException, FightingException {
        System.out.println("Enter 1st character id");
        int gameCharacterId1 = Integer.parseInt(br.readLine());
        System.out.println("Enter 2nd character id");
        int gameCharacterId2 = Integer.parseInt(br.readLine());
        if (gameCharacterId1 == gameCharacterId2)
            throw new MyException("You can't fight yourself");
        System.out.println("!!!START OF FIGHT!!!");
        System.out.println(FightingArena.getInstance().fighting(gameCharacterId1, gameCharacterId2));
        System.out.println("!!!END OF FIGHT!!!");
    }

    /**
     * COMPLEX OPERATION
     * at the end of month
     * rewards the account of the character that has defeated the most characters in the end of the current month
     * also rewards the top 3 accounts (with a descending prize) whose characters has defeated the top character the most times
     * @throws MyException
     * @throws SQLException
     * @throws AccountServiceException
     */
    private void rewardBountyHunter() throws MyException, SQLException, AccountServiceException {
        if (true) { //Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
            AccountService.getInstance().rewardingBountyHunter();
            System.out.println("Bounty hunters were rewarded");
        } else {
            throw new MyException("Not the end of month");
        }
    }

    /**
     * helper function to quickly see what stats the character has
     * @throws IOException
     * @throws MyException
     * @throws SQLException
     */
    private void showGameCharacterStats() throws IOException, MyException, SQLException {
        System.out.println("Enter character id:");
        GameCharacter gameCharacter = GameCharacterFinder.getInstance().findById(Integer.parseInt(br.readLine()));
        if (gameCharacter == null) {
            throw new MyException("No such character exists");
        } else {
            EffectiveAttributes stats = EffectiveAttributesFinder.getInstance().findById(gameCharacter.getId());
            System.out.println("max_hp=" + stats.getMaxHealth() + " pow=" + stats.getPower() + " def=" + stats.getDefense());
        }
    }


    /**
     * COMPLEX OPERATION
     * morphs two characters of the same account queried by the user
     * makes a randomized lvl 1 character that has the power stat about the same as the sum of the two morphing characters
     * @throws SQLException
     * @throws MyException
     * @throws IOException
     * @throws MetamorphosisException
     */
    private void metamorphosis() throws SQLException, MyException, IOException, MetamorphosisException {
        System.out.println("Enter account id:");
        int accountId = Integer.parseInt(br.readLine());
        System.out.println("Enter 1st id of character you want to morph:");
        int gameCharacterId1 = Integer.parseInt(br.readLine());
        System.out.println("Enter 2nd id of character you want to morph:");
        int gameCharacterId2 = Integer.parseInt(br.readLine());
        Metamorphosis.getInstance().metaMorphing(accountId, gameCharacterId1, gameCharacterId2);
        System.out.println("Morphing was successful");
    }

    /**
     * STATISTIC OPERATION
     * sorts X players by the kills acquired in the last week by their characters
     * adds the (killing) day and hour to the player and also whether they are in the 90th percentile
     * @throws SQLException
     */
    private void exhaustionStats() throws SQLException {
        System.out.println("*** EXHAUSTION STATS ***");
        PlayerExhaustionPrinter.getInstance().printTableHeaders();
        for (PlayerExhaustion p : PlayerExhaustionFinder.getInstance().findFirstX(50)) {
            PlayerExhaustionPrinter.getInstance().print(p);
        }
    }

    /**
     * COMPLEX OPERATION
     * changes character's account to new given by the user
     * substracts service fee + fee given by the user (negotiated price) from the new account
     * and adds the "negotiated price" to the old account
     * @throws MyException
     * @throws SQLException
     * @throws IOException
     * @throws AccountServiceException
     */
    private void transferGameCharacter() throws MyException, SQLException, IOException, AccountServiceException {
        System.out.println("Enter id of character you want to transfer: ");
        int characterId = Integer.parseInt(br.readLine());
        System.out.println("Enter new account id of character: ");
        int newAccountId = Integer.parseInt(br.readLine());
        System.out.println("Enter how much credits you want for the character: ");
        int negotiatedPrice = Integer.parseInt(br.readLine());
        AccountService.getInstance().transferringGameCharacter(characterId, newAccountId, negotiatedPrice);
        System.out.println("Character transferred successfully");
    }

    /**
     * updates character's parameters to new given by the user
     * @throws IOException
     * @throws SQLException
     * @throws MyException
     */
    private void updateGameCharacter() throws IOException, SQLException, MyException {
        System.out.println("Enter character id: ");

        int characterId = Integer.parseInt(br.readLine());

        GameCharacter gameCharacter = GameCharacterFinder.getInstance().findById(characterId);
        if (gameCharacter == null) {
            System.out.println("No such character exists");
        } else {
            System.out.println("Enter new level: ");
            int level = Integer.parseInt(br.readLine());
            gameCharacter.setLevel(level);
            gameCharacter.setExperience(GameCharacter.calculateExp(level));
            gameCharacter.update();

            System.out.println("Enter new hp: ");
            int hp = Integer.parseInt(br.readLine());
            if (EffectiveAttributesFinder.getInstance().findById(characterId).getMaxHealth() < hp)
                throw new MyException("hp exceeded max hp");
            else {
                gameCharacter.setCurrentHealth(hp);
                gameCharacter.update();
            }

        }
    }

    /**
     * adds item queried by the user to the character queried by the user
     * @throws IOException
     * @throws SQLException
     * @throws MyException
     */
    private void giftItemToGameCharacter() throws IOException, SQLException, MyException {
        System.out.println("Enter character id: ");
        CharacterItem characterItem = new CharacterItem();
        int gameCharacterId = Integer.parseInt(br.readLine());
        GameCharacter gameCharacter = GameCharacterFinder.getInstance().findById(gameCharacterId);

        if (gameCharacter == null) {
            throw new MyException("No such character exists");
        } else {
            System.out.println("Choose item out of 60 items to give to character (type in id): ");
            int itemId = Integer.parseInt(br.readLine());
            Item item = ItemFinder.getInstance().findById(itemId);

            if (item == null) {
                throw new MyException("No such item exists");
            } else {
                characterItem.setCharacterId(gameCharacterId);
                characterItem.setItemId(itemId);
                System.out.println("If you want to equip the item type in \"y\": ");
                boolean equipped = br.readLine().equals("y");
                characterItem.setEquipped(equipped);
                characterItem.insert();

                if (equipped) {
                    gameCharacter.setCurrentHealth(
                            gameCharacter.getCurrentHealth() + item.getMaxHealthModifier()
                    );
                    gameCharacter.update();
                }
                System.out.println("Item successfully added to character");
            }
        }
    }

    /**
     * adds characters with parameteres given by the user
     * @throws IOException
     * @throws SQLException
     * @throws MyException
     */
    private void addGameCharacter() throws IOException, SQLException, MyException {
        GameCharacter gameCharacter = new GameCharacter();

        System.out.println("Enter account id: ");
        int accountId = Integer.parseInt(br.readLine());
        Account account = AccountFinder.getInstance().findById(accountId);
        if (account == null) {
            throw new MyException("No such account exists");
        } else {
            gameCharacter.setAccountId(accountId);

            System.out.println("Enter name:");
            gameCharacter.setName(br.readLine());
            System.out.println("Enter gender (\"M\" = male, \"F\"= female, anything else is it's own gender):");
            gameCharacter.setGender(br.readLine());
            System.out.println("Enter hair type (number): ");
            gameCharacter.setHairType(Integer.parseInt(br.readLine()));
            System.out.println("Enter body type (number): ");
            gameCharacter.setBodyType(Integer.parseInt(br.readLine()));

            System.out.println("Enter race id: ");
            int raceId = Integer.parseInt(br.readLine());
            Race race = RaceFinder.getInstance().findById(raceId);
            if (race == null) {
                throw new MyException("Nonexisting race, type in existing race id: ");
            } else {
                gameCharacter.setRaceId(raceId);

                System.out.println("Enter character's class of race id: ");
                int characterClassId = Integer.parseInt(br.readLine());

                if (CharacterClassFinder.getInstance().findByRaceId(raceId).stream().allMatch(e -> e.getId() != characterClassId)) {
                    throw new MyException("Character class not of race or class not existing");
                } else {
                    gameCharacter.setCharacterClassId(characterClassId);

                    gameCharacter.setLevel(1);
                    gameCharacter.setExperience(GameCharacter.calculateExp(1));
                    gameCharacter.setCurrentHealth(1);
                    gameCharacter.insert();

                    gameCharacter.setCurrentHealth(
                            EffectiveAttributesFinder.getInstance().findById(gameCharacter.getId()).getMaxHealth()
                    );
                    gameCharacter.update();

                    System.out.println("The character has been successfully added");
                    System.out.print("The character's id is: ");
                    System.out.println(gameCharacter.getId());
                }
            }
        }
    }

    /**
     * changes class' parameters and also asks in a loop whether the user wants to add an ability to the class
     * @throws SQLException
     * @throws IOException
     * @throws MyException
     */
    private void updateCharacterClass() throws SQLException, IOException, MyException {
        System.out.println("Enter a character class' id:");
        int characterClassId = Integer.parseInt(br.readLine());

        CharacterClass characterClass = CharacterClassFinder.getInstance().findById(characterClassId);

        if (characterClass == null) {
            throw new MyException("No such character class exists");
        } else {
            System.out.println("Enter name:");
            characterClass.setName(br.readLine());
            System.out.println("Enter max health per level:");
            characterClass.setMaxHealthPerLevel(Integer.parseInt(br.readLine()));
            System.out.println("Enter defense per level:");
            characterClass.setDefensePerLevel(Integer.parseInt(br.readLine()));
            System.out.println("Enter power per level:");
            characterClass.setPowerPerLevel(Integer.parseInt(br.readLine()));

            characterClass.update();

            System.out.println("if you want to add or delete any of the character class' abilities then type in it's \"id\" if not type \"n\":");
            String answer = br.readLine();
            while (!answer.equals("n")) {
                System.out.println("Do you want to add or delete? type in a/d");
                String resolve = br.readLine();
                CharacterClassAbility characterClassAbility = new CharacterClassAbility();
                characterClassAbility.setClassId(characterClassId);
                characterClassAbility.setAbilityId(Integer.parseInt(answer));
                if (resolve.equals("a")) {
                    try { characterClassAbility.insert(); } catch (SQLException e) {}
                    System.out.println("ability with id of " + answer + " ");
                } else if (resolve.equals("d")) {
                    characterClassAbility.delete();
                } else {
                    break;
                }
                System.out.println("if you want to change any of the character class' abilities then type in it's \"id\" if not type \"n\":");
                answer = br.readLine();
            }

            System.out.println("The character class has been successfully updated");
        }
    }

    /**
     * adds class with parameters given by the user
     * @throws IOException
     * @throws SQLException
     */
    private void addCharacterClass() throws IOException, SQLException {
        CharacterClass characterClass = new CharacterClass();

        System.out.println("Enter name:");
        characterClass.setName(br.readLine());
        System.out.println("Enter max health per level:");
        characterClass.setMaxHealthPerLevel(Integer.parseInt(br.readLine()));
        System.out.println("Enter defense per level:");
        characterClass.setDefensePerLevel(Integer.parseInt(br.readLine()));
        System.out.println("Enter power per level:");
        characterClass.setPowerPerLevel(Integer.parseInt(br.readLine()));

        characterClass.insert();

        System.out.println("The character class has been successfully added");
        System.out.print("The character class' id is: ");
        System.out.println(characterClass.getId());
    }

    /**
     * shows class queried by the user
     * @throws IOException
     * @throws SQLException
     * @throws MyException
     */
    private void showCharacterClass() throws IOException, SQLException, MyException {
        System.out.println("Enter class id:");
        int characterClassId = Integer.parseInt(br.readLine());

        CharacterClass characterClass = CharacterClassFinder.getInstance().findById(characterClassId);

        if (characterClass == null) {
            throw new MyException("No such class exists");
        } else {
            CharacterClassPrinter.getInstance().printTableHeaders();
            CharacterClassPrinter.getInstance().print(characterClass);
            System.out.println();
            System.out.println("And the class' abilities");
            System.out.println();
            AbilityPrinter.getInstance().printTableHeaders();
            for (Ability ability : AbilityFinder.getInstance().findByCharacterClassId(characterClassId)) {
                AbilityPrinter.getInstance().print(ability);
            }
        }
    }

    /**
     * deletes class queried by the user
     * @throws IOException
     * @throws SQLException
     * @throws MyException
     */
    private void deleteCharacterClass() throws IOException, SQLException, MyException {
        System.out.println("Enter class id: ");
        int characterClassId = Integer.parseInt(br.readLine());

        CharacterClass characterClass = CharacterClassFinder.getInstance().findById(characterClassId);

        if (characterClass == null) {
            throw new MyException("No such class exists");
        } else {
            characterClass.delete();
            System.out.println("The class has been successfully deleted");
        }
    }

    /**
     * shows all classes
     * @throws SQLException
     */
    private void showAllCharacterClasses() throws SQLException {
        CharacterClassPrinter.getInstance().printTableHeaders();
        for (CharacterClass characterClass : CharacterClassFinder.getInstance().findAll()) {
            CharacterClassPrinter.getInstance().print(characterClass);
        }
    }

    /**
     * removes class queried by the user from the race queried by the user
     * @throws IOException
     * @throws SQLException
     * @throws MyException
     */
    private void removeClassOfRace() throws IOException, SQLException, MyException {
        System.out.println("Enter race id: ");
        int raceId = Integer.parseInt(br.readLine());
        Race race = RaceFinder.getInstance().findById(raceId);

        if (race == null) {
            throw new MyException("No such race exists");
        } else {
            System.out.println("Enter id of class you want to remove from race: ");
            int characterClassId = Integer.parseInt(br.readLine());
            CharacterClass characterClass = CharacterClassFinder.getInstance().findById(characterClassId);
            if (characterClass == null) {
                throw new MyException("No such class exists");
            } else {
                RaceCharacterClass raceCharacterClass = new RaceCharacterClass();
                raceCharacterClass.setRaceId(raceId);
                raceCharacterClass.setClassId(characterClassId);
                raceCharacterClass.delete();
                System.out.println("class no longer of race"); // ;)
            }
        }
    }

    /**
     * adds class queried by the user to the race queried by the user
     * @throws IOException
     * @throws SQLException
     * @throws MyException
     */
    private void addClassToRace() throws IOException, SQLException, MyException {
        System.out.println("Enter race id: ");
        int raceId = Integer.parseInt(br.readLine());

        Race race = RaceFinder.getInstance().findById(raceId);

        if (race == null) {
            throw new MyException("No such race exists");
        } else {
            System.out.println("Enter id of class you want to add: ");
            int characterClassId = Integer.parseInt(br.readLine());
            CharacterClass characterClass = CharacterClassFinder.getInstance().findById(characterClassId);
            if (characterClass == null) {
                throw new MyException("No such class exists");
            } else {
                RaceCharacterClass raceCharacterClass = new RaceCharacterClass();
                raceCharacterClass.setRaceId(raceId);
                raceCharacterClass.setClassId(characterClassId);
                raceCharacterClass.insert();
                System.out.println("Class successfully added to race");
            }
        }
    }

    /**
     * deletes race queried by the user
     * @throws IOException
     * @throws SQLException
     * @throws MyException
     */
    private void deleteRace() throws IOException, SQLException, MyException {

        System.out.println("Enter race id: ");
        int raceId = Integer.parseInt(br.readLine());

        Race race = RaceFinder.getInstance().findById(raceId);

        if (race == null) {
            throw new MyException("No such race exists");
        } else {
            race.delete();
            System.out.println("The race has been successfully deleted");
        }
    }

    /**
     * adds race created with the parameters given by the user
     * @throws SQLException
     * @throws IOException
     */
    private void addRace() throws SQLException, IOException {

        Race race = new Race();

        System.out.println("Enter name:");
        race.setName(br.readLine());

        race.insert();

        System.out.println("The race has been successfully added");
        System.out.print("The race's id is: ");
        System.out.println(race.getId());
    }

    /**
     * shows all races
     * @throws SQLException
     */
    private void showAllRaces() throws SQLException {
        RacePrinter.getInstance().printTableHeaders();
        for (Race race : RaceFinder.getInstance().findAll()) {
            RacePrinter.getInstance().print(race);
        }
    }

    /**
     * STATISTIC OPERATION
     * shows table "weekly rippers" where for every week it shows a character (ripper) who has defeated the most characters in that week
     * and shows table "monthly rippers" where for every month it shows a character (ripper) who has defeated the most characters in that month
     * both tables also show (count of females and males defeated by that ripper)
     * @throws SQLException
     */
    private void jackTheRipperStats() throws SQLException {
        System.out.println("*** JACK THE RIPPER STATS ***");
        System.out.println("WEEKLY RIPPERS");
        JackTheRipperPrinter.getInstance().printTableHeaders();
        for (JackTheRipper j : JackTheRipperFinder.getInstance().findAllWeekly()) {
            JackTheRipperPrinter.getInstance().print(j);
        }
        System.out.println();
        System.out.println("MONTHLY RIPPERS");
        JackTheRipperPrinter.getInstance().printTableHeaders();
        for (JackTheRipper j : JackTheRipperFinder.getInstance().findAllMonthly()) {
            JackTheRipperPrinter.getInstance().print(j);
        }
    }

    /**
     * deletes a character queried by the user
     * @throws IOException
     * @throws SQLException
     * @throws MyException
     */
    private void deleteGameCharacter() throws IOException, SQLException, MyException {
        System.out.println("Enter game character's id:");
        int gameCharacterId = Integer.parseInt(br.readLine());

        GameCharacter gameCharacter = GameCharacterFinder.getInstance().findById(gameCharacterId);

        if (gameCharacter == null) {
            throw new MyException("No such game character exists");
        } else {
            gameCharacter.delete();
            System.out.println("The game character no longer exists");
        }
    }

    /**
     * shows a character queried by the user
     * @throws IOException
     * @throws SQLException
     * @throws MyException
     */
    private void showGameCharacter() throws IOException, SQLException, MyException {
        System.out.println("Enter character's id:");
        int gameCharacterId = Integer.parseInt(br.readLine());

        GameCharacter gameCharacter = GameCharacterFinder.getInstance().findById(gameCharacterId);

        if (gameCharacter == null) {
            throw new MyException("No such character exists");
        } else {
            GameCharacterPrinter.getInstance().printTableHeaders();
            GameCharacterPrinter.getInstance().print(gameCharacter);
        }
    }

    /**
     * lists a range of characters queried by the page of the user
     * @throws SQLException
     * @throws MyException
     * @throws IOException
     */
    private void showGameCharacters() throws SQLException, MyException, IOException {
        System.out.println("Enter number of page: ");
        int n = Integer.parseInt(br.readLine()) - 1;
        if (n < 0)
            throw new MyException("Page number has to be greater than 0");

        List<GameCharacter> l =  GameCharacterFinder.getInstance().findAllOnPage(n);
        if (l.size() == 0)
            throw new MyException("Page number contains no entries");

        GameCharacterPrinter.getInstance().printTableHeaders();
        for (GameCharacter character : l) {
            GameCharacterPrinter.getInstance().print(character);
        }

    }

    /**
     * COMPLEX OPERATION
     * changes character's hair type and body type and
     * if the character's account has enough credits then credits are subtracted from the account
     * also make a entry into the transactions history
     * @throws MyException
     * @throws SQLException
     * @throws IOException
     * @throws AccountServiceException
     */
    private void changeAppearance() throws MyException, SQLException, IOException, AccountServiceException {

        System.out.println("Enter id of character you want to change appearance : ");
        int characterId = Integer.parseInt(br.readLine());
        System.out.println("Type in number to change \"hair type\"");
        int hairType = Integer.parseInt(br.readLine());
        System.out.println("Type in number to change \"body type\"");
        int bodyType = Integer.parseInt(br.readLine());
        AccountService.getInstance().changingAppearance(characterId, hairType, bodyType);
        System.out.println("character's appearance changed successfully");
    }

    /**
     * deletes account queried by the user
     * @throws IOException
     * @throws SQLException
     * @throws MyException
     */
    private void deleteAccount() throws IOException, SQLException, MyException {
        System.out.println("Enter account's id:");
        int accountId = Integer.parseInt(br.readLine());

        Account account = AccountFinder.getInstance().findById(accountId);

        if (account == null) {
            throw new MyException("No such account exists");
        } else {
            account.delete();
            System.out.println("The account has been successfully deleted");
        }
    }

    /**
     * update account's parameters to new given by the user
     * @throws SQLException
     * @throws IOException
     * @throws MyException
     */
    private void updateAccount() throws SQLException, IOException, MyException {
        System.out.println("Enter account's id:");
        int accountId = Integer.parseInt(br.readLine());

        Account account = AccountFinder.getInstance().findById(accountId);

        if (account == null) {
            throw new MyException("No such account exists");
        } else {
            System.out.println("Enter username:");
            account.setUsername(br.readLine());
            System.out.println("Enter mail:");
            account.setMail(br.readLine());
            System.out.println("Enter password:");
            account.setPassword(br.readLine());

            account.update();

            System.out.println("The account has been successfully updated");
        }
    }

    /**
     * adds new account with parameters given by the user
     * @throws SQLException
     * @throws IOException
     */
    private void addAccount() throws SQLException, IOException {

        Account account = new Account();

        System.out.println("Enter username:");
        account.setUsername(br.readLine());
        System.out.println("Enter mail:");
        account.setMail(br.readLine());
        System.out.println("Enter password:");
        account.setPassword(br.readLine());
        account.setCredits(0);

        account.insert();

        System.out.println("The account has been successfully added");
        System.out.print("The account's id is: ");
        System.out.println(account.getId());
    }

    /**
     * show's account queried by the user
     * @throws IOException
     * @throws SQLException
     * @throws NumberFormatException
     * @throws MyException
     */
    private void showAccount() throws IOException, SQLException, NumberFormatException, MyException {

        System.out.println("Enter account id:");
        int accountId = Integer.parseInt(br.readLine());

        Account account = AccountFinder.getInstance().findById(accountId);

        if (account == null) {
            throw new MyException("No such account exists");
        } else {
            AccountPrinter.getInstance().printTableHeaders();
            AccountPrinter.getInstance().print(account);
        }
    }

    /**
     * show's transactions of account queried by the user
     * @throws IOException
     * @throws SQLException
     * @throws MyException
     */
    private void showAccountTransactions() throws IOException, SQLException, MyException {

        System.out.println("Enter account id:");
        int accountId = Integer.parseInt(br.readLine());

        Account account = AccountFinder.getInstance().findById(accountId);

        if (account == null) {
            throw new MyException("No such account exists");
        } else {
            TransactionHistoryPrinter.getInstance().printTableHeaders();
            for (TransactionHistory th : TransactionHistoryFinder.getInstance().findAllByAccountId(accountId))
                TransactionHistoryPrinter.getInstance().print(th);
        }
    }

    /**
     * lists a range of accounts queried by the page of the user
     * @throws SQLException
     * @throws IOException
     * @throws MyException
     */
    private void listAccounts() throws SQLException, IOException, MyException {
        System.out.println("Enter number of page: ");
        int n = Integer.parseInt(br.readLine()) - 1;
        if (n < 0)
            throw new MyException("Page number has to be greater than 0");

        List<GameCharacter> l =  GameCharacterFinder.getInstance().findAllOnPage(n);
        if (l.size() == 0)
            throw new MyException("Page number contains no entries");

        AccountPrinter.getInstance().printTableHeaders();
        for (Account account : AccountFinder.getInstance().findAllOnPage(n)) {
            AccountPrinter.getInstance().print(account);
        }
    }
}