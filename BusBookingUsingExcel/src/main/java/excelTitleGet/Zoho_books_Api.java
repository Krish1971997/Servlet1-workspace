package excelTitleGet;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Zoho_books_Api {
		public static void main(String[] args) throws IOException {
			OkHttpClient client = new OkHttpClient();

			Request request = new Request.Builder()
			  .url("https://www.zohoapis.com/books/v3/purchaseorders/4783570000000228446/status/open?organization_id=840104509")
			  .post(null)
			  .addHeader("Authorization", "Zoho-oauthtoken 1000.f59a5ccf452d5d2a772d2e6971623573.4d2dfad97edafc99da225b9e24f8e0a3")
			  .build();

			Response response = client.newCall(request).execute();
			String jsonResponse = response.body().string();

	        // Pretty print JSON response
	        Gson gson = new GsonBuilder().setPrettyPrinting().create();
	        JsonElement jsonElement = JsonParser.parseString(jsonResponse);
	        String prettyJsonString = gson.toJson(jsonElement);

	        System.out.println(prettyJsonString);
		}
}
