package com.zohopayments.net;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ZohoResponse {
   private final int statusCode;
   private final Map<String, List<String>> headers;
   private final String body;

   public ZohoResponse(int statusCode, Map<String, List<String>> headers, String body) {
      this.statusCode = statusCode;
      this.headers = headers != null ? deepUnmodifiable(headers) : Collections.emptyMap();
      this.body = body;
   }

   public int getStatusCode() {
      return this.statusCode;
   }

   public Map<String, List<String>> getHeaders() {
      return this.headers;
   }

   public String getBody() {
      return this.body;
   }

   private static Map<String, List<String>> deepUnmodifiable(Map<String, List<String>> src) {
      Map<String, List<String>> safe = new LinkedHashMap(src.size());

      for(Map.Entry<String, List<String>> e : src.entrySet()) {
         safe.put((String)e.getKey(), Collections.unmodifiableList(new ArrayList((Collection)e.getValue())));
      }

      return Collections.unmodifiableMap(safe);
   }
}
