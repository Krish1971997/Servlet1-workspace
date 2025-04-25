package banking.models;

import banking.exceptions.InsufficientFundsException;

public class CheckingsAccount extends Account {
	private double overDraftLimit;

	public CheckingsAccount(String accountId, User accountHolder, double balance, double overDraftLimit) {
		super(accountId, accountHolder, balance);
		this.overDraftLimit = overDraftLimit;
	}

	public double getOverDraftLimit() {
		return this.overDraftLimit;
	}

	public void withdraw(double amount) throws InsufficientFundsException {
		synchronized (this) {
			if ((balance + overDraftLimit) > amount) {
				balance -= amount;
				System.out.println("Checking Account: withdraw successful. Current balance: " + balance);
			} else {
				throw new InsufficientFundsException("Insufficient Funds. Please add funds and try again");
			}
		}
	}

	public void deposit(double amount) {
		synchronized (this) {
			balance += amount;
			System.out.println("Checking Account: deposit of " + amount + " successful. Current Balance: " + balance);
		}
	}

	@Override
	public String toString() {
		return ("\nAccount ID: " + accountId + "\nAccountHolder: " + accountHolder + "\nBalance: " + balance
				+ "\nOverDraftLimit: " + overDraftLimit);
	}
}
