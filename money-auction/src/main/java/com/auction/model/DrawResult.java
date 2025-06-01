package com.auction.model;

import java.sql.Timestamp;

public class DrawResult {
	private int id;
	private int auctionId;
	private int userId;
	private String userName;
	private int month;
	private double amount;
	private Timestamp drawDate;

	// Getters and setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAuctionId() {
		return auctionId;
	}

	public void setAuctionId(int auctionId) {
		this.auctionId = auctionId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Timestamp getDrawDate() {
		return drawDate;
	}

	public void setDrawDate(Timestamp drawDate) {
		this.drawDate = drawDate;
	}
}
