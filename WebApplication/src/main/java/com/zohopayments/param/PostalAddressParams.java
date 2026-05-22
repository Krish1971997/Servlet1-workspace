package com.zohopayments.param;

public final class PostalAddressParams {
   private final String name;
   private final String addressLine1;
   private final String addressLine2;
   private final String city;
   private final String state;
   private final String country;
   private final String postalCode;

   private PostalAddressParams(Builder b) {
      this.name = b.name;
      this.addressLine1 = b.addressLine1;
      this.addressLine2 = b.addressLine2;
      this.city = b.city;
      this.state = b.state;
      this.country = b.country;
      this.postalCode = b.postalCode;
   }

   public String getName() {
      return this.name;
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

   public String getCountry() {
      return this.country;
   }

   public String getPostalCode() {
      return this.postalCode;
   }

   public static Builder builder() {
      return new Builder();
   }

   public static final class Builder {
      private String name;
      private String addressLine1;
      private String addressLine2;
      private String city;
      private String state;
      private String country;
      private String postalCode;

      private Builder() {
      }

      public Builder name(String name) {
         this.name = name;
         return this;
      }

      public Builder addressLine1(String addressLine1) {
         this.addressLine1 = addressLine1;
         return this;
      }

      public Builder addressLine2(String addressLine2) {
         this.addressLine2 = addressLine2;
         return this;
      }

      public Builder city(String city) {
         this.city = city;
         return this;
      }

      public Builder state(String state) {
         this.state = state;
         return this;
      }

      public Builder country(String country) {
         this.country = country;
         return this;
      }

      public Builder postalCode(String postalCode) {
         this.postalCode = postalCode;
         return this;
      }

      public PostalAddressParams build() {
         if (this.country != null && !this.country.isEmpty()) {
            return new PostalAddressParams(this);
         } else {
            throw new IllegalArgumentException("country is required for address");
         }
      }
   }
}
