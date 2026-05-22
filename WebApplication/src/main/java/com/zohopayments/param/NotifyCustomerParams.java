package com.zohopayments.param;

public final class NotifyCustomerParams {
   private final Boolean email;
   private final Boolean sms;

   public NotifyCustomerParams(Boolean email, Boolean sms) {
      this.email = email;
      this.sms = sms;
   }

   public Boolean getEmail() {
      return this.email;
   }

   public Boolean getSms() {
      return this.sms;
   }
}
