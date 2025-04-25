package banking.models;

import banking.exceptions.InsufficientFundsException;

public class SavingsAccount extends Account {
	private double rate;

	public SavingsAccount(String accountId, User accountHolder, double balance, double rate) {
		super(accountId, accountHolder, balance);
		this.rate = rate;
	}

	public void withdraw(double amount) throws InsufficientFundsException {
		synchronized (this) {
			if (balance >= amount) {
				balance -= amount;
				System.out.println(
						"Savings Account withdrawal successful from " + accountId + " current balance: " + balance);
			} else {
				throw new InsufficientFundsException("Insufficient Funds. Please add more funds and try again");
			}
		}
	}

	public void deposit(double amount) {
		synchronized (this) {
			balance += amount;
			System.out.println("Savings Account deposit successful to " + accountId + " current balance: " + balance);
		}
	}

	public double getRate() {
		return this.rate;
	}

	@Override
	public String toString() {
		return ("\nAccount ID: " + accountId + "\nAccountHolder: " + accountHolder + "\nBalance: " + balance
				+ "\nInterest Rate: " + rate + "\nAt this rate Interest per year: " + (balance / 100) * rate);
	}
}
