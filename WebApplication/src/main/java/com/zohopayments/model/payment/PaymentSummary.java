package com.zohopayments.model.payment;

public final class PaymentSummary {
   private String paymentId;
   private String amount;
   private String currency;
   private String receiptEmail;
   private String referenceNumber;
   private String amountCaptured;
   private String amountRefunded;
   private String feeAmount;
   private String netAmount;
   private String status;
   private Long date;
   private PaymentMethod paymentMethod;

   PaymentSummary() {
   }

   public String getPaymentId() {
      return this.paymentId;
   }

   public String getAmount() {
      return this.amount;
   }

   public String getCurrency() {
      return this.currency;
   }

   public String getReceiptEmail() {
      return this.receiptEmail;
   }

   public String getReferenceNumber() {
      return this.referenceNumber;
   }

   public String getAmountCaptured() {
      return this.amountCaptured;
   }

   public String getAmountRefunded() {
      return this.amountRefunded;
   }

   public String getFeeAmount() {
      return this.feeAmount;
   }

   public String getNetAmount() {
      return this.netAmount;
   }

   public String getStatus() {
      return this.status;
   }

   public Long getDate() {
      return this.date;
   }

   public PaymentMethod getPaymentMethod() {
      return this.paymentMethod;
   }

   public static final class PaymentMethod {
      private String paymentMethodId;
      private String type;

      PaymentMethod() {
      }

      public String getPaymentMethodId() {
         return this.paymentMethodId;
      }

      public String getType() {
         return this.type;
      }
   }
}
