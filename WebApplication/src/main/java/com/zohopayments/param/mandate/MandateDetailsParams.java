package com.zohopayments.param.mandate;

import com.zohopayments.param.ParamValidator;

public final class MandateDetailsParams {
   private final String paymentMethodType;
   private final String frequency;
   private final String description;
   private final String amountRule;
   private final Double maxAmount;
   private final String startDate;
   private final String endDate;
   private final Integer debitDay;
   private final String debitRule;

   private MandateDetailsParams(Builder b) {
      this.paymentMethodType = b.paymentMethodType;
      this.frequency = b.frequency;
      this.description = b.description;
      this.amountRule = b.amountRule;
      this.maxAmount = b.maxAmount;
      this.startDate = b.startDate;
      this.endDate = b.endDate;
      this.debitDay = b.debitDay;
      this.debitRule = b.debitRule;
   }

   public String getPaymentMethodType() {
      return this.paymentMethodType;
   }

   public String getFrequency() {
      return this.frequency;
   }

   public String getDescription() {
      return this.description;
   }

   public String getAmountRule() {
      return this.amountRule;
   }

   public Double getMaxAmount() {
      return this.maxAmount;
   }

   public String getStartDate() {
      return this.startDate;
   }

   public String getEndDate() {
      return this.endDate;
   }

   public Integer getDebitDay() {
      return this.debitDay;
   }

   public String getDebitRule() {
      return this.debitRule;
   }

   public static Builder builder() {
      return new Builder();
   }

   public static final class Builder {
      private String paymentMethodType;
      private String frequency;
      private String description;
      private String amountRule;
      private Double maxAmount;
      private String startDate;
      private String endDate;
      private Integer debitDay;
      private String debitRule;

      private Builder() {
      }

      public Builder paymentMethodType(String paymentMethodType) {
         this.paymentMethodType = paymentMethodType;
         return this;
      }

      public Builder frequency(String frequency) {
         this.frequency = frequency;
         return this;
      }

      public Builder description(String description) {
         this.description = description;
         return this;
      }

      public Builder amountRule(String amountRule) {
         this.amountRule = amountRule;
         return this;
      }

      public Builder maxAmount(Double maxAmount) {
         this.maxAmount = maxAmount;
         return this;
      }

      public Builder startDate(String startDate) {
         this.startDate = startDate;
         return this;
      }

      public Builder endDate(String endDate) {
         this.endDate = endDate;
         return this;
      }

      public Builder debitDay(Integer debitDay) {
         this.debitDay = debitDay;
         return this;
      }

      public Builder debitRule(String debitRule) {
         this.debitRule = debitRule;
         return this;
      }

      public MandateDetailsParams build() {
         if (this.paymentMethodType != null && !this.paymentMethodType.isEmpty()) {
            if (this.frequency != null && !this.frequency.isEmpty()) {
               if (this.description != null && !this.description.isEmpty()) {
                  if (this.amountRule != null && !this.amountRule.isEmpty()) {
                     if ("variable".equals(this.amountRule) && this.maxAmount == null) {
                        throw new IllegalArgumentException("maxAmount is required when amountRule is variable");
                     } else {
                        ParamValidator.validateDescription(this.description);
                        return new MandateDetailsParams(this);
                     }
                  } else {
                     throw new IllegalArgumentException("amountRule is required");
                  }
               } else {
                  throw new IllegalArgumentException("description is required");
               }
            } else {
               throw new IllegalArgumentException("frequency is required");
            }
         } else {
            throw new IllegalArgumentException("paymentMethodType is required");
         }
      }
   }
}
