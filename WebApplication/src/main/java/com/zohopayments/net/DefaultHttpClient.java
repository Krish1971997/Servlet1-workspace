package com.zohopayments.net;

import com.zohopayments.exception.ConnectionException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class DefaultHttpClient implements HttpClientInterface {
	private final HttpClient httpClient;
	private final Duration defaultTimeout;

	public DefaultHttpClient(Duration connectTimeout, Duration defaultTimeout) {
		this.defaultTimeout = defaultTimeout;
		this.httpClient = HttpClient.newBuilder().connectTimeout(connectTimeout).build();
	}

	public ZohoResponse execute(ZohoRequest request) {
		Duration effectiveTimeout = request.getTimeout() != null ? request.getTimeout() : this.defaultTimeout;
		HttpRequest.Builder builder = HttpRequest.newBuilder().uri(URI.create(request.getUrl()))
				.timeout(effectiveTimeout);

		for (Map.Entry<String, List<String>> h : request.getHeaders().entrySet()) {
			for (String value : h.getValue()) {
				builder.header((String) h.getKey(), value);
			}
		}

		switch (request.getMethod()) {
		case GET -> builder.GET();
		case POST -> builder.POST(this.bodyPublisher(request.getBody()));
		case PUT -> builder.PUT(this.bodyPublisher(request.getBody()));
		case DELETE -> builder.DELETE();
		default ->
			throw new UnsupportedOperationException("Unsupported HTTP method: " + String.valueOf(request.getMethod()));
		}

		try {
			HttpResponse<String> response = this.httpClient.send(builder.build(), BodyHandlers.ofString());
			Map<String, List<String>> responseHeaders = new LinkedHashMap();
			response.headers().map().forEach((k, v) -> {
				if (v != null && !v.isEmpty()) {
					responseHeaders.put(k, v);
				}

			});
			return new ZohoResponse(response.statusCode(), responseHeaders, (String) response.body());
		} catch (IOException e) {
			throw new ConnectionException("HTTP request failed: " + String.valueOf(request.getMethod()), e);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new ConnectionException("HTTP request interrupted: " + String.valueOf(request.getMethod()), e);
		}
	}

	public void close() {
		if (this.httpClient instanceof AutoCloseable) {
			try {
				((AutoCloseable) this.httpClient).close();
			} catch (InterruptedException var2) {
				Thread.currentThread().interrupt();
			} catch (Exception var3) {
			}
		}

	}

	private HttpRequest.BodyPublisher bodyPublisher(String body) {
		return body != null && !body.isEmpty() ? BodyPublishers.ofString(body) : BodyPublishers.noBody();
	}
}
