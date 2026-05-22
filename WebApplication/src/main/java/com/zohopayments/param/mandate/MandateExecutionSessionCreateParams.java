package com.zohopayments.param.mandate;

import com.zohopayments.param.MetaDataParams;
import com.zohopayments.param.MetaDataValidator;
import com.zohopayments.param.ParamValidator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MandateExecutionSessionCreateParams {
   private final Double amount;
   private final String currency;
   private final String customerId;
   private final String type;
   private final String description;
   private final String invoiceNumber;
   private final Integer maxRetryCount;
   private final List<MetaDataParams> metaData;

   private MandateExecutionSessionCreateParams(Builder b) {
      this.amount = b.amount;
      this.currency = b.currency;
      this.customerId = b.customerId;
      this.type = "mandate_execution";
      this.description = b.description;
      this.invoiceNumber = b.invoiceNumber;
      this.maxRetryCount = b.maxRetryCount;
      this.metaData = b.metaData != null ? Collections.unmodifiableList(new ArrayList(b.metaData)) : null;
   }

   public Double getAmount() {
      return this.amount;
   }

   public String getCurrency() {
      return this.currency;
   }

   public String getCustomerId() {
      return this.customerId;
   }

   public String getType() {
      return this.type;
   }

   public String getDescription() {
      return this.description;
   }

   public String getInvoiceNumber() {
      return this.invoiceNumber;
   }

   public Integer getMaxRetryCount() {
      return this.maxRetryCount;
   }

   public List<MetaDataParams> getMetaData() {
      return this.metaData;
   }

   public static Builder builder() {
      return new Builder();
   }

   public static final class Builder {
      private Double amount;
      private String currency;
      private String customerId;
      private String description;
      private String invoiceNumber;
      private Integer maxRetryCount;
      private List<MetaDataParams> metaData;

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

      public Builder customerId(String customerId) {
         this.customerId = customerId;
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

      public Builder maxRetryCount(Integer maxRetryCount) {
         this.maxRetryCount = maxRetryCount;
         return this;
      }

      public Builder metaData(List<MetaDataParams> metaData) {
         this.metaData = metaData;
         return this;
      }

      public MandateExecutionSessionCreateParams build() {
         if (this.amount == null) {
            throw new IllegalArgumentException("amount is required");
         } else if (this.currency != null && !this.currency.isEmpty()) {
            if (this.customerId != null && !this.customerId.isEmpty()) {
               if (this.description != null && !this.description.isEmpty()) {
                  if (this.invoiceNumber != null && !this.invoiceNumber.isEmpty()) {
                     if (this.maxRetryCount == null || this.maxRetryCount >= 1 && this.maxRetryCount <= 3) {
                        ParamValidator.validateDescription(this.description);
                        ParamValidator.validateInvoiceNumber(this.invoiceNumber);
                        MetaDataValidator.validate(this.metaData);
                        return new MandateExecutionSessionCreateParams(this);
                     } else {
                        throw new IllegalArgumentException("maxRetryCount must be between 1 and 3");
                     }
                  } else {
                     throw new IllegalArgumentException("invoiceNumber is required");
                  }
               } else {
                  throw new IllegalArgumentException("description is required");
               }
            } else {
               throw new IllegalArgumentException("customerId is required");
            }
         } else {
            throw new IllegalArgumentException("currency is required");
         }
      }
   }
}
