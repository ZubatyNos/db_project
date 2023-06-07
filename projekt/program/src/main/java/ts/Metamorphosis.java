package ts;

import main.DbContext;
import rdg.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

public class Metamorphosis {
    Random rnd = new Random();
    private static final Metamorphosis INSTANCE = new Metamorphosis();

    public static Metamorphosis getInstance() {
        return INSTANCE;
    }

    private Metamorphosis() {
    }

    /**
     * sets the transaction isolation to repeatable read and executes doTransferGameCharacter(characterId, accountId, negotiatedPrice) and commits
     * repeats this max 5 times if a serialization error (40001) has occurred
     * @param accountId
     * @param gameCharacterId1
     * @param gameCharacterId2
     * @throws SQLException transaction rollback unless the sqltype is 40001 then it repeats
     * @throws MetamorphosisException transaction rollback
     * @throws MyException transaction rollback
     */
    public void metaMorphing(int accountId, int gameCharacterId1, int gameCharacterId2) throws SQLException, MetamorphosisException, MyException {
        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

        int numberOfTries = 5;
        while (numberOfTries-- > 0) {
            DbContext.getConnection().setAutoCommit(false);
            try {
                doMetamorphosis(accountId, gameCharacterId1, gameCharacterId2);
                DbContext.getConnection().commit();
                return;
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
    }

    /**
     * checkes whether the characters aren't the same and if they are of the same account
     * if yes then performs morph()
     * @param accountId
     * @param gameCharacterId1
     * @param gameCharacterId2
     * @throws MetamorphosisException
     * @throws SQLException
     * @throws MyException
     */
    private void doMetamorphosis(int accountId, int gameCharacterId1, int gameCharacterId2) throws MetamorphosisException, SQLException, MyException {
        Account account = AccountFinder.getInstance().findById(accountId);
        if (account == null)
            throw new MetamorphosisException("No such account exists");
        if (gameCharacterId1 == gameCharacterId2)
            throw new MetamorphosisException("Cannot morph the same character");

        GameCharacter gameCharacter1 = GameCharacterFinder.getInstance().findById(gameCharacterId1);
        GameCharacter gameCharacter2 = GameCharacterFinder.getInstance().findById(gameCharacterId2);
        if (gameCharacter1 == null)
            throw new MetamorphosisException("No such game character with id "+gameCharacterId1);
        if (gameCharacter2 == null)
            throw new MetamorphosisException("No such game character with id " + gameCharacterId2);
        if (gameCharacter1.getAccountId() != gameCharacter2.getAccountId())
            throw new MetamorphosisException("Characters not of the same account");

        morph(gameCharacter1, gameCharacter2);
    }


    /**
     * creates and insert new gameCharacter with random parameters randomly picked from either of the gameCharacters1/2
     * makes sure the new power stat of the morphed character is about the the same as the sum of the gameCharacters1 and 2
     * then it deletes the gameCharacters1 and 2
     * @param gameCharacter1
     * @param gameCharacter2
     * @throws MyException
     * @throws SQLException
     */
    private void morph(GameCharacter gameCharacter1, GameCharacter gameCharacter2) throws MyException, SQLException {
        GameCharacter morphedCharacter = new GameCharacter();
        morphedCharacter.setName(gameCharacter1.getName() + "_" + gameCharacter2.getName());
        morphedCharacter.setBodyType(rnd.nextBoolean() ? gameCharacter1.getBodyType() : gameCharacter2.getBodyType());
        morphedCharacter.setHairType(rnd.nextBoolean() ? gameCharacter1.getHairType() : gameCharacter2.getHairType());
        morphedCharacter.setGender(rnd.nextBoolean() ? gameCharacter1.getGender() : gameCharacter2.getGender());
        morphedCharacter.setAccountId(gameCharacter1.getAccountId());

        int raceId = gameCharacter1.getRaceId();
        int characterClassId = gameCharacter1.getCharacterClassId();
        if (rnd.nextBoolean()) {
            raceId = gameCharacter2.getRaceId();
            characterClassId = gameCharacter2.getCharacterClassId();
        }
        morphedCharacter.setRaceId(raceId);
        morphedCharacter.setCharacterClassId(characterClassId);

        morphedCharacter.setLevel(1);
        morphedCharacter.setExperience(GameCharacter.calculateExp(1));
        morphedCharacter.setCurrentHealth(1);

        morphedCharacter.insert();


        EffectiveAttributes char1Stats = EffectiveAttributesFinder.getInstance().findById(gameCharacter1.getId());
        EffectiveAttributes char2Stats = EffectiveAttributesFinder.getInstance().findById(gameCharacter2.getId());

        Item item = new Item();
        item.setName("Two soul amulet");
        item.setPowerModifier(
                (int)((char1Stats.getPower() + char2Stats.getPower())*(rnd.nextFloat()*0.6 + 0.7))
        );
        item.insert();

        CharacterItem charItem = new CharacterItem();
        charItem.setEquipped(true);
        charItem.setItemId(item.getId());
        charItem.setCharacterId(morphedCharacter.getId());
        charItem.insert();

        morphedCharacter.update(); //prepocitanie stlpca current_health
        gameCharacter1.delete();
        gameCharacter2.delete();
    }
}
