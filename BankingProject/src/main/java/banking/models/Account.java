package banking.models;

import banking.interfaces.AccountOperations;

public abstract class Account implements AccountOperations {
	String accountId;
	User accountHolder;
	double balance;

	public Account(String accountId, User accountHolder, double balance) {
		this.accountId = accountId;
		this.accountHolder = accountHolder;
		this.balance = balance;
	}

	public String getAccountId() {
		return this.accountId;
	}

	public User getAccountHolder() {
		return this.accountHolder;
	}

	public double getRate() {
		return 0;
	}

	public double getOverDraftLimit() {
		return 0;
	}

	public synchronized double getBalance() {
		return this.balance;
	}
}
