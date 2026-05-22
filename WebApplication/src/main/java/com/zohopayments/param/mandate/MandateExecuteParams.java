package com.zohopayments.param.mandate;

import com.zohopayments.param.ParamValidator;

public final class MandateExecuteParams {
   private final String customerId;
   private final String mandateId;
   private final String paymentsSessionId;
   private final String invoiceNumber;
   private final Double amount;
   private final String mandateNotificationId;
   private final String receiptEmail;
   private final String phone;
   private final String phoneCountryCode;
   private final String description;
   private final String referenceNumber;

   private MandateExecuteParams(Builder b) {
      this.customerId = b.customerId;
      this.mandateId = b.mandateId;
      this.paymentsSessionId = b.paymentsSessionId;
      this.invoiceNumber = b.invoiceNumber;
      this.amount = b.amount;
      this.mandateNotificationId = b.mandateNotificationId;
      this.receiptEmail = b.receiptEmail;
      this.phone = b.phone;
      this.phoneCountryCode = b.phoneCountryCode;
      this.description = b.description;
      this.referenceNumber = b.referenceNumber;
   }

   public String getCustomerId() {
      return this.customerId;
   }

   public String getMandateId() {
      return this.mandateId;
   }

   public String getPaymentsSessionId() {
      return this.paymentsSessionId;
   }

   public String getInvoiceNumber() {
      return this.invoiceNumber;
   }

   public Double getAmount() {
      return this.amount;
   }

   public String getMandateNotificationId() {
      return this.mandateNotificationId;
   }

   public String getReceiptEmail() {
      return this.receiptEmail;
   }

   public String getPhone() {
      return this.phone;
   }

   public String getPhoneCountryCode() {
      return this.phoneCountryCode;
   }

   public String getDescription() {
      return this.description;
   }

   public String getReferenceNumber() {
      return this.referenceNumber;
   }

   public static Builder builder() {
      return new Builder();
   }

   public static final class Builder {
      private String customerId;
      private String mandateId;
      private String paymentsSessionId;
      private String invoiceNumber;
      private Double amount;
      private String mandateNotificationId;
      private String receiptEmail;
      private String phone;
      private String phoneCountryCode;
      private String description;
      private String referenceNumber;

      private Builder() {
      }

      public Builder customerId(String customerId) {
         this.customerId = customerId;
         return this;
      }

      public Builder mandateId(String mandateId) {
         this.mandateId = mandateId;
         return this;
      }

      public Builder paymentsSessionId(String paymentsSessionId) {
         this.paymentsSessionId = paymentsSessionId;
         return this;
      }

      public Builder invoiceNumber(String invoiceNumber) {
         this.invoiceNumber = invoiceNumber;
         return this;
      }

      public Builder amount(Double amount) {
         this.amount = amount;
         return this;
      }

      public Builder mandateNotificationId(String mandateNotificationId) {
         this.mandateNotificationId = mandateNotificationId;
         return this;
      }

      public Builder receiptEmail(String receiptEmail) {
         this.receiptEmail = receiptEmail;
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

      public Builder description(String description) {
         this.description = description;
         return this;
      }

      public Builder referenceNumber(String referenceNumber) {
         this.referenceNumber = referenceNumber;
         return this;
      }

      public MandateExecuteParams build() {
         if (this.customerId != null && !this.customerId.isEmpty()) {
            if (this.mandateId != null && !this.mandateId.isEmpty()) {
               if (this.paymentsSessionId != null && !this.paymentsSessionId.isEmpty()) {
                  if (this.invoiceNumber != null && !this.invoiceNumber.isEmpty()) {
                     if (this.amount == null) {
                        throw new IllegalArgumentException("amount is required");
                     } else {
                        ParamValidator.validateDescription(this.description);
                        ParamValidator.validateInvoiceNumber(this.invoiceNumber);
                        ParamValidator.validateReferenceNumber(this.referenceNumber);
                        return new MandateExecuteParams(this);
                     }
                  } else {
                     throw new IllegalArgumentException("invoiceNumber is required");
                  }
               } else {
                  throw new IllegalArgumentException("paymentsSessionId is required");
               }
            } else {
               throw new IllegalArgumentException("mandateId is required");
            }
         } else {
            throw new IllegalArgumentException("customerId is required");
         }
      }
   }
}
