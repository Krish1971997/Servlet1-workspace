package banking.services;

import java.sql.SQLException;

import banking.dao.AccountDAO;
import banking.exceptions.InsufficientFundsException;
import banking.models.Account;

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
