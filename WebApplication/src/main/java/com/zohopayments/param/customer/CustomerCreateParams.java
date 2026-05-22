package com.zohopayments.param.customer;

import com.zohopayments.param.MetaDataParams;
import com.zohopayments.param.MetaDataValidator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CustomerCreateParams {
   private final String name;
   private final String email;
   private final String phone;
   private final String phoneCountryCode;
   private final List<MetaDataParams> metaData;

   private CustomerCreateParams(Builder b) {
      this.name = b.name;
      this.email = b.email;
      this.phone = b.phone;
      this.phoneCountryCode = b.phoneCountryCode;
      this.metaData = b.metaData != null ? Collections.unmodifiableList(new ArrayList(b.metaData)) : null;
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

   public List<MetaDataParams> getMetaData() {
      return this.metaData;
   }

   public static Builder builder() {
      return new Builder();
   }

   public static final class Builder {
      private String name;
      private String email;
      private String phone;
      private String phoneCountryCode;
      private List<MetaDataParams> metaData;

      private Builder() {
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

      public Builder metaData(List<MetaDataParams> metaData) {
         this.metaData = metaData;
         return this;
      }

      public CustomerCreateParams build() {
         if (this.name != null && !this.name.isEmpty()) {
            if (this.email != null && !this.email.isEmpty()) {
               MetaDataValidator.validate(this.metaData);
               return new CustomerCreateParams(this);
            } else {
               throw new IllegalArgumentException("email is required");
            }
         } else {
            throw new IllegalArgumentException("name is required");
         }
      }
   }
}
