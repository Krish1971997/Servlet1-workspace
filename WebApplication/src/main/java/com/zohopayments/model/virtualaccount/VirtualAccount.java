package com.zohopayments.model.virtualaccount;

import com.zohopayments.model.MetaData;
import java.util.Collections;
import java.util.List;

public final class VirtualAccount {
   private String virtualAccountId;
   private String accountNumber;
   private String ifscCode;
   private String beneficiaryName;
   private String description;
   private String customerId;
   private String referenceNumber;
   private String status;
   private String expiresAt;
   private Long createdTime;
   private Long lastModifiedTime;
   private List<MetaData> metaData;
   private Double minimumAmount;
   private Double maximumAmount;
   private Double amountPaid;

   VirtualAccount() {
   }

   public String getVirtualAccountId() {
      return this.virtualAccountId;
   }

   public String getAccountNumber() {
      return this.accountNumber;
   }

   public String getIfscCode() {
      return this.ifscCode;
   }

   public String getBeneficiaryName() {
      return this.beneficiaryName;
   }

   public String getDescription() {
      return this.description;
   }

   public String getCustomerId() {
      return this.customerId;
   }

   public String getReferenceNumber() {
      return this.referenceNumber;
   }

   public String getStatus() {
      return this.status;
   }

   public String getExpiresAt() {
      return this.expiresAt;
   }

   public Long getCreatedTime() {
      return this.createdTime;
   }

   public Long getLastModifiedTime() {
      return this.lastModifiedTime;
   }

   public List<MetaData> getMetaData() {
      return this.metaData != null ? Collections.unmodifiableList(this.metaData) : Collections.emptyList();
   }

   public Double getMinimumAmount() {
      return this.minimumAmount;
   }

   public Double getMaximumAmount() {
      return this.maximumAmount;
   }

   public Double getAmountPaid() {
      return this.amountPaid;
   }
}
