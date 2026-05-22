package com.zohopayments;

import com.zohopayments.auth.OAuthToken;
import com.zohopayments.net.DefaultHttpClient;
import com.zohopayments.net.HttpClientInterface;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class ZohoPayments {
	static final String SDK_NAME = "zoho-payments-java-sdk";
	static final String SDK_VERSION = "1.0.0";
	private static final Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofSeconds(30L);
	private static final Duration DEFAULT_REQUEST_TIMEOUT = Duration.ofSeconds(60L);

	private ZohoPayments() {
	}

	public static Builder builder() {
		return new Builder();
	}

	public static OAuthToken generateAccessToken(String refreshToken, String clientId, String clientSecret,
			String redirectUri, Edition edition) {
		return OAuthTokenRefresher.generateAccessToken(refreshToken, clientId, clientSecret, redirectUri, edition);
	}

	public static final class Builder {
		private static final Set<String> RESERVED_HEADERS = Set.of("authorization", "user-agent", "accept",
				"content-type", "content-length", "host");
		private String accountId;
		private Edition edition;
		private String accessToken;
		private HttpClientInterface httpClient;
		private Duration connectTimeout;
		private Duration requestTimeout;
		private final Map<String, String> defaultHeaders = new LinkedHashMap();
		private boolean consumed;

		private Builder() {
		}

		public Builder accountId(String accountId) {
			this.checkNotConsumed();
			this.accountId = accountId;
			return this;
		}

		public Builder edition(Edition edition) {
			this.checkNotConsumed();
			this.edition = edition;
			return this;
		}

		public Builder oauthToken(String accessToken) {
			this.checkNotConsumed();
			if (accessToken != null && !accessToken.isBlank()) {
				this.accessToken = accessToken;
				return this;
			} else {
				throw new IllegalArgumentException("oauthToken is required");
			}
		}

		public Builder oauthToken(OAuthToken token) {
			this.checkNotConsumed();
			if (token != null && token.getAccessToken() != null && !token.getAccessToken().isBlank()) {
				this.accessToken = token.getAccessToken();
				return this;
			} else {
				throw new IllegalArgumentException("oauthToken is required");
			}
		}

		public Builder httpClient(HttpClientInterface httpClient) {
			this.checkNotConsumed();
			this.httpClient = httpClient;
			return this;
		}

		public Builder connectTimeout(Duration connectTimeout) {
			this.checkNotConsumed();
			this.connectTimeout = connectTimeout;
			return this;
		}

		public Builder requestTimeout(Duration requestTimeout) {
			this.checkNotConsumed();
			this.requestTimeout = requestTimeout;
			return this;
		}

		public Builder addDefaultHeader(String name, String value) {
			this.checkNotConsumed();
			if (name != null && !name.isBlank()) {
				if (value == null) {
					throw new IllegalArgumentException("header value is required (name=" + name + ")");
				} else if (RESERVED_HEADERS.contains(name.toLowerCase(Locale.ROOT))) {
					throw new IllegalArgumentException(
							"Header '" + name + "' is managed by the SDK and cannot be set via addDefaultHeader");
				} else {
					this.defaultHeaders.put(name, value);
					return this;
				}
			} else {
				throw new IllegalArgumentException("header name is required");
			}
		}

		public ZohoPaymentsClient build() {
			this.checkNotConsumed();
			if (this.accountId != null && !this.accountId.isEmpty()) {
				if (this.edition == null) {
					throw new IllegalArgumentException(
							"edition is required (Edition.IN, Edition.IN_SANDBOX, or Edition.US)");
				} else if (this.accessToken != null && !this.accessToken.isEmpty()) {
					if (this.httpClient != null && this.connectTimeout != null) {
						throw new IllegalArgumentException(
								"connectTimeout cannot be combined with a custom httpClient ��� configure the connect timeout on your custom transport instead");
					} else {
						TokenManager tokenManager = new TokenManager(this.accessToken);
						Duration rt = this.requestTimeout != null ? this.requestTimeout
								: ZohoPayments.DEFAULT_REQUEST_TIMEOUT;
						HttpClientInterface transport = this.httpClient;
						if (transport == null) {
							Duration ct = this.connectTimeout != null ? this.connectTimeout
									: ZohoPayments.DEFAULT_CONNECT_TIMEOUT;
							transport = new DefaultHttpClient(ct, rt);
						}

						ZohoHttpClient zohoHttpClient = new ZohoHttpClient(transport, tokenManager, this.edition,
								this.accountId, rt, this.defaultHeaders);
						this.consumed = true;
						return new ZohoPaymentsClient(zohoHttpClient, tokenManager, this.edition);
					}
				} else {
					throw new IllegalArgumentException("oauthToken is required");
				}
			} else {
				throw new IllegalArgumentException("accountId is required");
			}
		}

		private void checkNotConsumed() {
			if (this.consumed) {
				throw new IllegalStateException(
						"This Builder has already produced a client; create a new builder via ZohoPayments.builder().");
			}
		}
	}
}
