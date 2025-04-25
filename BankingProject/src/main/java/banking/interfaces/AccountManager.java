package banking.interfaces;

import java.sql.SQLException;
import java.util.List;

import banking.exceptions.UserNotFoundException;
import banking.models.Account;
import banking.models.CheckingsAccount;
import banking.models.SavingsAccount;

public interface AccountManager {
    public Account getAccountById(String accountId) throws SQLException, UserNotFoundException;

    public int deleteSavingsAccount(String aid);

    public int deleteCheckingsAccount(String aid);

    public void UpdateRate(String rate, String aid);

    public void UpdateOverDraft(String od, String aid);

    public List<SavingsAccount> getAllSavingsAccount(boolean everified);

    public List<CheckingsAccount> getAllCheckingsAccount();

    public String getAccountHolderUserId(String accountId);

    public String getTpin(String accountId);

    public String getMD5(String pin) throws SQLException;

    public int createSavingsAccount(SavingsAccount account, String tpin) throws SQLException;

    public int createCheckingsAccount(CheckingsAccount account, String tpin) throws SQLException;

    public SavingsAccount getSavingsAccount(String accountId, String userId) throws SQLException, UserNotFoundException;

    public CheckingsAccount getCheckingsAccount(String accountId, String userId)
            throws SQLException, UserNotFoundException;

    public long getLastAccountId() throws SQLException;

    public void updateAccountBalance(Account account) throws SQLException;

}
