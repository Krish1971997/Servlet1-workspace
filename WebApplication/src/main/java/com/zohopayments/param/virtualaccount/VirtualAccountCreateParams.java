package com.zohopayments.param.virtualaccount;

import com.zohopayments.param.MetaDataParams;
import com.zohopayments.param.MetaDataValidator;
import com.zohopayments.param.ParamValidator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class VirtualAccountCreateParams {
   private final String description;
   private final String customerId;
   private final Double minimumAmount;
   private final Double maximumAmount;
   private final String expiresAt;
   private final String referenceNumber;
   private final List<MetaDataParams> metaData;

   private VirtualAccountCreateParams(Builder b) {
      this.description = b.description;
      this.customerId = b.customerId;
      this.minimumAmount = b.minimumAmount;
      this.maximumAmount = b.maximumAmount;
      this.expiresAt = b.expiresAt;
      this.referenceNumber = b.referenceNumber;
      this.metaData = b.metaData != null ? Collections.unmodifiableList(new ArrayList(b.metaData)) : null;
   }

   public String getDescription() {
      return this.description;
   }

   public String getCustomerId() {
      return this.customerId;
   }

   public Double getMinimumAmount() {
      return this.minimumAmount;
   }

   public Double getMaximumAmount() {
      return this.maximumAmount;
   }

   public String getExpiresAt() {
      return this.expiresAt;
   }

   public String getReferenceNumber() {
      return this.referenceNumber;
   }

   public List<MetaDataParams> getMetaData() {
      return this.metaData;
   }

   public static Builder builder() {
      return new Builder();
   }

   public static final class Builder {
      private String description;
      private String customerId;
      private Double minimumAmount;
      private Double maximumAmount;
      private String expiresAt;
      private String referenceNumber;
      private List<MetaDataParams> metaData;

      private Builder() {
      }

      public Builder description(String description) {
         this.description = description;
         return this;
      }

      public Builder customerId(String customerId) {
         this.customerId = customerId;
         return this;
      }

      public Builder minimumAmount(Double minimumAmount) {
         this.minimumAmount = minimumAmount;
         return this;
      }

      public Builder maximumAmount(Double maximumAmount) {
         this.maximumAmount = maximumAmount;
         return this;
      }

      public Builder expiresAt(String expiresAt) {
         this.expiresAt = expiresAt;
         return this;
      }

      public Builder referenceNumber(String referenceNumber) {
         this.referenceNumber = referenceNumber;
         return this;
      }

      public Builder metaData(List<MetaDataParams> metaData) {
         this.metaData = metaData;
         return this;
      }

      public VirtualAccountCreateParams build() {
         if (this.description != null && !this.description.isEmpty()) {
            ParamValidator.validateDescription(this.description);
            ParamValidator.validateReferenceNumber(this.referenceNumber);
            MetaDataValidator.validate(this.metaData);
            return new VirtualAccountCreateParams(this);
         } else {
            throw new IllegalArgumentException("description is required");
         }
      }
   }
}
