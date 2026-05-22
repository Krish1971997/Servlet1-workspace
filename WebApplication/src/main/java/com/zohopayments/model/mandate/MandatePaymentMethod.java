package com.zohopayments.model.mandate;

public final class MandatePaymentMethod {
   private String type;
   private Upi upi;

   MandatePaymentMethod() {
   }

   public String getType() {
      return this.type;
   }

   public Upi getUpi() {
      return this.upi;
   }

   public static final class Upi {
      private String upiId;

      Upi() {
      }

      public String getUpiId() {
         return this.upiId;
      }
   }
}
