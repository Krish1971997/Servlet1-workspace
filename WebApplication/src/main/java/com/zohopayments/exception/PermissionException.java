package com.zohopayments.exception;

public class PermissionException extends ZohoPaymentsAPIException {
   private static final long serialVersionUID = 1L;

   public PermissionException(String codeString, String errorMessage) {
      super(403, codeString, errorMessage);
   }
}
