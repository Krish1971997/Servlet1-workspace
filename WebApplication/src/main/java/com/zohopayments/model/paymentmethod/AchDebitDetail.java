package com.zohopayments.model.paymentmethod;

public final class AchDebitDetail {
   private String accountHolderName;
   private String lastFourDigits;
   private String accountHolderType;
   private String accountType;
   private String bankName;
   private String routingNumber;

   AchDebitDetail() {
   }

   public String getAccountHolderName() {
      return this.accountHolderName;
   }

   public String getLastFourDigits() {
      return this.lastFourDigits;
   }

   public String getAccountHolderType() {
      return this.accountHolderType;
   }

   public String getAccountType() {
      return this.accountType;
   }

   public String getBankName() {
      return this.bankName;
   }

   public String getRoutingNumber() {
      return this.routingNumber;
   }
}
