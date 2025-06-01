package com.auction.model;

public class Auction {
	private int id;
	private int totalMonths;
	private int currentMonth;

	// Getters and setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTotalMonths() {
		return totalMonths;
	}

	public void setTotalMonths(int totalMonths) {
		this.totalMonths = totalMonths;
	}

	public int getCurrentMonth() {
		return currentMonth;
	}

	public void setCurrentMonth(int currentMonth) {
		this.currentMonth = currentMonth;
	}
}
