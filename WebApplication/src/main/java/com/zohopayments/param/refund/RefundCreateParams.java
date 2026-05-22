package com.zohopayments.param.refund;

import com.zohopayments.param.MetaDataParams;
import com.zohopayments.param.MetaDataValidator;
import com.zohopayments.param.ParamValidator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class RefundCreateParams {
   private final Double amount;
   private final String reason;
   private final String type;
   private final String description;
   private final List<MetaDataParams> metaData;

   private RefundCreateParams(Builder b) {
      this.amount = b.amount;
      this.reason = b.reason;
      this.type = b.type;
      this.description = b.description;
      this.metaData = b.metaData != null ? Collections.unmodifiableList(new ArrayList(b.metaData)) : null;
   }

   public Double getAmount() {
      return this.amount;
   }

   public String getReason() {
      return this.reason;
   }

   public String getType() {
      return this.type;
   }

   public String getDescription() {
      return this.description;
   }

   public List<MetaDataParams> getMetaData() {
      return this.metaData;
   }

   public static Builder builder() {
      return new Builder();
   }

   public static final class Builder {
      private Double amount;
      private String reason;
      private String type;
      private String description;
      private List<MetaDataParams> metaData;

      private Builder() {
      }

      public Builder amount(Double amount) {
         this.amount = amount;
         return this;
      }

      public Builder reason(String reason) {
         this.reason = reason;
         return this;
      }

      public Builder type(String type) {
         this.type = type;
         return this;
      }

      public Builder description(String description) {
         this.description = description;
         return this;
      }

      public Builder metaData(List<MetaDataParams> metaData) {
         this.metaData = metaData;
         return this;
      }

      public RefundCreateParams build() {
         if (this.amount == null) {
            throw new IllegalArgumentException("amount is required");
         } else if (this.reason != null && !this.reason.isEmpty()) {
            if (this.type != null && !this.type.isEmpty()) {
               ParamValidator.validateDescription(this.description);
               MetaDataValidator.validate(this.metaData);
               return new RefundCreateParams(this);
            } else {
               throw new IllegalArgumentException("type is required");
            }
         } else {
            throw new IllegalArgumentException("reason is required");
         }
      }
   }
}
