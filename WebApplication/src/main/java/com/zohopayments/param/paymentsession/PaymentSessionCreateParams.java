package com.zohopayments.param.paymentsession;

import com.zohopayments.param.ConfigurationsParams;
import com.zohopayments.param.MetaDataParams;
import com.zohopayments.param.MetaDataValidator;
import com.zohopayments.param.ParamValidator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PaymentSessionCreateParams {
   private final Double amount;
   private final String currency;
   private final Integer expiresIn;
   private final List<MetaDataParams> metaData;
   private final String description;
   private final String invoiceNumber;
   private final String referenceNumber;
   private final Integer maxRetryCount;
   private final ConfigurationsParams configurations;

   private PaymentSessionCreateParams(Builder b) {
      this.amount = b.amount;
      this.currency = b.currency;
      this.expiresIn = b.expiresIn;
      this.metaData = b.metaData != null ? Collections.unmodifiableList(new ArrayList(b.metaData)) : null;
      this.description = b.description;
      this.invoiceNumber = b.invoiceNumber;
      this.referenceNumber = b.referenceNumber;
      this.maxRetryCount = b.maxRetryCount;
      this.configurations = b.configurations;
   }

   public Double getAmount() {
      return this.amount;
   }

   public String getCurrency() {
      return this.currency;
   }

   public Integer getExpiresIn() {
      return this.expiresIn;
   }

   public List<MetaDataParams> getMetaData() {
      return this.metaData;
   }

   public String getDescription() {
      return this.description;
   }

   public String getInvoiceNumber() {
      return this.invoiceNumber;
   }

   public String getReferenceNumber() {
      return this.referenceNumber;
   }

   public Integer getMaxRetryCount() {
      return this.maxRetryCount;
   }

   public ConfigurationsParams getConfigurations() {
      return this.configurations;
   }

   public static Builder builder() {
      return new Builder();
   }

   public static final class Builder {
      private Double amount;
      private String currency;
      private Integer expiresIn;
      private List<MetaDataParams> metaData;
      private String description;
      private String invoiceNumber;
      private String referenceNumber;
      private Integer maxRetryCount;
      private ConfigurationsParams configurations;

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

      public Builder expiresIn(Integer expiresIn) {
         this.expiresIn = expiresIn;
         return this;
      }

      public Builder metaData(List<MetaDataParams> metaData) {
         this.metaData = metaData;
         return this;
      }

      public Builder description(String description) {
         this.description = description;
         return this;
      }

      public Builder invoiceNumber(String invoiceNumber) {
         this.invoiceNumber = invoiceNumber;
         return this;
      }

      public Builder referenceNumber(String referenceNumber) {
         this.referenceNumber = referenceNumber;
         return this;
      }

      public Builder maxRetryCount(Integer maxRetryCount) {
         this.maxRetryCount = maxRetryCount;
         return this;
      }

      public Builder configurations(ConfigurationsParams configurations) {
         this.configurations = configurations;
         return this;
      }

      public PaymentSessionCreateParams build() {
         if (this.amount == null) {
            throw new IllegalArgumentException("amount is required");
         } else if (this.currency != null && !this.currency.isEmpty()) {
            if (this.description != null && !this.description.isEmpty()) {
               ParamValidator.validateDescription(this.description);
               ParamValidator.validateInvoiceNumber(this.invoiceNumber);
               ParamValidator.validateReferenceNumber(this.referenceNumber);
               if (this.expiresIn == null || this.expiresIn >= 300 && this.expiresIn <= 900) {
                  if (this.maxRetryCount == null || this.maxRetryCount >= 1 && this.maxRetryCount <= 5) {
                     MetaDataValidator.validate(this.metaData);
                     return new PaymentSessionCreateParams(this);
                  } else {
                     throw new IllegalArgumentException("maxRetryCount must be between 1 and 5");
                  }
               } else {
                  throw new IllegalArgumentException("expiresIn must be between 300 and 900 (seconds)");
               }
            } else {
               throw new IllegalArgumentException("description is required");
            }
         } else {
            throw new IllegalArgumentException("currency is required");
         }
      }
   }
}
