package com.zohopayments.net;

public enum RequestMethod {
   GET,
   POST,
   PUT,
   DELETE;

   // $FF: synthetic method
   private static RequestMethod[] $values() {
      return new RequestMethod[]{GET, POST, PUT, DELETE};
   }
}
