package com.zohopayments.model.paymentlink;

import com.zohopayments.model.Configurations;
import java.util.Collections;
import java.util.List;

public final class PaymentLink {
   private String paymentLinkId;
   private String url;
   private String expiresAt;
   private String amount;
   private String amountPaid;
   private String currency;
   private String status;
   private String email;
   private String referenceId;
   private String description;
   private String returnUrl;
   private String phone;
   private String phoneCountryCode;
   private Long createdTime;
   private String createdById;
   private String createdBy;
   private String lastModifiedById;
   private String lastModified;
   private Configurations configurations;
   private List<PaymentLinkPayment> payments;

   PaymentLink() {
   }

   public String getPaymentLinkId() {
      return this.paymentLinkId;
   }

   public String getUrl() {
      return this.url;
   }

   public String getExpiresAt() {
      return this.expiresAt;
   }

   public String getAmount() {
      return this.amount;
   }

   public String getAmountPaid() {
      return this.amountPaid;
   }

   public String getCurrency() {
      return this.currency;
   }

   public String getStatus() {
      return this.status;
   }

   public String getEmail() {
      return this.email;
   }

   public String getReferenceId() {
      return this.referenceId;
   }

   public String getDescription() {
      return this.description;
   }

   public String getReturnUrl() {
      return this.returnUrl;
   }

   public String getPhone() {
      return this.phone;
   }

   public String getPhoneCountryCode() {
      return this.phoneCountryCode;
   }

   public Long getCreatedTime() {
      return this.createdTime;
   }

   public String getCreatedById() {
      return this.createdById;
   }

   public String getCreatedBy() {
      return this.createdBy;
   }

   public String getLastModifiedById() {
      return this.lastModifiedById;
   }

   public String getLastModified() {
      return this.lastModified;
   }

   public Configurations getConfigurations() {
      return this.configurations;
   }

   public List<PaymentLinkPayment> getPayments() {
      return this.payments != null ? Collections.unmodifiableList(this.payments) : Collections.emptyList();
   }

   public static final class PaymentLinkPayment {
      private String paymentId;
      private String amount;
      private String type;
      private String status;
      private Long date;

      PaymentLinkPayment() {
      }

      public String getPaymentId() {
         return this.paymentId;
      }

      public String getAmount() {
         return this.amount;
      }

      public String getType() {
         return this.type;
      }

      public String getStatus() {
         return this.status;
      }

      public Long getDate() {
         return this.date;
      }
   }
}
