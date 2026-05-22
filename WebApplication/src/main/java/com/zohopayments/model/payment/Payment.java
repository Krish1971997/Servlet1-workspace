package com.zohopayments.model.payment;

import com.zohopayments.model.MetaData;
import com.zohopayments.model.paymentmethod.PaymentMethodDetail;
import java.util.Collections;
import java.util.List;

public final class Payment {
   private String paymentId;
   private String phone;
   private String amount;
   private String currency;
   private String paymentsSessionId;
   private String receiptEmail;
   private String referenceNumber;
   private String transactionReferenceNumber;
   private String invoiceNumber;
   private String amountCaptured;
   private String amountRefunded;
   private String feeAmount;
   private String netTaxAmount;
   private String totalFeeAmount;
   private String netAmount;
   private String status;
   private Double exchangeRate;
   private String statementDescriptor;
   private String description;
   private Long date;
   private PaymentMethodDetail paymentMethod;
   private List<MetaData> metaData;

   Payment() {
   }

   public String getPaymentId() {
      return this.paymentId;
   }

   public String getPhone() {
      return this.phone;
   }

   public String getAmount() {
      return this.amount;
   }

   public String getCurrency() {
      return this.currency;
   }

   public String getPaymentsSessionId() {
      return this.paymentsSessionId;
   }

   public String getReceiptEmail() {
      return this.receiptEmail;
   }

   public String getReferenceNumber() {
      return this.referenceNumber;
   }

   public String getTransactionReferenceNumber() {
      return this.transactionReferenceNumber;
   }

   public String getInvoiceNumber() {
      return this.invoiceNumber;
   }

   public String getAmountCaptured() {
      return this.amountCaptured;
   }

   public String getAmountRefunded() {
      return this.amountRefunded;
   }

   public String getFeeAmount() {
      return this.feeAmount;
   }

   public String getNetTaxAmount() {
      return this.netTaxAmount;
   }

   public String getTotalFeeAmount() {
      return this.totalFeeAmount;
   }

   public String getNetAmount() {
      return this.netAmount;
   }

   public String getStatus() {
      return this.status;
   }

   public Double getExchangeRate() {
      return this.exchangeRate;
   }

   public String getStatementDescriptor() {
      return this.statementDescriptor;
   }

   public String getDescription() {
      return this.description;
   }

   public Long getDate() {
      return this.date;
   }

   public PaymentMethodDetail getPaymentMethod() {
      return this.paymentMethod;
   }

   public List<MetaData> getMetaData() {
      return this.metaData != null ? Collections.unmodifiableList(this.metaData) : Collections.emptyList();
   }
}
