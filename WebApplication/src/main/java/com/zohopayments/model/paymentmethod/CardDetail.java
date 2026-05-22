package com.zohopayments.model.paymentmethod;

public final class CardDetail {
   private String cardHolderName;
   private String lastFourDigits;
   private String expiryMonth;
   private String expiryYear;
   private String brand;
   private String funding;

   CardDetail() {
   }

   public String getCardHolderName() {
      return this.cardHolderName;
   }

   public String getLastFourDigits() {
      return this.lastFourDigits;
   }

   public String getExpiryMonth() {
      return this.expiryMonth;
   }

   public String getExpiryYear() {
      return this.expiryYear;
   }

   public String getBrand() {
      return this.brand;
   }

   public String getFunding() {
      return this.funding;
   }
}
