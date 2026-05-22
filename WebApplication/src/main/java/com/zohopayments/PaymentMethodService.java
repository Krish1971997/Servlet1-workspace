package com.zohopayments;

import com.google.gson.JsonObject;
import com.zohopayments.model.paymentmethod.PaymentMethod;
import com.zohopayments.param.paymentmethod.PaymentMethodUpdateParams;

public final class PaymentMethodService {
	private final ZohoHttpClient client;

	PaymentMethodService(ZohoHttpClient client) {
		this.client = client;
	}

	public PaymentMethod get(String paymentMethodId) {
		if (paymentMethodId != null && !paymentMethodId.isEmpty()) {
			return (PaymentMethod) this.client.get("/paymentmethods/" + ZohoHttpClient.encodePath(paymentMethodId),
					PaymentMethod.class, "payment_method");
		} else {
			throw new IllegalArgumentException("paymentMethodId is required");
		}
	}

	public PaymentMethod update(String paymentMethodId, PaymentMethodUpdateParams params) {
		if (paymentMethodId != null && !paymentMethodId.isEmpty()) {
			if (params == null) {
				throw new IllegalArgumentException("params is required");
			} else {
				JsonObject body = JsonUtil.gson().toJsonTree(params).getAsJsonObject();
				return (PaymentMethod) this.client.put("/paymentmethods/" + ZohoHttpClient.encodePath(paymentMethodId),
						body, PaymentMethod.class, "payment_method");
			}
		} else {
			throw new IllegalArgumentException("paymentMethodId is required");
		}
	}

	public void delete(String paymentMethodId) {
		if (paymentMethodId != null && !paymentMethodId.isEmpty()) {
			this.client.delete("/paymentmethods/" + ZohoHttpClient.encodePath(paymentMethodId));
		} else {
			throw new IllegalArgumentException("paymentMethodId is required");
		}
	}
}
