package com.zohopayments.model.mandate;

public final class MandatePayment {
   private String paymentsSessionId;
   private String invoiceNumber;
   private String customerId;
   private String amount;
   private String currency;
   private String status;
   private String statementDescriptor;
   private String description;
   private String referenceNumber;
   private Long date;
   private PaymentMethod paymentMethod;

   MandatePayment() {
   }

   public String getPaymentsSessionId() {
      return this.paymentsSessionId;
   }

   public String getInvoiceNumber() {
      return this.invoiceNumber;
   }

   public String getCustomerId() {
      return this.customerId;
   }

   public String getAmount() {
      return this.amount;
   }

   public String getCurrency() {
      return this.currency;
   }

   public String getStatus() {
      return this.status;
   }

   public String getStatementDescriptor() {
      return this.statementDescriptor;
   }

   public String getDescription() {
      return this.description;
   }

   public String getReferenceNumber() {
      return this.referenceNumber;
   }

   public Long getDate() {
      return this.date;
   }

   public PaymentMethod getPaymentMethod() {
      return this.paymentMethod;
   }

   public static final class PaymentMethod {
      private String type;

      PaymentMethod() {
      }

      public String getType() {
         return this.type;
      }
   }
}
