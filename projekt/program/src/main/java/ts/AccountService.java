package ts;

import main.DbContext;
import rdg.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Rastislav Urbanek
 */
public class AccountService {
    private static final AccountService INSTANCE = new AccountService();

    public static AccountService getInstance() { return INSTANCE; }
    private AccountService() { }


    /**
     * adds credits to account and updates it
     * @param account
     * @param credits
     * @throws SQLException
     */
    public void addCredits(Account account, int credits) throws SQLException {
        account.setCredits(account.getCredits() + credits);
        account.update();
    }

    /**
     * subtracts credits from accounts and updates it
     * @param account
     * @param credits
     * @throws SQLException
     */
    public void subtractCredits(Account account, int credits) throws SQLException {
        account.setCredits(account.getCredits() - credits);
        account.update();
    }


    /**
     * sets the transaction isolation to repeatable read and executes doTransferGameCharacter(characterId, accountId, negotiatedPrice) and commits
     * repeats this max 5 times if a serialization error (40001) has occurred
     * @param characterId
     * @param accountId
     * @param negotiatedPrice
     * @throws SQLException transaction rollback unless the sqltype is 40001 then it repeats
     * @throws MyException transaction rollback
     * @throws AccountServiceException transaction rollback
     * @throws IOException transaction rollback
     */
    public void transferringGameCharacter(int characterId, int accountId, int negotiatedPrice) throws SQLException, MyException, AccountServiceException, IOException {
        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

        int numberOfTries = 5;
        while (numberOfTries-- > 0) {
            DbContext.getConnection().setAutoCommit(false);
            try {
                doTransferGameCharacter(characterId, accountId, negotiatedPrice);
                DbContext.getConnection().commit();
                DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
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
     * finds gameCharacter by characterId, finds the account of the gameCharacter
     * finds character transfer service
     * checks if the account has enough credits for the service + negotiatedPrice
     * if yes then changes the gameCharacter accountId to tne newAccountId and updates
     * @param characterId
     * @param newAccountId
     * @param negotiatedPrice
     * @throws MyException
     * @throws SQLException
     * @throws AccountServiceException
     * @throws IOException
     */
    private void doTransferGameCharacter(int characterId, int newAccountId, int negotiatedPrice) throws MyException, SQLException, AccountServiceException, IOException {
        GameCharacter gameCharacter = GameCharacterFinder.getInstance().findById(characterId);
        if (gameCharacter == null)
            throw new AccountServiceException("No such character exists");
        if (newAccountId == gameCharacter.getAccountId())
            throw new AccountServiceException("Can't transfer own character to self");

        Account newAccount = AccountFinder.getInstance().findById(newAccountId);
        if (newAccount == null)
            throw new AccountServiceException("Account you want to transfer the character to doesn't exist");

        Account oldAccount = AccountFinder.getInstance().findById(gameCharacter.getAccountId());
        if (oldAccount == null)
            throw new AccountServiceException("Character's account no longer exists");

        Service service = ServiceFinder.getInstance().findByType("character transfer");
        if (newAccount.getCredits() < service.getCredits() + negotiatedPrice)
            throw new AccountServiceException("character's new account doesn't have enough credits for this operation");

        gameCharacter.setAccountId(newAccountId);
        gameCharacter.update();
        subtractCredits(newAccount, service.getCredits() + negotiatedPrice);
        makeNewTransactionHistory(newAccount.getId(), "character transfer", service.getCredits() + negotiatedPrice);
        addCredits(oldAccount, negotiatedPrice);
        makeNewTransactionHistory(oldAccount.getId(), "character transfer", negotiatedPrice);
    }

    /**
     * sets the transaction isolation to repeatable read, executes doChangeAppearance(characterId, hairType, bodyType) and commits
     * repeats this max 5 times if a serialization error (40001) has occurred
     * @param characterId
     * @param hairType
     * @param bodyType
     * @throws SQLException transaction rollback unless the sqltype is 40001 then it repeats
     * @throws MyException transaction rollback
     * @throws AccountServiceException transaction rollback
     * @throws IOException transaction rollback
     */
    public void changingAppearance(int characterId, int hairType, int bodyType) throws SQLException, MyException, AccountServiceException, IOException {
        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

        int numberOfTries = 5;
        while (numberOfTries-- > 0) {
            DbContext.getConnection().setAutoCommit(false);
            try {
                doChangeAppearance(characterId, hairType, bodyType);
                DbContext.getConnection().commit();
                DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
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
     * sets the transaction isolation to SERIALIZABLE, executes doRewardBountyHunter() and commits
     * repeats this max 5 times if a serialization error (40001) has occurred
     * @throws SQLException transaction rollback unless the sqltype is 40001 then it repeats
     * @throws MyException transaction rollback
     * @throws AccountServiceException transaction rollback
     */
    public void rewardingBountyHunter() throws SQLException, MyException, AccountServiceException {
        DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

        int numberOfAttempts = 5;
        while (numberOfAttempts-- > 0) {
            DbContext.getConnection().setAutoCommit(false);
            try {
                doRewardBountyHunter();
                DbContext.getConnection().commit();
                DbContext.getConnection().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
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
     * reward the top 1 and top 3 characters and adds price to the respective character accounts
     * @throws MyException
     * @throws SQLException
     * @throws AccountServiceException
     */
    private void doRewardBountyHunter() throws MyException, SQLException, AccountServiceException {
        int prize = 1000, i = 0;
        for (GameCharacter gameCharacter : GameCharacterFinder.getInstance().findBountyHunters()) {
            Account account = AccountFinder.getInstance().findById(gameCharacter.getAccountId());
            if (account == null)
                throw new AccountServiceException("An account doesn't exist");
            addCredits(account, prize);
            prize -= ++i*100;
        }
    }


    /**
     * finds gameCharacter by characterId and appearance change service
     * find the gameCharacter's account and checks whether it has enough credits for the appearance change service
     * if yes then sets gameCharacters's hairType and bodyType to hairType and bodyType params, updates the gameCharacter
     * substracts the credits from the account and make a new transaction history entry
     * @param characterId
     * @param hairType
     * @param bodyType
     * @throws SQLException
     * @throws AccountServiceException
     * @throws MyException
     * @throws IOException
     */
    private void doChangeAppearance(int characterId, int hairType, int bodyType) throws SQLException, AccountServiceException, MyException, IOException {
        GameCharacter gameCharacter = GameCharacterFinder.getInstance().findById(characterId);
        if (gameCharacter == null)
            throw new AccountServiceException("No such character exists");

        Account account = AccountFinder.getInstance().findById(gameCharacter.getAccountId()); //account can be null right?
        if (account == null)
            throw new AccountServiceException("No such account");

        Service service = ServiceFinder.getInstance().findByType("appearance change");
        if (account.getCredits() < service.getCredits())
            throw new AccountServiceException("character's account doesn't have enough credits");

        gameCharacter.setHairType(hairType);
        gameCharacter.setBodyType(bodyType);
        gameCharacter.update();
        subtractCredits(account, service.getCredits());
        makeNewTransactionHistory(account.getId(), "appearance change", service.getCredits());
    }

    /**
     * makes new transaction entry with accountId, type and amount
     * @param accountId
     * @param type
     * @param amount
     * @throws SQLException
     */
    public void makeNewTransactionHistory(int accountId, String type, int amount) throws SQLException {
        Service service = ServiceFinder.getInstance().findByType(type);
        TransactionHistory transactionHistory = new TransactionHistory();
        transactionHistory.setAccountId(accountId);
        transactionHistory.setCredits(amount);
        transactionHistory.setServiceId(service.getId());
        transactionHistory.setDescription(service.getType());
        transactionHistory.insert();
    }
}
