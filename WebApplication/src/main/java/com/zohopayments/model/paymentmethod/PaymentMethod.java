package com.zohopayments.model.paymentmethod;

public final class PaymentMethod {
   private String paymentMethodId;
   private String customerId;
   private String customerName;
   private String customerEmail;
   private String type;
   private String status;
   private String currency;
   private String source;
   private Long createdTime;
   private Long lastModifiedTime;
   private SavedCardDetail card;
   private AchDebitDetail achDebit;
   private AddressDetail billingAddress;

   PaymentMethod() {
   }

   public String getPaymentMethodId() {
      return this.paymentMethodId;
   }

   public String getCustomerId() {
      return this.customerId;
   }

   public String getCustomerName() {
      return this.customerName;
   }

   public String getCustomerEmail() {
      return this.customerEmail;
   }

   public String getType() {
      return this.type;
   }

   public String getStatus() {
      return this.status;
   }

   public String getCurrency() {
      return this.currency;
   }

   public String getSource() {
      return this.source;
   }

   public Long getCreatedTime() {
      return this.createdTime;
   }

   public Long getLastModifiedTime() {
      return this.lastModifiedTime;
   }

   public SavedCardDetail getCard() {
      return this.card;
   }

   public AchDebitDetail getAchDebit() {
      return this.achDebit;
   }

   public AddressDetail getBillingAddress() {
      return this.billingAddress;
   }
}
