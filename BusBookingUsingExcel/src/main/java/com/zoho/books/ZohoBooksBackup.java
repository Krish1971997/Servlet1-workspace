package com.zoho.books;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ZohoBooksBackup {

    // Replace with your Zoho Books API OAuth access token
    private static final String ACCESS_TOKEN = "1000.8ef2608bde08cd3f5d0782ae460161f5.389a28a1d5dcfcc4bf7393998d70118f";
    private static final String ORGANIZATION_ID = "840104509"; // Replace with your Zoho Books Organization ID

    public static String getInvoicesFromZohoBooks() {
        String apiUrl = "https://books.zoho.com/api/v3/invoices?organization_id=" + ORGANIZATION_ID;
        try {
            // Set up the connection to Zoho Books API
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Zoho-oauthtoken " + ACCESS_TOKEN);

            // Read the response from the API
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Return the response containing invoice data (JSON format)
            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveInvoicesToFile(String data, String filePath) {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(data);  // Write the invoice data (JSON) to a file
            System.out.println("Invoice data saved to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String invoicesData = getInvoicesFromZohoBooks();
        if (invoicesData != null) {
            System.out.println("Fetched Invoices: " + invoicesData);

            // Save the fetched invoice data to a JSON file
            saveInvoicesToFile(invoicesData, "zoho_books_invoices.json");
        } else {
            System.out.println("Failed to fetch invoices.");
        }
    }
}
