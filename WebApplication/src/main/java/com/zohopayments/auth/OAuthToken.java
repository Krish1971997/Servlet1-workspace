package com.zohopayments.auth;

public final class OAuthToken {
	private final String accessToken;
	private final long expiresIn;

	public OAuthToken(String accessToken, long expiresIn) {
		if (accessToken != null && !accessToken.isEmpty()) {
			this.accessToken = accessToken;
			this.expiresIn = expiresIn;
		} else {
			throw new IllegalArgumentException("access Token is required");
		}
	}

	public String getAccessToken() {
		return this.accessToken;
	}

	public long getExpiresIn() {
		return this.expiresIn;
	}
}
