package banking.services;

import banking.models.Account;
import banking.models.CheckingsAccount;
import banking.models.SavingsAccount;
import banking.exceptions.InsufficientFundsException;
import banking.dao.AccountDAO;
import java.sql.SQLException;

public class AccountServices<T extends Account> {
	private AccountDAO accountDAO = new AccountDAO();

	public void deposit(T account, double amount) throws SQLException {
		account.deposit(amount);
		accountDAO.updateAccountBalance(account);
	}

	public void withdraw(T account, double amount) throws NullPointerException,InsufficientFundsException, SQLException {
		account.withdraw(amount);
		accountDAO.updateAccountBalance(account);
	}
}
