package com.zohopayments.model.refund;

import com.zohopayments.model.MetaData;
import java.util.Collections;
import java.util.List;

public final class Refund {
   private String refundId;
   private String paymentId;
   private String referenceNumber;
   private String amount;
   private String defaultCurrencyAmount;
   private String type;
   private String reason;
   private String description;
   private String status;
   private String networkReferenceNumber;
   private String failureReason;
   private Long date;
   private List<MetaData> metaData;

   Refund() {
   }

   public String getRefundId() {
      return this.refundId;
   }

   public String getPaymentId() {
      return this.paymentId;
   }

   public String getReferenceNumber() {
      return this.referenceNumber;
   }

   public String getAmount() {
      return this.amount;
   }

   public String getDefaultCurrencyAmount() {
      return this.defaultCurrencyAmount;
   }

   public String getType() {
      return this.type;
   }

   public String getReason() {
      return this.reason;
   }

   public String getDescription() {
      return this.description;
   }

   public String getStatus() {
      return this.status;
   }

   public String getNetworkReferenceNumber() {
      return this.networkReferenceNumber;
   }

   public String getFailureReason() {
      return this.failureReason;
   }

   public Long getDate() {
      return this.date;
   }

   public List<MetaData> getMetaData() {
      return this.metaData != null ? Collections.unmodifiableList(this.metaData) : Collections.emptyList();
   }
}
