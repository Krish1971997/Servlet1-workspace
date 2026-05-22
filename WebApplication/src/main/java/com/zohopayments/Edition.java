package com.zohopayments;

public enum Edition {
	IN("https://payments.zoho.in/api/v1", "https://accounts.zoho.in"),
	IN_SANDBOX("https://paymentssandbox.zoho.in/api/v1", "https://accounts.zoho.in"),
	US("https://payments.zoho.com/api/v1", "https://accounts.zoho.com");

	private final String baseUrl;
	private final String accountsUrl;

	private Edition(String baseUrl, String accountsUrl) {
		this.baseUrl = baseUrl;
		this.accountsUrl = accountsUrl;
	}

	public String getBaseUrl() {
		return this.baseUrl;
	}

	public String getAccountsUrl() {
		return this.accountsUrl;
	}

	public boolean isUS() {
		return this == US;
	}

	public boolean isIN() {
		return this == IN || this == IN_SANDBOX;
	}

	// $FF: synthetic method
	private static Edition[] $values() {
		return new Edition[] { IN, IN_SANDBOX, US };
	}
}
