package com.zohopayments;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.zohopayments.auth.OAuthToken;
import com.zohopayments.exception.ConnectionException;
import com.zohopayments.exception.ZohoPaymentsException;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

final class OAuthTokenRefresher {
	private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(30L);
	private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(60L);
	private static final int MAX_ERROR_BODY_SNIPPET = 500;

	private OAuthTokenRefresher() {
	}

	static OAuthToken generateAccessToken(String refreshToken, String clientId, String clientSecret, String redirectUri,
			Edition edition) {
		if (refreshToken != null && !refreshToken.isEmpty()) {
			if (clientId != null && !clientId.isEmpty()) {
				if (clientSecret != null && !clientSecret.isEmpty()) {
					if (redirectUri != null && !redirectUri.isEmpty()) {
						if (edition == null) {
							throw new IllegalArgumentException("edition is required");
						} else {
							HttpClient httpClient = HttpClient.newBuilder().connectTimeout(CONNECT_TIMEOUT).build();

							OAuthToken var16;
							try {
								String tokenUrl = edition.getAccountsUrl() + "/oauth/v2/token";
								String var10000 = encode(refreshToken);
								String body = "refresh_token=" + var10000 + "&client_id=" + encode(clientId)
										+ "&client_secret=" + encode(clientSecret) + "&redirect_uri="
										+ encode(redirectUri) + "&grant_type=refresh_token";
								HttpRequest request = HttpRequest.newBuilder().uri(URI.create(tokenUrl))
										.timeout(REQUEST_TIMEOUT)
										.header("Content-Type", "application/x-www-form-urlencoded")
										.POST(BodyPublishers.ofString(body)).build();
								HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
								int statusCode = response.statusCode();
								String rawBody = (String) response.body();
								if (rawBody == null || rawBody.isBlank()) {
									throw new ZohoPaymentsException(
											"Token refresh failed: empty response body [HTTP " + statusCode + "]");
								}

								JsonObject json;
								try {
									json = JsonUtil.parseObject(rawBody);
								} catch (JsonSyntaxException e) {
									throw new ZohoPaymentsException(
											"Token refresh failed: response is not valid JSON [HTTP " + statusCode
													+ "]: " + truncateForMessage(rawBody),
											e);
								}

								if (json == null) {
									throw new ZohoPaymentsException(
											"Token refresh failed: expected a JSON object [HTTP " + statusCode + "]: "
													+ truncateForMessage(rawBody));
								}

								if (json.has("error") && !json.get("error").isJsonNull()) {
									throw new ZohoPaymentsException(
											"Token refresh failed: " + json.get("error").getAsString());
								}

								if (statusCode < 200 || statusCode >= 300) {
									throw new ZohoPaymentsException("Token refresh failed [HTTP " + statusCode + "]: "
											+ truncateForMessage(rawBody));
								}

								if (!json.has("access_token") || json.get("access_token").isJsonNull()) {
									throw new ZohoPaymentsException(
											"Token refresh failed: access_token not found in response");
								}

								String newAccessToken = json.get("access_token").getAsString();
								long expiresIn;
								if (json.has("expires_in_sec") && !json.get("expires_in_sec").isJsonNull()) {
									expiresIn = json.get("expires_in_sec").getAsLong();
								} else if (json.has("expires_in") && !json.get("expires_in").isJsonNull()) {
									expiresIn = json.get("expires_in").getAsLong();
								} else {
									expiresIn = 3600L;
								}

								var16 = new OAuthToken(newAccessToken, expiresIn);
							} catch (InterruptedException e) {
								Thread.currentThread().interrupt();
								throw new ZohoPaymentsException("Token refresh request interrupted", e);
							} catch (IOException e) {
								throw new ConnectionException("Token refresh request failed", e);
							} finally {
								closeQuietly(httpClient);
							}

							return var16;
						}
					} else {
						throw new IllegalArgumentException("redirectUri is required");
					}
				} else {
					throw new IllegalArgumentException("clientSecret is required");
				}
			} else {
				throw new IllegalArgumentException("clientId is required");
			}
		} else {
			throw new IllegalArgumentException("refreshToken is required");
		}
	}

	private static void closeQuietly(HttpClient client) {
		if (client instanceof AutoCloseable) {
			try {
				((AutoCloseable) client).close();
			} catch (InterruptedException var2) {
				Thread.currentThread().interrupt();
			} catch (Exception var3) {
			}
		}
	}

	private static String encode(String value) {
		return URLEncoder.encode(value, StandardCharsets.UTF_8);
	}

	private static String truncateForMessage(String body) {
		if (body == null) {
			return "";
		} else {
			String t = body.strip();
			return t.length() <= 500 ? t : t.substring(0, 500) + "...";
		}
	}
}
