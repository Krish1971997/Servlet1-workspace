package com.zohopayments.model.mandate;

public final class Mandate {
   private String mandateId;
   private String customerId;
   private String customerName;
   private String customerEmail;
   private String customerPhone;
   private String amount;
   private String currency;
   private String amountRule;
   private String frequency;
   private Integer debitDay;
   private String debitRule;
   private Long startDate;
   private Long endDate;
   private String status;
   private String description;
   private MandatePaymentMethod paymentMethod;

   Mandate() {
   }

   public String getMandateId() {
      return this.mandateId;
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

   public String getCustomerPhone() {
      return this.customerPhone;
   }

   public String getAmount() {
      return this.amount;
   }

   public String getCurrency() {
      return this.currency;
   }

   public String getAmountRule() {
      return this.amountRule;
   }

   public String getFrequency() {
      return this.frequency;
   }

   public Integer getDebitDay() {
      return this.debitDay;
   }

   public String getDebitRule() {
      return this.debitRule;
   }

   public Long getStartDate() {
      return this.startDate;
   }

   public Long getEndDate() {
      return this.endDate;
   }

   public String getStatus() {
      return this.status;
   }

   public String getDescription() {
      return this.description;
   }

   public MandatePaymentMethod getPaymentMethod() {
      return this.paymentMethod;
   }
}
