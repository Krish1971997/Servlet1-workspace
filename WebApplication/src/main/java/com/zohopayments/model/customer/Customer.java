package com.zohopayments.model.customer;

import com.zohopayments.model.MetaData;
import java.util.Collections;
import java.util.List;

public final class Customer {
   private String customerId;
   private String name;
   private String email;
   private String phone;
   private String dialingCode;
   private Long createdTime;
   private Long lastModifiedTime;
   private List<MetaData> metaData;
   private List<CustomerPaymentMethod> paymentMethods;

   Customer() {
   }

   public String getCustomerId() {
      return this.customerId;
   }

   public String getName() {
      return this.name;
   }

   public String getEmail() {
      return this.email;
   }

   public String getPhone() {
      return this.phone;
   }

   public String getDialingCode() {
      return this.dialingCode;
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

   public List<CustomerPaymentMethod> getPaymentMethods() {
      return this.paymentMethods != null ? Collections.unmodifiableList(this.paymentMethods) : Collections.emptyList();
   }
}
