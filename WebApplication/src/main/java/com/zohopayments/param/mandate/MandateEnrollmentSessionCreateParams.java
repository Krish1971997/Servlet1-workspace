package com.zohopayments.param.mandate;

import com.zohopayments.param.MetaDataParams;
import com.zohopayments.param.MetaDataValidator;
import com.zohopayments.param.ParamValidator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MandateEnrollmentSessionCreateParams {
   private final Double amount;
   private final String currency;
   private final String customerId;
   private final String type;
   private final String description;
   private final String invoiceNumber;
   private final Integer maxRetryCount;
   private final MandateDetailsParams mandateDetails;
   private final List<MetaDataParams> metaData;
   private final MandateConfigurationsParams configurations;

   private MandateEnrollmentSessionCreateParams(Builder b) {
      this.amount = b.amount;
      this.currency = b.currency;
      this.customerId = b.customerId;
      this.type = "mandate_enrollment";
      this.description = b.description;
      this.invoiceNumber = b.invoiceNumber;
      this.maxRetryCount = b.maxRetryCount;
      this.mandateDetails = b.mandateDetails;
      this.metaData = b.metaData != null ? Collections.unmodifiableList(new ArrayList(b.metaData)) : null;
      this.configurations = b.configurations;
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

   public MandateDetailsParams getMandateDetails() {
      return this.mandateDetails;
   }

   public List<MetaDataParams> getMetaData() {
      return this.metaData;
   }

   public MandateConfigurationsParams getConfigurations() {
      return this.configurations;
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
      private MandateDetailsParams mandateDetails;
      private List<MetaDataParams> metaData;
      private MandateConfigurationsParams configurations;

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

      public Builder mandateDetails(MandateDetailsParams mandateDetails) {
         this.mandateDetails = mandateDetails;
         return this;
      }

      public Builder metaData(List<MetaDataParams> metaData) {
         this.metaData = metaData;
         return this;
      }

      public Builder configurations(MandateConfigurationsParams configurations) {
         this.configurations = configurations;
         return this;
      }

      public MandateEnrollmentSessionCreateParams build() {
         if (this.amount == null) {
            throw new IllegalArgumentException("amount is required");
         } else if (this.currency != null && !this.currency.isEmpty()) {
            if (this.customerId != null && !this.customerId.isEmpty()) {
               if (this.description != null && !this.description.isEmpty()) {
                  if (this.mandateDetails == null) {
                     throw new IllegalArgumentException("mandateDetails is required");
                  } else if (this.maxRetryCount == null || this.maxRetryCount >= 1 && this.maxRetryCount <= 3) {
                     ParamValidator.validateDescription(this.description);
                     ParamValidator.validateInvoiceNumber(this.invoiceNumber);
                     MetaDataValidator.validate(this.metaData);
                     return new MandateEnrollmentSessionCreateParams(this);
                  } else {
                     throw new IllegalArgumentException("maxRetryCount must be between 1 and 3");
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
