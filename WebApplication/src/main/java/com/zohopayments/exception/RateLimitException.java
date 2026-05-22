package com.zohopayments.exception;

public class RateLimitException extends ZohoPaymentsAPIException {
   private static final long serialVersionUID = 1L;

   public RateLimitException(String codeString, String errorMessage) {
      super(429, codeString, errorMessage);
   }
}
