package com.zohopayments.net;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class ZohoRequest {
	private final RequestMethod method;
	private final String url;
	private final Map<String, List<String>> headers;
	private final String body;
	private final Duration timeout;

	private ZohoRequest(RequestMethod method, String url, Map<String, List<String>> headers, String body,
			Duration timeout) {
		this.method = method;
		this.url = url;
		this.headers = deepUnmodifiable(headers);
		this.body = body;
		this.timeout = timeout;
	}

	public RequestMethod getMethod() {
		return this.method;
	}

	public String getUrl() {
		return this.url;
	}

	public Map<String, List<String>> getHeaders() {
		return this.headers;
	}

	public String getBody() {
		return this.body;
	}

	public Duration getTimeout() {
		return this.timeout;
	}

	public static Builder builder() {
		return new Builder();
	}

	private static Map<String, List<String>> deepUnmodifiable(Map<String, List<String>> src) {
		Map<String, List<String>> safe = new LinkedHashMap(src.size());

		for (Map.Entry<String, List<String>> e : src.entrySet()) {
			safe.put((String) e.getKey(), Collections.unmodifiableList(new ArrayList((Collection) e.getValue())));
		}

		return Collections.unmodifiableMap(safe);
	}

	public static final class Builder {
		private RequestMethod method;
		private String url;
		private final Map<String, List<String>> headers = new LinkedHashMap();
		private String body;
		private Duration timeout;

		private Builder() {
		}

		public Builder method(RequestMethod method) {
			this.method = method;
			return this;
		}

		public Builder url(String url) {
			this.url = url;
			return this;
		}

		public Builder header(String name, String value) {
			((List) this.headers.computeIfAbsent(name, (k) -> new ArrayList())).add(value);
			return this;
		}

		public Builder setHeader(String name, String value) {
			List<String> list = new ArrayList();
			list.add(value);
			this.headers.put(name, list);
			return this;
		}

		public Builder headers(Map<String, String> headers) {
			headers.forEach(this::header);
			return this;
		}

		public Builder body(String body) {
			this.body = body;
			return this;
		}

		public Builder timeout(Duration timeout) {
			this.timeout = timeout;
			return this;
		}

		public ZohoRequest build() {
			Objects.requireNonNull(this.method, "method is required");
			Objects.requireNonNull(this.url, "url is required");
			return new ZohoRequest(this.method, this.url, this.headers, this.body, this.timeout);
		}
	}
}
