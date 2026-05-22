package com.zohopayments.model;

public final class NotifyCustomer {
   private Boolean email;
   private Boolean sms;

   NotifyCustomer() {
   }

   public Boolean getEmail() {
      return this.email;
   }

   public Boolean getSms() {
      return this.sms;
   }
}
