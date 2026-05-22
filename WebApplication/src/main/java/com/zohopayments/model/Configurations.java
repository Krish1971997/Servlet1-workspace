package com.zohopayments.model;

import java.util.Collections;
import java.util.List;

public final class Configurations {
   private List<String> allowedPaymentMethods;
   private HostedPageResponse hostedPageParameters;

   Configurations() {
   }

   public List<String> getAllowedPaymentMethods() {
      return this.allowedPaymentMethods != null ? Collections.unmodifiableList(this.allowedPaymentMethods) : Collections.emptyList();
   }

   public HostedPageResponse getHostedPageParameters() {
      return this.hostedPageParameters;
   }

   public static final class HostedPageResponse {
      private String description;
      private String successUrl;
      private String failureUrl;
      private String name;
      private String email;
      private String phone;
      private String phoneCountryCode;
      private String udf1;
      private String udf2;
      private String udf3;
      private String udf4;
      private String udf5;

      HostedPageResponse() {
      }

      public String getDescription() {
         return this.description;
      }

      public String getSuccessUrl() {
         return this.successUrl;
      }

      public String getFailureUrl() {
         return this.failureUrl;
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

      public String getPhoneCountryCode() {
         return this.phoneCountryCode;
      }

      public String getUdf1() {
         return this.udf1;
      }

      public String getUdf2() {
         return this.udf2;
      }

      public String getUdf3() {
         return this.udf3;
      }

      public String getUdf4() {
         return this.udf4;
      }

      public String getUdf5() {
         return this.udf5;
      }
   }
}
