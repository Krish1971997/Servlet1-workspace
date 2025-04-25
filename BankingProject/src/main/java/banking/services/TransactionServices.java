package banking.services;

import banking.interfaces.TransactionOperations;
import banking.models.Account;

import java.sql.SQLException;

import banking.dao.AccountDAO;
import banking.exceptions.InsufficientFundsException;
import banking.utils.ThreadlocalClass;

public class TransactionServices implements TransactionOperations {
	static AccountDAO accountDAO = new AccountDAO();

	public synchronized void transfer(Account fromAccount, Account toAccount, double amount)
			throws InsufficientFundsException {
		fromAccount.withdraw(amount);
		toAccount.deposit(amount);
		try {
			accountDAO.updateAccountBalance(fromAccount);
			accountDAO.updateAccountBalance(toAccount);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
