package com.zohopayments;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.zohopayments.model.ListResponse;
import com.zohopayments.model.virtualaccount.VirtualAccount;
import com.zohopayments.model.virtualaccount.VirtualAccountPayment;
import com.zohopayments.param.virtualaccount.VirtualAccountCreateParams;
import com.zohopayments.param.virtualaccount.VirtualAccountPaymentListParams;
import com.zohopayments.param.virtualaccount.VirtualAccountUpdateParams;
import java.lang.reflect.Type;
import java.util.List;

public final class CollectService {
	private static final Type PAYMENT_LIST_TYPE = (new TypeToken<List<VirtualAccountPayment>>() {
	}).getType();
	private final ZohoHttpClient client;

	CollectService(ZohoHttpClient client) {
		this.client = client;
	}

	public VirtualAccount create(VirtualAccountCreateParams params) {
		if (params == null) {
			throw new IllegalArgumentException("params is required");
		} else {
			JsonObject body = JsonUtil.gson().toJsonTree(params).getAsJsonObject();
			return (VirtualAccount) this.client.post("/virtualaccounts", body, VirtualAccount.class, "virtual_account");
		}
	}

	public VirtualAccount update(String virtualAccountId, VirtualAccountUpdateParams params) {
		if (virtualAccountId != null && !virtualAccountId.isEmpty()) {
			if (params == null) {
				throw new IllegalArgumentException("params is required");
			} else {
				JsonObject body = JsonUtil.gson().toJsonTree(params).getAsJsonObject();
				return (VirtualAccount) this.client.put(
						"/virtualaccounts/" + ZohoHttpClient.encodePath(virtualAccountId), body, VirtualAccount.class,
						"virtual_account");
			}
		} else {
			throw new IllegalArgumentException("virtualAccountId is required");
		}
	}

	public VirtualAccount get(String virtualAccountId) {
		if (virtualAccountId != null && !virtualAccountId.isEmpty()) {
			return (VirtualAccount) this.client.get("/virtualaccounts/" + ZohoHttpClient.encodePath(virtualAccountId),
					VirtualAccount.class, "virtual_account");
		} else {
			throw new IllegalArgumentException("virtualAccountId is required");
		}
	}

	public ListResponse<VirtualAccountPayment> listPayments(String virtualAccountId) {
		return this.listPayments(virtualAccountId, (VirtualAccountPaymentListParams) null);
	}

	public ListResponse<VirtualAccountPayment> listPayments(String virtualAccountId,
			VirtualAccountPaymentListParams params) {
		if (virtualAccountId != null && !virtualAccountId.isEmpty()) {
			QueryParams q = null;
			if (params != null) {
				q = (new QueryParams()).add("status", params.getStatus()).add("per_page", params.getPerPage())
						.add("page", params.getPage()).add("sort_column", params.getSortColumn())
						.add("sort_order", params.getSortOrder());
			}

			String path = "/virtualaccounts/" + ZohoHttpClient.encodePath(virtualAccountId) + "/payments";
			return this.client.<VirtualAccountPayment>list(path, q, PAYMENT_LIST_TYPE, "payments");
		} else {
			throw new IllegalArgumentException("virtualAccountId is required");
		}
	}

	public void close(String virtualAccountId) {
		if (virtualAccountId != null && !virtualAccountId.isEmpty()) {
			this.client.put("/virtualaccounts/" + ZohoHttpClient.encodePath(virtualAccountId) + "/close");
		} else {
			throw new IllegalArgumentException("virtualAccountId is required");
		}
	}
}
