package com.zohopayments.model.paymentmethod;

public final class PaymentMethodSession {
   private String paymentMethodSessionId;
   private String customerId;
   private String description;
   private Long createdTime;
   private String status;
   private PaymentMethodSessionDetail paymentMethod;

   PaymentMethodSession() {
   }

   public String getPaymentMethodSessionId() {
      return this.paymentMethodSessionId;
   }

   public String getCustomerId() {
      return this.customerId;
   }

   public String getDescription() {
      return this.description;
   }

   public Long getCreatedTime() {
      return this.createdTime;
   }

   public String getStatus() {
      return this.status;
   }

   public PaymentMethodSessionDetail getPaymentMethod() {
      return this.paymentMethod;
   }
}
