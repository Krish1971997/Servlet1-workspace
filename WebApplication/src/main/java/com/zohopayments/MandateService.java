package com.zohopayments;

import com.google.gson.JsonObject;
import com.zohopayments.model.mandate.Mandate;
import com.zohopayments.model.mandate.MandateNotification;
import com.zohopayments.model.mandate.MandatePayment;
import com.zohopayments.model.paymentsession.PaymentSession;
import com.zohopayments.param.mandate.MandateEnrollmentSessionCreateParams;
import com.zohopayments.param.mandate.MandateExecuteParams;
import com.zohopayments.param.mandate.MandateExecutionSessionCreateParams;
import com.zohopayments.param.mandate.MandateNotifyParams;

public final class MandateService {
   private final ZohoHttpClient client;

   MandateService(ZohoHttpClient client) {
      this.client = client;
   }

   public PaymentSession createEnrollmentSession(MandateEnrollmentSessionCreateParams params) {
      if (params == null) {
         throw new IllegalArgumentException("params is required");
      } else {
         JsonObject body = JsonUtil.gson().toJsonTree(params).getAsJsonObject();
         return (PaymentSession)this.client.post("/paymentsessions", body, PaymentSession.class, "payments_session");
      }
   }

   public PaymentSession createExecutionSession(MandateExecutionSessionCreateParams params) {
      if (params == null) {
         throw new IllegalArgumentException("params is required");
      } else {
         JsonObject body = JsonUtil.gson().toJsonTree(params).getAsJsonObject();
         return (PaymentSession)this.client.post("/paymentsessions", body, PaymentSession.class, "payments_session");
      }
   }

   public MandateNotification sendNotification(MandateNotifyParams params) {
      if (params == null) {
         throw new IllegalArgumentException("params is required");
      } else {
         JsonObject body = JsonUtil.gson().toJsonTree(params).getAsJsonObject();
         return (MandateNotification)this.client.post("/mandates/notify", body, MandateNotification.class, "mandate_notification");
      }
   }

   public MandatePayment execute(MandateExecuteParams params) {
      if (params == null) {
         throw new IllegalArgumentException("params is required");
      } else {
         JsonObject body = JsonUtil.gson().toJsonTree(params).getAsJsonObject();
         return (MandatePayment)this.client.post("/mandates/execute", body, MandatePayment.class, "payment");
      }
   }

   public MandateNotification getNotification(String mandateNotificationId) {
      if (mandateNotificationId != null && !mandateNotificationId.isEmpty()) {
         return (MandateNotification)this.client.get("/mandates/notifications/" + ZohoHttpClient.encodePath(mandateNotificationId), MandateNotification.class, "mandate_notification");
      } else {
         throw new IllegalArgumentException("mandateNotificationId is required");
      }
   }

   public Mandate get(String mandateId) {
      if (mandateId != null && !mandateId.isEmpty()) {
         return (Mandate)this.client.get("/mandates/" + ZohoHttpClient.encodePath(mandateId), Mandate.class, "mandate");
      } else {
         throw new IllegalArgumentException("mandateId is required");
      }
   }
}
