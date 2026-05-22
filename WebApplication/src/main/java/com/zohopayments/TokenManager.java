package com.zohopayments;

final class TokenManager {
   private volatile String accessToken;

   TokenManager(String accessToken) {
      if (accessToken != null && !accessToken.isEmpty()) {
         this.accessToken = accessToken;
      } else {
         throw new IllegalArgumentException("accessToken must not be null or empty");
      }
   }

   String getAccessToken() {
      return this.accessToken;
   }

   void updateToken(String newAccessToken) {
      if (newAccessToken != null && !newAccessToken.isEmpty()) {
         this.accessToken = newAccessToken;
      } else {
         throw new IllegalArgumentException("newAccessToken must not be null or empty");
      }
   }
}
