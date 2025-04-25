package banking.interfaces;

import banking.exceptions.InsufficientFundsException;

public interface AccountOperations {

	public void deposit(double amount);

	public void withdraw(double amount) throws InsufficientFundsException;

}
