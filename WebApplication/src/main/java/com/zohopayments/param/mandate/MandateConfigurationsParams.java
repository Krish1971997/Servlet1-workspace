package com.zohopayments.param.mandate;

import com.zohopayments.param.HostedPageParams;

public final class MandateConfigurationsParams {
   private final HostedPageParams hostedPageParameters;

   private MandateConfigurationsParams(Builder b) {
      this.hostedPageParameters = b.hostedPageParameters;
   }

   public HostedPageParams getHostedPageParameters() {
      return this.hostedPageParameters;
   }

   public static Builder builder() {
      return new Builder();
   }

   public static final class Builder {
      private HostedPageParams hostedPageParameters;

      private Builder() {
      }

      public Builder hostedPageParameters(HostedPageParams hostedPageParameters) {
         this.hostedPageParameters = hostedPageParameters;
         return this;
      }

      public MandateConfigurationsParams build() {
         return new MandateConfigurationsParams(this);
      }
   }
}
