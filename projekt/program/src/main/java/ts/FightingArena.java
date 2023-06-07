package ts;

import main.DbContext;
import rdg.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 *
 * @author Rastislav Urbanek
 */
public class FightingArena {
    private static final FightingArena INSTANCE = new FightingArena();
    Random rnd = new Random();

    public static FightingArena getInstance() { return INSTANCE; }
    private FightingArena() { }


    /**
     * sets the transaction isolation to repeatable read and executes doFight(gameCharacterId1, gameCharacterId2)
     * repeats this if a serialization error (40001) has occurred
     * @param gameCharacterId1
     * @param gameCharacterId2
     * @return log to be printed in mainMenu
     * @throws SQLException transaction rollback unless the sqltype is 40001 then it repeats
     * @throws MyException transaction rollback
     * @throws FightingException transaction rollback
     */
    public String fighting(int gameCharacterId1, int gameCharacterId2) throws SQLException, MyException, FightingException {
        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

        int numberOfTries = 5;
        while (numberOfTries-- > 0) {
            DbContext.getConnection().setAutoCommit(false);
            try {
                String log = doFight(gameCharacterId1, gameCharacterId2);
                DbContext.getConnection().commit();
                DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                return log;
            } catch (SQLException e) {
                DbContext.getConnection().rollback();
                if (e.getSQLState().equals("40001") == false) {
                    throw e;
                }
            } catch (Exception e) {
                DbContext.getConnection().rollback();
                throw e;
            } finally {
                DbContext.getConnection().setAutoCommit(true);
            }
        }
        throw new FightingException("Please try the fight again, an error has occurred");
    }

    /**
     * checks if the gameCharacters exist and randomly select the attacker and defender and randomly heals one of the characters or attacks the defender
     * if defender killed makes new Fight entry and increase exp of attacker
     * @param gameCharacterId1
     * @param gameCharacterId2
     * @return
     * @throws MyException
     * @throws SQLException
     * @throws FightingException
     */
    private String doFight(int gameCharacterId1, int gameCharacterId2) throws MyException, SQLException, FightingException {
        GameCharacter gameCharacter1 = GameCharacterFinder.getInstance().findById(gameCharacterId1);
        if (gameCharacter1 == null)
            throw new FightingException("1st character doesn't exist");
        GameCharacter gameCharacter2 = GameCharacterFinder.getInstance().findById(gameCharacterId2);
        if (gameCharacter2 == null)
            throw new FightingException("2nd character doesn't exist");
        if (gameCharacter1.getCurrentHealth() == 0 && gameCharacter2.getCurrentHealth() == 0)
            throw new FightingException("Both characters are dead! There's nothing that can be done");

        List<Ability> abilities1 = AbilityFinder.getInstance().findByCharacterClassId(gameCharacter1.getCharacterClassId()); //this is a disgrace
        List<Ability> abilities2 = AbilityFinder.getInstance().findByCharacterClassId(gameCharacter2.getCharacterClassId());
        List<Ability> healing1 = abilities1.stream().filter(Ability::isHealing).collect(Collectors.toList());
        List<Ability> healing2 = abilities2.stream().filter(Ability::isHealing).collect(Collectors.toList());
        List<Ability> damaging1 = abilities1.stream().filter(e->!e.isHealing()).collect(Collectors.toList());
        List<Ability> damaging2 = abilities2.stream().filter(e->!e.isHealing()).collect(Collectors.toList());

        EffectiveAttributes stats1 = EffectiveAttributesFinder.getInstance().findById(gameCharacterId1);
        EffectiveAttributes stats2 = EffectiveAttributesFinder.getInstance().findById(gameCharacterId2);


        GameCharacter attacker = gameCharacter1;
        GameCharacter defender = gameCharacter2;
        EffectiveAttributes attStats = stats1;
        EffectiveAttributes defStats = stats2;
        List<Ability> attHealing = healing1;
        List<Ability> attDamaging = damaging1;
        if (rnd.nextBoolean() || attacker.getCurrentHealth() == 0) {
            attacker = gameCharacter2;
            defender = gameCharacter1;
            attStats = stats2;
            defStats = stats1;
            attHealing = healing2;
            attDamaging = damaging2;
        }

        if (rnd.nextBoolean()) {
            Ability healAbility = attHealing.get(rnd.nextInt(attHealing.size()));
            int heal = (int) Math.round(healAbility.getPowerFactor() * attStats.getPower());
            GameCharacter charToHeal = rnd.nextBoolean() ? attacker : defender;
            heal(charToHeal,(charToHeal==attacker) ? attStats : defStats, heal);

            return attacker.getName() + " is healing " + charToHeal.getName() + " by " + heal + "hp with " + healAbility.getName();
        } else {
            if (defender.getCurrentHealth() == 0)
                throw new FightingException("Defending character is already dead!");
            Ability dmgAbility = attDamaging.get(rnd.nextInt(attDamaging.size()));
            int damage = (int) Math.round(dmgAbility.getPowerFactor() * attStats.getPower());
            damage(defender,  defStats, damage);
            String log = attacker.getName() + " attacked " + defender.getName() + " with " + dmgAbility.getName() + " and dealt " + damage + " damage";
            if (defender.getCurrentHealth() == 0) {
                log += "\n" + defender.getName() + " has been killed!";
                makeFightEntry(attacker.getId(), defender.getId());
                int exp = Math.abs(attacker.getLevel() - defender.getLevel()) + 10;
                attacker.setExperience(attacker.getExperience()+exp);
                if (attacker.getExperience() > GameCharacter.calculateExp(attacker.getLevel()+1))
                    levelUp(attacker);
            }
            return log;
        }
    }

    /**
     * heals the gameCharacter by amount and makes sure the current_hp doesn't exceeds the maximum hp, and updates
     * @param gameCharacter
     * @param stats
     * @param amount
     * @throws SQLException
     */
    private void heal(GameCharacter gameCharacter, EffectiveAttributes stats, int amount) throws SQLException {
        gameCharacter.setCurrentHealth(Math.min(stats.getMaxHealth(), gameCharacter.getCurrentHealth() + amount));
        gameCharacter.update();
    }

    /**
     * damages the gameCharacter by amount and makes sure the current_hp doesn't go negative, and updates
     * @param gameCharacter
     * @param defStats
     * @param amount
     * @throws SQLException
     */
    private void damage(GameCharacter gameCharacter, EffectiveAttributes defStats, int amount) throws SQLException {
        amount = Math.max(0, amount - defStats.getDefense());
        gameCharacter.setCurrentHealth(Math.max(0, gameCharacter.getCurrentHealth() - amount));
        System.out.println("gethp"+ gameCharacter.getCurrentHealth());
        gameCharacter.update();
    }

    /**
     * levels the gameCharacter up and updates
     * @param gameCharacter
     * @throws MyException
     * @throws SQLException
     */
    private void levelUp(GameCharacter gameCharacter) throws MyException, SQLException {
        gameCharacter.setLevel(gameCharacter.getLevel()+1);
        gameCharacter.update();
    }

    /**
     * makes new Fight entry with params
     * @param winnerId
     * @param loserId
     * @throws SQLException
     */
    private void makeFightEntry(int winnerId, int loserId) throws SQLException {
        Fight fight = new Fight();
        fight.setWinnerId(winnerId);
        fight.setLoserId(loserId);
        fight.setDateOfFight(new Timestamp(System.currentTimeMillis()));
        fight.insert();
    }
}
