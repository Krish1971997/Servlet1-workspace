package com.zohopayments;

import com.google.gson.JsonObject;
import com.zohopayments.model.paymentmethod.PaymentMethodSession;
import com.zohopayments.param.paymentmethod.PaymentMethodSessionCreateParams;

public final class PaymentMethodSessionService {
   private final ZohoHttpClient client;

   PaymentMethodSessionService(ZohoHttpClient client) {
      this.client = client;
   }

   public PaymentMethodSession create(PaymentMethodSessionCreateParams params) {
      if (params == null) {
         throw new IllegalArgumentException("params is required");
      } else {
         JsonObject body = JsonUtil.gson().toJsonTree(params).getAsJsonObject();
         return (PaymentMethodSession)this.client.post("/paymentmethodsessions", body, PaymentMethodSession.class, "payment_method_session");
      }
   }

   public PaymentMethodSession get(String paymentMethodSessionId) {
      if (paymentMethodSessionId != null && !paymentMethodSessionId.isEmpty()) {
         return (PaymentMethodSession)this.client.get("/paymentmethodsessions/" + ZohoHttpClient.encodePath(paymentMethodSessionId), PaymentMethodSession.class, "payment_method_session");
      } else {
         throw new IllegalArgumentException("paymentMethodSessionId is required");
      }
   }
}
