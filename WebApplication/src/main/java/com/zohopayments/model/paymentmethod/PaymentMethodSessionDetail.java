package com.zohopayments.model.paymentmethod;

public final class PaymentMethodSessionDetail {
   private String paymentMethodId;
   private String status;
   private Long createdTime;
   private String type;

   PaymentMethodSessionDetail() {
   }

   public String getPaymentMethodId() {
      return this.paymentMethodId;
   }

   public String getStatus() {
      return this.status;
   }

   public Long getCreatedTime() {
      return this.createdTime;
   }

   public String getType() {
      return this.type;
   }
}
