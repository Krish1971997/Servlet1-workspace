package com.expensemanager.model;

/**
 * Ported from Android PaymentTypeDao/PaymentType — user-managed payment
 * methods (Cash, UPI, Card, ...) with a single default. Transactions store
 * the name as plain text (no FK), matching the Android behaviour where
 * deleting a type keeps existing transaction values.
 */
public class PaymentType {
	private int id;
	private String name;
	private boolean isDefault;

	public PaymentType() {
	}

	public PaymentType(int id, String name, boolean isDefault) {
		this.id = id;
		this.name = name;
		this.isDefault = isDefault;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
}
