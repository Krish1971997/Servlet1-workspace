package com.zohopayments;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

final class ApiResponse {
   private final int statusCode;
   private final JsonObject body;

   ApiResponse(int statusCode, JsonObject body) {
      this.statusCode = statusCode;
      this.body = body;
   }

   JsonObject getBody() {
      return this.body;
   }

   String getCodeString() {
      if (this.body != null && this.body.has("code")) {
         try {
            JsonElement codeElement = this.body.get("code");
            if (codeElement != null && !codeElement.isJsonNull()) {
               String raw = codeElement.getAsString();
               if (raw == null) {
                  return null;
               } else {
                  String trimmed = raw.trim();
                  return trimmed.isEmpty() ? null : trimmed;
               }
            } else {
               return null;
            }
         } catch (ClassCastException | UnsupportedOperationException | IllegalStateException var4) {
            return null;
         }
      } else {
         return null;
      }
   }

   String getMessage() {
      if (this.body != null && this.body.has("message")) {
         JsonElement el = this.body.get("message");
         if (el != null && el.isJsonPrimitive()) {
            return el.getAsString();
         }
      }

      return null;
   }

   boolean isSuccess() {
      return this.statusCode >= 200 && this.statusCode < 300;
   }
}
