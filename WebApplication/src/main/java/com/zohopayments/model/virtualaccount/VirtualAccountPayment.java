package com.zohopayments.model.virtualaccount;

public final class VirtualAccountPayment {
   private String paymentId;
   private String customerId;
   private String virtualAccountId;
   private String customerName;
   private String customerEmail;
   private String amount;
   private String receiptEmail;
   private String dialingCode;
   private String phone;
   private String referenceNumber;
   private String transactionReferenceNumber;
   private String paymentType;
   private String currency;
   private String balance;
   private String amountCaptured;
   private String amountRefunded;
   private String feeAmount;
   private String status;
   private String transactionType;
   private String fraudAlert;
   private String failureReason;
   private String failureCategory;
   private String nextAction;
   private String tip;
   private Long date;
   private PaymentMethod paymentMethod;

   VirtualAccountPayment() {
   }

   public String getPaymentId() {
      return this.paymentId;
   }

   public String getCustomerId() {
      return this.customerId;
   }

   public String getVirtualAccountId() {
      return this.virtualAccountId;
   }

   public String getCustomerName() {
      return this.customerName;
   }

   public String getCustomerEmail() {
      return this.customerEmail;
   }

   public String getAmount() {
      return this.amount;
   }

   public String getReceiptEmail() {
      return this.receiptEmail;
   }

   public String getDialingCode() {
      return this.dialingCode;
   }

   public String getPhone() {
      return this.phone;
   }

   public String getReferenceNumber() {
      return this.referenceNumber;
   }

   public String getTransactionReferenceNumber() {
      return this.transactionReferenceNumber;
   }

   public String getPaymentType() {
      return this.paymentType;
   }

   public String getCurrency() {
      return this.currency;
   }

   public String getBalance() {
      return this.balance;
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

   public String getStatus() {
      return this.status;
   }

   public String getTransactionType() {
      return this.transactionType;
   }

   public String getFraudAlert() {
      return this.fraudAlert;
   }

   public String getFailureReason() {
      return this.failureReason;
   }

   public String getFailureCategory() {
      return this.failureCategory;
   }

   public String getNextAction() {
      return this.nextAction;
   }

   public String getTip() {
      return this.tip;
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
