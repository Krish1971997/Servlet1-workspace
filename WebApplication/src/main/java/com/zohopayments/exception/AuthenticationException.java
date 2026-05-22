package com.zohopayments.exception;

public class AuthenticationException extends ZohoPaymentsAPIException {
   private static final long serialVersionUID = 1L;

   public AuthenticationException(String codeString, String errorMessage) {
      super(401, codeString, errorMessage);
   }
}
