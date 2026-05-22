package com.zohopayments.model.paymentsession;

import com.zohopayments.model.Configurations;
import com.zohopayments.model.MetaData;
import java.util.Collections;
import java.util.List;

public final class PaymentSession {
   private String paymentsSessionId;
   private String accessKey;
   private String currency;
   private String amount;
   private String status;
   private Long createdTime;
   private Long expiryTime;
   private List<PaymentSessionPayment> payments;
   private List<MetaData> metaData;
   private String description;
   private String invoiceNumber;
   private String referenceNumber;
   private Integer maxRetryCount;
   private Configurations configurations;

   PaymentSession() {
   }

   public String getPaymentsSessionId() {
      return this.paymentsSessionId;
   }

   public String getAccessKey() {
      return this.accessKey;
   }

   public String getCurrency() {
      return this.currency;
   }

   public String getAmount() {
      return this.amount;
   }

   public String getStatus() {
      return this.status;
   }

   public Long getCreatedTime() {
      return this.createdTime;
   }

   public Long getExpiryTime() {
      return this.expiryTime;
   }

   public List<PaymentSessionPayment> getPayments() {
      return this.payments != null ? Collections.unmodifiableList(this.payments) : Collections.emptyList();
   }

   public List<MetaData> getMetaData() {
      return this.metaData != null ? Collections.unmodifiableList(this.metaData) : Collections.emptyList();
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

   public Configurations getConfigurations() {
      return this.configurations;
   }
}
