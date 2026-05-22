package com.zohopayments;

import com.google.gson.JsonObject;
import com.zohopayments.model.paymentlink.PaymentLink;
import com.zohopayments.param.paymentlink.PaymentLinkCreateParams;
import com.zohopayments.param.paymentlink.PaymentLinkUpdateParams;

public final class PaymentLinkService {
	private final ZohoHttpClient client;

	PaymentLinkService(ZohoHttpClient client) {
		this.client = client;
	}

	public PaymentLink create(PaymentLinkCreateParams params) {
		if (params == null) {
			throw new IllegalArgumentException("params is required");
		} else {
			JsonObject body = JsonUtil.gson().toJsonTree(params).getAsJsonObject();
			return (PaymentLink) this.client.post("/paymentlinks", body, PaymentLink.class, "payment_links");
		}
	}

	public PaymentLink update(String paymentLinkId, PaymentLinkUpdateParams params) {
		if (paymentLinkId != null && !paymentLinkId.isEmpty()) {
			if (params == null) {
				throw new IllegalArgumentException("params is required");
			} else {
				JsonObject body = JsonUtil.gson().toJsonTree(params).getAsJsonObject();
				return (PaymentLink) this.client.put("/paymentlinks/" + ZohoHttpClient.encodePath(paymentLinkId), body,
						PaymentLink.class, "payment_links");
			}
		} else {
			throw new IllegalArgumentException("paymentLinkId is required");
		}
	}

	public PaymentLink get(String paymentLinkId) {
		if (paymentLinkId != null && !paymentLinkId.isEmpty()) {
			return (PaymentLink) this.client.get("/paymentlinks/" + ZohoHttpClient.encodePath(paymentLinkId),
					PaymentLink.class, "payment_links");
		} else {
			throw new IllegalArgumentException("paymentLinkId is required");
		}
	}

	public PaymentLink cancel(String paymentLinkId) {
		if (paymentLinkId != null && !paymentLinkId.isEmpty()) {
			return (PaymentLink) this.client.put(
					"/paymentlinks/" + ZohoHttpClient.encodePath(paymentLinkId) + "/cancel", PaymentLink.class,
					"payment_links");
		} else {
			throw new IllegalArgumentException("paymentLinkId is required");
		}
	}
}
