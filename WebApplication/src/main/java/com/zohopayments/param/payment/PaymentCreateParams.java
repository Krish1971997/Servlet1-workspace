package com.zohopayments.param.payment;

import com.zohopayments.param.MetaDataParams;
import com.zohopayments.param.MetaDataValidator;
import com.zohopayments.param.ParamValidator;
import com.zohopayments.param.PostalAddressParams;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PaymentCreateParams {
   private final String customerId;
   private final String paymentMethodId;
   private final Double amount;
   private final String currency;
   private final Boolean customerOnSession;
   private final BrowserInfo browserInfo;
   private final String statementDescriptor;
   private final String description;
   private final PostalAddressParams shippingAddress;
   private final List<MetaDataParams> metaData;

   private PaymentCreateParams(Builder b) {
      this.customerId = b.customerId;
      this.paymentMethodId = b.paymentMethodId;
      this.amount = b.amount;
      this.currency = b.currency;
      this.customerOnSession = b.customerOnSession;
      this.browserInfo = b.browserInfo;
      this.statementDescriptor = b.statementDescriptor;
      this.description = b.description;
      this.shippingAddress = b.shippingAddress;
      this.metaData = b.metaData != null ? Collections.unmodifiableList(new ArrayList(b.metaData)) : null;
   }

   public String getCustomerId() {
      return this.customerId;
   }

   public String getPaymentMethodId() {
      return this.paymentMethodId;
   }

   public Double getAmount() {
      return this.amount;
   }

   public String getCurrency() {
      return this.currency;
   }

   public Boolean getCustomerOnSession() {
      return this.customerOnSession;
   }

   public BrowserInfo getBrowserInfo() {
      return this.browserInfo;
   }

   public String getStatementDescriptor() {
      return this.statementDescriptor;
   }

   public String getDescription() {
      return this.description;
   }

   public PostalAddressParams getShippingAddress() {
      return this.shippingAddress;
   }

   public List<MetaDataParams> getMetaData() {
      return this.metaData;
   }

   public static Builder builder() {
      return new Builder();
   }

   public static final class BrowserInfo {
      private final String userAgent;
      private final String acceptHeader;
      private final Integer screenHeight;
      private final Integer screenWidth;
      private final String language;
      private final Integer timeZoneOffset;
      private final Integer colorDepth;

      private BrowserInfo(Builder b) {
         this.userAgent = b.userAgent;
         this.acceptHeader = b.acceptHeader;
         this.screenHeight = b.screenHeight;
         this.screenWidth = b.screenWidth;
         this.language = b.language;
         this.timeZoneOffset = b.timeZoneOffset;
         this.colorDepth = b.colorDepth;
      }

      public String getUserAgent() {
         return this.userAgent;
      }

      public String getAcceptHeader() {
         return this.acceptHeader;
      }

      public Integer getScreenHeight() {
         return this.screenHeight;
      }

      public Integer getScreenWidth() {
         return this.screenWidth;
      }

      public String getLanguage() {
         return this.language;
      }

      public Integer getTimeZoneOffset() {
         return this.timeZoneOffset;
      }

      public Integer getColorDepth() {
         return this.colorDepth;
      }

      public static Builder builder() {
         return new Builder();
      }

      public static final class Builder {
         private String userAgent;
         private String acceptHeader;
         private Integer screenHeight;
         private Integer screenWidth;
         private String language;
         private Integer timeZoneOffset;
         private Integer colorDepth;

         private Builder() {
         }

         public Builder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
         }

         public Builder acceptHeader(String acceptHeader) {
            this.acceptHeader = acceptHeader;
            return this;
         }

         public Builder screenHeight(Integer screenHeight) {
            this.screenHeight = screenHeight;
            return this;
         }

         public Builder screenWidth(Integer screenWidth) {
            this.screenWidth = screenWidth;
            return this;
         }

         public Builder language(String language) {
            this.language = language;
            return this;
         }

         public Builder timeZoneOffset(Integer timeZoneOffset) {
            this.timeZoneOffset = timeZoneOffset;
            return this;
         }

         public Builder colorDepth(Integer colorDepth) {
            this.colorDepth = colorDepth;
            return this;
         }

         public BrowserInfo build() {
            return new BrowserInfo(this);
         }
      }
   }

   public static final class Builder {
      private String customerId;
      private String paymentMethodId;
      private Double amount;
      private String currency;
      private Boolean customerOnSession;
      private BrowserInfo browserInfo;
      private String statementDescriptor;
      private String description;
      private PostalAddressParams shippingAddress;
      private List<MetaDataParams> metaData;

      private Builder() {
      }

      public Builder customerId(String customerId) {
         this.customerId = customerId;
         return this;
      }

      public Builder paymentMethodId(String paymentMethodId) {
         this.paymentMethodId = paymentMethodId;
         return this;
      }

      public Builder amount(Double amount) {
         this.amount = amount;
         return this;
      }

      public Builder currency(String currency) {
         this.currency = currency;
         return this;
      }

      public Builder customerOnSession(Boolean customerOnSession) {
         this.customerOnSession = customerOnSession;
         return this;
      }

      public Builder browserInfo(BrowserInfo browserInfo) {
         this.browserInfo = browserInfo;
         return this;
      }

      public Builder statementDescriptor(String statementDescriptor) {
         this.statementDescriptor = statementDescriptor;
         return this;
      }

      public Builder description(String description) {
         this.description = description;
         return this;
      }

      public Builder shippingAddress(PostalAddressParams shippingAddress) {
         this.shippingAddress = shippingAddress;
         return this;
      }

      public Builder metaData(List<MetaDataParams> metaData) {
         this.metaData = metaData;
         return this;
      }

      public PaymentCreateParams build() {
         if (this.customerId != null && !this.customerId.isEmpty()) {
            if (this.paymentMethodId != null && !this.paymentMethodId.isEmpty()) {
               if (this.amount == null) {
                  throw new IllegalArgumentException("amount is required");
               } else if (this.currency != null && !this.currency.isEmpty()) {
                  ParamValidator.validateDescription(this.description);
                  MetaDataValidator.validate(this.metaData);
                  return new PaymentCreateParams(this);
               } else {
                  throw new IllegalArgumentException("currency is required");
               }
            } else {
               throw new IllegalArgumentException("paymentMethodId is required");
            }
         } else {
            throw new IllegalArgumentException("customerId is required");
         }
      }
   }
}
