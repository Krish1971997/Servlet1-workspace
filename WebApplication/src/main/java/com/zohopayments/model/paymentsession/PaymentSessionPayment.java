package com.zohopayments.model.paymentsession;

public final class PaymentSessionPayment {
   private String paymentId;
   private String status;
   private Long createdTime;

   PaymentSessionPayment() {
   }

   public String getPaymentId() {
      return this.paymentId;
   }

   public String getStatus() {
      return this.status;
   }

   public Long getCreatedTime() {
      return this.createdTime;
   }
}
