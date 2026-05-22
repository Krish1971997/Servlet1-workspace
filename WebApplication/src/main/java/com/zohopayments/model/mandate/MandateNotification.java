package com.zohopayments.model.mandate;

public final class MandateNotification {
   private String mandateId;
   private String mandateNotificationId;
   private String customerId;
   private String mandateAmount;
   private String currency;
   private String amountRule;
   private String notificationAmount;
   private String notificationStatus;
   private String description;
   private String invoiceNumber;
   private Long notificationDate;
   private Long executionDate;
   private String amount;
   private MandatePaymentMethod paymentMethod;

   MandateNotification() {
   }

   public String getMandateId() {
      return this.mandateId;
   }

   public String getMandateNotificationId() {
      return this.mandateNotificationId;
   }

   public String getCustomerId() {
      return this.customerId;
   }

   public String getMandateAmount() {
      return this.mandateAmount;
   }

   public String getCurrency() {
      return this.currency;
   }

   public String getAmountRule() {
      return this.amountRule;
   }

   public String getNotificationAmount() {
      return this.notificationAmount;
   }

   public String getNotificationStatus() {
      return this.notificationStatus;
   }

   public String getDescription() {
      return this.description;
   }

   public String getInvoiceNumber() {
      return this.invoiceNumber;
   }

   public Long getNotificationDate() {
      return this.notificationDate;
   }

   public Long getExecutionDate() {
      return this.executionDate;
   }

   public String getAmount() {
      return this.amount;
   }

   public MandatePaymentMethod getPaymentMethod() {
      return this.paymentMethod;
   }
}
