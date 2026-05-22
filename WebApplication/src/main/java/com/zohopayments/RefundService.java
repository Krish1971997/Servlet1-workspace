package com.zohopayments;

import com.google.gson.JsonObject;
import com.zohopayments.model.refund.Refund;
import com.zohopayments.param.refund.RefundCreateParams;

public final class RefundService {
   private final ZohoHttpClient client;

   RefundService(ZohoHttpClient client) {
      this.client = client;
   }

   public Refund create(String paymentId, RefundCreateParams params) {
      if (paymentId != null && !paymentId.isEmpty()) {
         if (params == null) {
            throw new IllegalArgumentException("params is required");
         } else {
            JsonObject body = JsonUtil.gson().toJsonTree(params).getAsJsonObject();
            String path = "/payments/" + ZohoHttpClient.encodePath(paymentId) + "/refunds";
            return (Refund)this.client.post(path, body, Refund.class, "refund");
         }
      } else {
         throw new IllegalArgumentException("paymentId is required");
      }
   }

   public Refund get(String refundId) {
      if (refundId != null && !refundId.isEmpty()) {
         return (Refund)this.client.get("/refunds/" + ZohoHttpClient.encodePath(refundId), Refund.class, "refund");
      } else {
         throw new IllegalArgumentException("refundId is required");
      }
   }
}
