package com.zohopayments.model.paymentmethod;

public final class AddressDetail {
   private String name;
   private String addressId;
   private String addressLine1;
   private String addressLine2;
   private String city;
   private String state;
   private String postalCode;
   private String country;

   AddressDetail() {
   }

   public String getName() {
      return this.name;
   }

   public String getAddressId() {
      return this.addressId;
   }

   public String getAddressLine1() {
      return this.addressLine1;
   }

   public String getAddressLine2() {
      return this.addressLine2;
   }

   public String getCity() {
      return this.city;
   }

   public String getState() {
      return this.state;
   }

   public String getPostalCode() {
      return this.postalCode;
   }

   public String getCountry() {
      return this.country;
   }
}
