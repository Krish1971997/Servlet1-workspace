package com.zohopayments.model.paymentmethod;

public final class SavedCardDetail {
   private String cardHolderName;
   private String lastFourDigits;
   private String expiryMonth;
   private String expiryYear;
   private String brand;
   private String funding;
   private String country;
   private CardChecks cardChecks;

   SavedCardDetail() {
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

   public String getCountry() {
      return this.country;
   }

   public CardChecks getCardChecks() {
      return this.cardChecks;
   }

   public static final class CardChecks {
      private String addressLineCheck;
      private String postalCodeCheck;
      private String cvcCheck;

      CardChecks() {
      }

      public String getAddressLineCheck() {
         return this.addressLineCheck;
      }

      public String getPostalCodeCheck() {
         return this.postalCodeCheck;
      }

      public String getCvcCheck() {
         return this.cvcCheck;
      }
   }
}
