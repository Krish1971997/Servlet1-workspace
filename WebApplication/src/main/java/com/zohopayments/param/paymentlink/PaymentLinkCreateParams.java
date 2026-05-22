package com.zohopayments.param.paymentlink;

import com.zohopayments.param.NotifyCustomerParams;
import com.zohopayments.param.ParamValidator;

public final class PaymentLinkCreateParams {
   private final Double amount;
   private final String currency;
   private final String description;
   private final String email;
   private final String phone;
   private final String phoneCountryCode;
   private final String expiresAt;
   private final String referenceId;
   private final String returnUrl;
   private final NotifyCustomerParams notifyCustomer;
   private final PaymentLinkConfigurationsParams configurations;

   private PaymentLinkCreateParams(Builder b) {
      this.amount = b.amount;
      this.currency = b.currency;
      this.description = b.description;
      this.email = b.email;
      this.phone = b.phone;
      this.phoneCountryCode = b.phoneCountryCode;
      this.expiresAt = b.expiresAt;
      this.referenceId = b.referenceId;
      this.returnUrl = b.returnUrl;
      this.notifyCustomer = b.notifyCustomer;
      this.configurations = b.configurations;
   }

   public Double getAmount() {
      return this.amount;
   }

   public String getCurrency() {
      return this.currency;
   }

   public String getDescription() {
      return this.description;
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

   public String getExpiresAt() {
      return this.expiresAt;
   }

   public String getReferenceId() {
      return this.referenceId;
   }

   public String getReturnUrl() {
      return this.returnUrl;
   }

   public NotifyCustomerParams getNotifyCustomer() {
      return this.notifyCustomer;
   }

   public PaymentLinkConfigurationsParams getConfigurations() {
      return this.configurations;
   }

   public static Builder builder() {
      return new Builder();
   }

   public static final class Builder {
      private Double amount;
      private String currency;
      private String description;
      private String email;
      private String phone;
      private String phoneCountryCode;
      private String expiresAt;
      private String referenceId;
      private String returnUrl;
      private NotifyCustomerParams notifyCustomer;
      private PaymentLinkConfigurationsParams configurations;

      private Builder() {
      }

      public Builder amount(Double amount) {
         this.amount = amount;
         return this;
      }

      public Builder currency(String currency) {
         this.currency = currency;
         return this;
      }

      public Builder description(String description) {
         this.description = description;
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

      public Builder expiresAt(String expiresAt) {
         this.expiresAt = expiresAt;
         return this;
      }

      public Builder referenceId(String referenceId) {
         this.referenceId = referenceId;
         return this;
      }

      public Builder returnUrl(String returnUrl) {
         this.returnUrl = returnUrl;
         return this;
      }

      public Builder notifyCustomer(NotifyCustomerParams notifyCustomer) {
         this.notifyCustomer = notifyCustomer;
         return this;
      }

      public Builder configurations(PaymentLinkConfigurationsParams configurations) {
         this.configurations = configurations;
         return this;
      }

      public PaymentLinkCreateParams build() {
         if (this.amount == null) {
            throw new IllegalArgumentException("amount is required");
         } else if (this.currency != null && !this.currency.isEmpty()) {
            if (this.description != null && !this.description.isEmpty()) {
               ParamValidator.validateDescription(this.description);
               ParamValidator.validateReferenceNumber(this.referenceId);
               return new PaymentLinkCreateParams(this);
            } else {
               throw new IllegalArgumentException("description is required");
            }
         } else {
            throw new IllegalArgumentException("currency is required");
         }
      }
   }
}
