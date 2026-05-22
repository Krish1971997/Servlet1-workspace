package com.zohopayments;

import com.google.gson.JsonObject;
import com.zohopayments.model.paymentsession.PaymentSession;
import com.zohopayments.param.paymentsession.PaymentSessionCreateParams;

public final class PaymentSessionService {
   private final ZohoHttpClient client;

   PaymentSessionService(ZohoHttpClient client) {
      this.client = client;
   }

   public PaymentSession create(PaymentSessionCreateParams params) {
      if (params == null) {
         throw new IllegalArgumentException("params is required");
      } else {
         JsonObject body = JsonUtil.gson().toJsonTree(params).getAsJsonObject();
         return (PaymentSession)this.client.post("/paymentsessions", body, PaymentSession.class, "payments_session");
      }
   }

   public PaymentSession get(String paymentSessionId) {
      if (paymentSessionId != null && !paymentSessionId.isEmpty()) {
         return (PaymentSession)this.client.get("/paymentsessions/" + ZohoHttpClient.encodePath(paymentSessionId), PaymentSession.class, "payments_session");
      } else {
         throw new IllegalArgumentException("paymentSessionId is required");
      }
   }
}
