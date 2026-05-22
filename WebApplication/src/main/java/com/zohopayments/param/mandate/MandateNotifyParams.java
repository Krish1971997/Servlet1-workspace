package com.zohopayments.param.mandate;

import com.zohopayments.param.ParamValidator;

public final class MandateNotifyParams {
   private final String mandateId;
   private final Double amount;
   private final String executionDate;
   private final String description;
   private final String invoiceNumber;

   private MandateNotifyParams(Builder b) {
      this.mandateId = b.mandateId;
      this.amount = b.amount;
      this.executionDate = b.executionDate;
      this.description = b.description;
      this.invoiceNumber = b.invoiceNumber;
   }

   public String getMandateId() {
      return this.mandateId;
   }

   public Double getAmount() {
      return this.amount;
   }

   public String getExecutionDate() {
      return this.executionDate;
   }

   public String getDescription() {
      return this.description;
   }

   public String getInvoiceNumber() {
      return this.invoiceNumber;
   }

   public static Builder builder() {
      return new Builder();
   }

   public static final class Builder {
      private String mandateId;
      private Double amount;
      private String executionDate;
      private String description;
      private String invoiceNumber;

      private Builder() {
      }

      public Builder mandateId(String mandateId) {
         this.mandateId = mandateId;
         return this;
      }

      public Builder amount(Double amount) {
         this.amount = amount;
         return this;
      }

      public Builder executionDate(String executionDate) {
         this.executionDate = executionDate;
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

      public MandateNotifyParams build() {
         if (this.mandateId != null && !this.mandateId.isEmpty()) {
            if (this.amount == null) {
               throw new IllegalArgumentException("amount is required");
            } else if (this.executionDate != null && !this.executionDate.isEmpty()) {
               if (this.description != null && !this.description.isEmpty()) {
                  if (this.invoiceNumber != null && !this.invoiceNumber.isEmpty()) {
                     ParamValidator.validateDescription(this.description);
                     ParamValidator.validateInvoiceNumber(this.invoiceNumber);
                     return new MandateNotifyParams(this);
                  } else {
                     throw new IllegalArgumentException("invoiceNumber is required");
                  }
               } else {
                  throw new IllegalArgumentException("description is required");
               }
            } else {
               throw new IllegalArgumentException("executionDate is required");
            }
         } else {
            throw new IllegalArgumentException("mandateId is required");
         }
      }
   }
}
