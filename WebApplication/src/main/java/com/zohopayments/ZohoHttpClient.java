package com.zohopayments;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.zohopayments.exception.AuthenticationException;
import com.zohopayments.exception.InvalidRequestException;
import com.zohopayments.exception.PermissionException;
import com.zohopayments.exception.RateLimitException;
import com.zohopayments.exception.ResourceNotFoundException;
import com.zohopayments.exception.ZohoPaymentsAPIException;
import com.zohopayments.exception.ZohoPaymentsException;
import com.zohopayments.model.ListResponse;
import com.zohopayments.net.HttpClientInterface;
import com.zohopayments.net.RequestMethod;
import com.zohopayments.net.ZohoRequest;
import com.zohopayments.net.ZohoResponse;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

final class ZohoHttpClient {
	private static final int MAX_ERROR_BODY_SNIPPET = 500;
	private static final String USER_AGENT = "zoho-payments-java-sdk/1.0.0";
	private final HttpClientInterface transport;
	private final TokenManager tokenManager;
	private final Edition edition;
	private final String accountId;
	private final Duration requestTimeout;
	private final Map<String, String> defaultHeaders;
	private final AtomicBoolean closed = new AtomicBoolean(false);

	ZohoHttpClient(HttpClientInterface transport, TokenManager tokenManager, Edition edition, String accountId,
			Duration requestTimeout, Map<String, String> defaultHeaders) {
		this.transport = transport;
		this.tokenManager = tokenManager;
		this.edition = edition;
		this.accountId = accountId;
		this.requestTimeout = requestTimeout;
		this.defaultHeaders = defaultHeaders != null ? Collections.unmodifiableMap(new LinkedHashMap(defaultHeaders))
				: Collections.emptyMap();
	}

	ApiResponse request(RequestMethod method, String path, QueryParams queryParams, JsonObject body) {
		if (this.closed.get()) {
			throw new IllegalStateException("ZohoPaymentsClient is closed ��� no further API calls may be made");
		} else {
			QueryParams merged = new QueryParams();
			if (queryParams != null) {
				merged.addAll(queryParams);
			}

			merged.add("account_id", this.accountId);
			String var10000 = this.edition.getBaseUrl();
			String url = var10000 + path;
			String qs = merged.toQueryString();
			if (!qs.isEmpty()) {
				url = url + "?" + qs;
			}

			ZohoRequest.Builder reqBuilder = ZohoRequest.builder().method(method).url(url).timeout(this.requestTimeout);

			for (Map.Entry<String, String> h : this.defaultHeaders.entrySet()) {
				reqBuilder.header((String) h.getKey(), (String) h.getValue());
			}

			reqBuilder.setHeader("User-Agent", "zoho-payments-java-sdk/1.0.0");
			reqBuilder.setHeader("Authorization", "Zoho-oauthtoken " + this.tokenManager.getAccessToken());
			reqBuilder.setHeader("Accept", "application/json");
			if (body != null) {
				reqBuilder.setHeader("Content-Type", "application/json");
				reqBuilder.body(body.toString());
			}

			ZohoRequest request = reqBuilder.build();
			ZohoResponse rawResponse = this.transport.execute(request);
			return this.parseAndValidate(rawResponse, method, path);
		}
	}

	private ApiResponse parseAndValidate(ZohoResponse rawResponse, RequestMethod method, String path) {
		String rawBody = rawResponse.getBody();
		JsonObject responseBody;
		if (rawBody != null && !rawBody.isBlank()) {
			try {
				responseBody = JsonUtil.parseObject(rawBody);
				if (responseBody == null) {
					responseBody = new JsonObject();
				}
			} catch (JsonSyntaxException e) {
				throw new ZohoPaymentsException("Unexpected non-JSON response body [HTTP " + rawResponse.getStatusCode()
						+ " " + String.valueOf(method) + "]. Raw body: " + truncateForMessage(rawBody), e);
			}
		} else {
			responseBody = new JsonObject();
		}

		ApiResponse apiResponse = new ApiResponse(rawResponse.getStatusCode(), responseBody);
		if (!apiResponse.isSuccess()) {
			this.throwTypedException(rawResponse.getStatusCode(), apiResponse.getCodeString(),
					apiResponse.getMessage());
		}

		return apiResponse;
	}

	private void throwTypedException(int statusCode, String codeString, String message) {
		switch (statusCode) {
		case 400:
		case 422:
			throw new InvalidRequestException(statusCode, codeString, message);
		case 401:
			throw new AuthenticationException(codeString, message);
		case 403:
			throw new PermissionException(codeString, message);
		case 404:
			throw new ResourceNotFoundException(codeString, message);
		case 429:
			throw new RateLimitException(codeString, message);
		default:
			throw new ZohoPaymentsAPIException(statusCode, codeString, message);
		}
	}

	void close() {
		if (this.closed.compareAndSet(false, true)) {
			this.transport.close();
		}

	}

	ApiResponse get(String path) {
		return this.request(RequestMethod.GET, path, (QueryParams) null, (JsonObject) null);
	}

	ApiResponse get(String path, QueryParams params) {
		return this.request(RequestMethod.GET, path, params, (JsonObject) null);
	}

	ApiResponse post(String path, JsonObject body) {
		return this.request(RequestMethod.POST, path, (QueryParams) null, body);
	}

	ApiResponse put(String path, JsonObject body) {
		return this.request(RequestMethod.PUT, path, (QueryParams) null, body);
	}

	ApiResponse put(String path) {
		return this.request(RequestMethod.PUT, path, (QueryParams) null, (JsonObject) null);
	}

	ApiResponse delete(String path) {
		return this.request(RequestMethod.DELETE, path, (QueryParams) null, (JsonObject) null);
	}

	<T> T get(String path, Class<T> type, String... envelopeKeys) {
		return (T) JsonUtil.unwrap(this.get(path), type, envelopeKeys);
	}

	<T> T post(String path, JsonObject body, Class<T> type, String... envelopeKeys) {
		return (T) JsonUtil.unwrap(this.post(path, body), type, envelopeKeys);
	}

	<T> T put(String path, JsonObject body, Class<T> type, String... envelopeKeys) {
		return (T) JsonUtil.unwrap(this.put(path, body), type, envelopeKeys);
	}

	<T> T put(String path, Class<T> type, String... envelopeKeys) {
		return (T) JsonUtil.unwrap(this.put(path), type, envelopeKeys);
	}

	<T> ListResponse<T> list(String path, QueryParams queryParams, Type listType, String... envelopeKeys) {
		JsonObject body = this.get(path, queryParams).getBody();
		List<T> items = JsonUtil.<T>listFromJson(body, listType, envelopeKeys);
		return new ListResponse<T>(items, JsonUtil.readPageContext(body));
	}

	static String encodePath(String segment) {
		return URLEncoder.encode(segment, StandardCharsets.UTF_8).replace("+", "%20");
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
