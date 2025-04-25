package banking.interfaces;

import banking.exceptions.InsufficientFundsException;
import banking.models.Account;

public interface TransactionOperations {
	public void transfer(Account fromAccount, Account toAccount, double amount) throws InsufficientFundsException;

}
