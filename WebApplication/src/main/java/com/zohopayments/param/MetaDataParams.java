package com.zohopayments.param;

public final class MetaDataParams {
   private final String key;
   private final String value;

   public MetaDataParams(String key, String value) {
      this.key = key;
      this.value = value;
   }

   public String getKey() {
      return this.key;
   }

   public String getValue() {
      return this.value;
   }
}
