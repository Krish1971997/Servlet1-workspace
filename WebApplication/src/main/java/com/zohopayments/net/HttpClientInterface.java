package com.zohopayments.net;

public interface HttpClientInterface {
   ZohoResponse execute(ZohoRequest var1);

   default void close() {
   }
}
