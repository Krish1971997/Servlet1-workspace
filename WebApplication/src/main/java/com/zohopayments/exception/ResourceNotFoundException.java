package com.zohopayments.exception;

public class ResourceNotFoundException extends ZohoPaymentsAPIException {
   private static final long serialVersionUID = 1L;

   public ResourceNotFoundException(String codeString, String errorMessage) {
      super(404, codeString, errorMessage);
   }
}
