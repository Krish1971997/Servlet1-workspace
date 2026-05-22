package com.zohopayments.model.paymentmethod;

public final class PaymentMethodDetail {
   private String type;
   private String mandateId;
   private CardDetail card;
   private AchDebitDetail achDebit;
   private Upi upi;
   private NetBanking netBanking;
   private BankTransfer bankTransfer;

   PaymentMethodDetail() {
   }

   public String getType() {
      return this.type;
   }

   public String getMandateId() {
      return this.mandateId;
   }

   public CardDetail getCard() {
      return this.card;
   }

   public AchDebitDetail getAchDebit() {
      return this.achDebit;
   }

   public Upi getUpi() {
      return this.upi;
   }

   public NetBanking getNetBanking() {
      return this.netBanking;
   }

   public BankTransfer getBankTransfer() {
      return this.bankTransfer;
   }

   public static final class Upi {
      private String upiId;
      private String channel;
      private String accountType;

      Upi() {
      }

      public String getUpiId() {
         return this.upiId;
      }

      public String getChannel() {
         return this.channel;
      }

      public String getAccountType() {
         return this.accountType;
      }
   }

   public static final class NetBanking {
      private String bankName;

      NetBanking() {
      }

      public String getBankName() {
         return this.bankName;
      }
   }

   public static final class BankTransfer {
      private String virtualAccountNumber;
      private String mode;
      private String payerName;
      private String payerAccountNo;
      private String payerIfscCode;

      BankTransfer() {
      }

      public String getVirtualAccountNumber() {
         return this.virtualAccountNumber;
      }

      public String getMode() {
         return this.mode;
      }

      public String getPayerName() {
         return this.payerName;
      }

      public String getPayerAccountNo() {
         return this.payerAccountNo;
      }

      public String getPayerIfscCode() {
         return this.payerIfscCode;
      }
   }
}
