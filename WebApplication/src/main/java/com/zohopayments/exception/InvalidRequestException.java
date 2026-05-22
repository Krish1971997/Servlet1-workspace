package com.zohopayments.exception;

public class InvalidRequestException extends ZohoPaymentsAPIException {
   private static final long serialVersionUID = 1L;

   public InvalidRequestException(int httpStatusCode, String codeString, String errorMessage) {
      super(httpStatusCode, codeString, errorMessage);
   }
}
