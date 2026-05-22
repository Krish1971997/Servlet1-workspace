package com.zohopayments;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.zohopayments.model.ListResponse;
import com.zohopayments.model.payment.Payment;
import com.zohopayments.model.payment.PaymentSummary;
import com.zohopayments.param.payment.PaymentCreateParams;
import com.zohopayments.param.payment.PaymentListParams;
import java.lang.reflect.Type;
import java.util.List;

public final class PaymentService {
   private static final Type PAYMENT_LIST_TYPE = (new TypeToken<List<PaymentSummary>>() {
   }).getType();
   private final ZohoHttpClient client;
   private final Edition edition;

   PaymentService(ZohoHttpClient client, Edition edition) {
      this.client = client;
      this.edition = edition;
   }

   public Payment create(PaymentCreateParams params) {
      if (!this.edition.isUS()) {
         throw new UnsupportedOperationException("Payment.create is only available for Edition.US");
      } else if (params == null) {
         throw new IllegalArgumentException("params is required");
      } else {
         JsonObject body = JsonUtil.gson().toJsonTree(params).getAsJsonObject();
         return (Payment)this.client.post("/payments", body, Payment.class, "payment");
      }
   }

   public Payment get(String paymentId) {
      if (paymentId != null && !paymentId.isEmpty()) {
         return (Payment)this.client.get("/payments/" + ZohoHttpClient.encodePath(paymentId), Payment.class, "payment");
      } else {
         throw new IllegalArgumentException("paymentId is required");
      }
   }

   public ListResponse<PaymentSummary> list() {
      return this.list((PaymentListParams)null);
   }

   public ListResponse<PaymentSummary> list(PaymentListParams params) {
      QueryParams q = null;
      if (params != null) {
         q = (new QueryParams()).add("status", params.getStatus()).add("search_text", params.getSearchText()).add("filter_by", params.getFilterBy()).add("from_date", params.getFromDate()).add("to_date", params.getToDate()).add("payment_method_type", params.getPaymentMethodType()).add("per_page", params.getPerPage()).add("page", params.getPage());
      }

      return this.client.<PaymentSummary>list("/payments", q, PAYMENT_LIST_TYPE, "payments");
   }
}
