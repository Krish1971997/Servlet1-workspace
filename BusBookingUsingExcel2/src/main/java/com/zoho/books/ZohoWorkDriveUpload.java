package com.zoho.books;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ZohoWorkDriveUpload {

    // Replace with your Zoho WorkDrive OAuth access token
    private static final String WORKDRIVE_ACCESS_TOKEN = "<your_zoho_workdrive_oauth_access_token>";
    private static final String FOLDER_ID = "<your_folder_id>"; // Replace with your folder ID

    public static void uploadFileToZohoWorkDrive(String filePath, String fileName) {
        try {
            // API endpoint to upload a file
            String uploadUrl = "https://workdrive.zoho.com/api/v1/files/upload?folder_id=" + FOLDER_ID;

            // Open a connection to the Zoho WorkDrive API
            HttpURLConnection connection = (HttpURLConnection) new URL(uploadUrl).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", "Zoho-oauthtoken " + WORKDRIVE_ACCESS_TOKEN);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=boundary");

            // Prepare the file to upload
            File fileToUpload = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(fileToUpload);
            OutputStream outputStream = connection.getOutputStream();

            // Set the boundary for multipart/form-data
            String boundary = "boundary";
            String lineEnd = "\r\n";
            String twoHyphens = "--";

            // Write the file data to the output stream
            outputStream.write((twoHyphens + boundary + lineEnd).getBytes());
            outputStream.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"" + lineEnd).getBytes());
            outputStream.write(("Content-Type: application/octet-stream" + lineEnd).getBytes());
            outputStream.write(lineEnd.getBytes());

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.write(lineEnd.getBytes());
            outputStream.write((twoHyphens + boundary + twoHyphens + lineEnd).getBytes());

            // Close streams
            fileInputStream.close();
            outputStream.flush();
            outputStream.close();

            // Check the response
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                System.out.println("File uploaded successfully to Zoho WorkDrive.");
            } else {
                System.out.println("Failed to upload file. Response Code: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Save the fetched data to a file
        String invoiceData = ZohoBooksBackup.getInvoicesFromZohoBooks(); // Reuse the ZohoBooksBackup method
        if (invoiceData != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("zoho_books_backup.json"))) {
                writer.write(invoiceData); // Save the data as a JSON file
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Upload the file to Zoho WorkDrive
            uploadFileToZohoWorkDrive("zoho_books_backup.json", "zoho_books_backup.json");
        }
    }
}
