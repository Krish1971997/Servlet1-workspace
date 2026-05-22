package com.zohopayments.param;

public final class HostedPageParams {
   private final String description;
   private final String successUrl;
   private final String failureUrl;
   private final String name;
   private final String email;
   private final String phone;
   private final String phoneCountryCode;
   private final String udf1;
   private final String udf2;
   private final String udf3;
   private final String udf4;
   private final String udf5;

   private HostedPageParams(Builder b) {
      this.description = b.description;
      this.successUrl = b.successUrl;
      this.failureUrl = b.failureUrl;
      this.name = b.name;
      this.email = b.email;
      this.phone = b.phone;
      this.phoneCountryCode = b.phoneCountryCode;
      this.udf1 = b.udf1;
      this.udf2 = b.udf2;
      this.udf3 = b.udf3;
      this.udf4 = b.udf4;
      this.udf5 = b.udf5;
   }

   public String getDescription() {
      return this.description;
   }

   public String getSuccessUrl() {
      return this.successUrl;
   }

   public String getFailureUrl() {
      return this.failureUrl;
   }

   public String getName() {
      return this.name;
   }

   public String getEmail() {
      return this.email;
   }

   public String getPhone() {
      return this.phone;
   }

   public String getPhoneCountryCode() {
      return this.phoneCountryCode;
   }

   public String getUdf1() {
      return this.udf1;
   }

   public String getUdf2() {
      return this.udf2;
   }

   public String getUdf3() {
      return this.udf3;
   }

   public String getUdf4() {
      return this.udf4;
   }

   public String getUdf5() {
      return this.udf5;
   }

   public static Builder builder() {
      return new Builder();
   }

   public static final class Builder {
      private String description;
      private String successUrl;
      private String failureUrl;
      private String name;
      private String email;
      private String phone;
      private String phoneCountryCode;
      private String udf1;
      private String udf2;
      private String udf3;
      private String udf4;
      private String udf5;

      private Builder() {
      }

      public Builder description(String description) {
         this.description = description;
         return this;
      }

      public Builder successUrl(String successUrl) {
         this.successUrl = successUrl;
         return this;
      }

      public Builder failureUrl(String failureUrl) {
         this.failureUrl = failureUrl;
         return this;
      }

      public Builder name(String name) {
         this.name = name;
         return this;
      }

      public Builder email(String email) {
         this.email = email;
         return this;
      }

      public Builder phone(String phone) {
         this.phone = phone;
         return this;
      }

      public Builder phoneCountryCode(String phoneCountryCode) {
         this.phoneCountryCode = phoneCountryCode;
         return this;
      }

      public Builder udf1(String udf1) {
         this.udf1 = udf1;
         return this;
      }

      public Builder udf2(String udf2) {
         this.udf2 = udf2;
         return this;
      }

      public Builder udf3(String udf3) {
         this.udf3 = udf3;
         return this;
      }

      public Builder udf4(String udf4) {
         this.udf4 = udf4;
         return this;
      }

      public Builder udf5(String udf5) {
         this.udf5 = udf5;
         return this;
      }

      public HostedPageParams build() {
         if (this.description != null && !this.description.isEmpty()) {
            if (this.successUrl != null && !this.successUrl.isEmpty()) {
               if (this.failureUrl != null && !this.failureUrl.isEmpty()) {
                  return new HostedPageParams(this);
               } else {
                  throw new IllegalArgumentException("failureUrl is required");
               }
            } else {
               throw new IllegalArgumentException("successUrl is required");
            }
         } else {
            throw new IllegalArgumentException("description is required");
         }
      }
   }
}
