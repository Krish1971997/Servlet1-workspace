package com.zoho.books;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UpdateStatus {
	
	private static final String ACCESS_TOKEN = "1000.8ef2608bde08cd3f5d0782ae460161f5.389a28a1d5dcfcc4bf7393998d70118f";
    private static final String ORGANIZATION_ID = "840104509"; // Replace with your Zoho Books Organization ID
	public static void main(String[] args) throws IOException {

	OkHttpClient client = new OkHttpClient();
	Request request = new Request.Builder()
	  .url("https://www.zohoapis.com/books/v3/salesorders/4783570000000194013/status/open?organization_id=840104509")
	  .post(null)
	  .addHeader("Authorization", "Zoho-oauthtoken 1000.8ef2608bde08cd3f5d0782ae460161f5.389a28a1d5dcfcc4bf7393998d70118f")
	  .build();

	Response response = client.newCall(request).execute();

}
}