package excelTitleGet;

import java.io.*;
import java.sql.*;

public class ImageInsertionExample {
    public static void main(String[] args) {
        try {
            // Establish the connection to the SQL Server database
            Connection connection = Jdbc_Connection.getConnection();

            // Prepare the SQL statement to insert the image
            String sql = "INSERT INTO Images (ImageData, ImageName) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            // Read the image file
            
            File imageFile = new File("C:/Users/Admin/Downloads/zoho-logo_brandlogos.net_kduhg-512x512.png");
            FileInputStream fis = new FileInputStream(imageFile);
            byte[] imageData = new byte[(int) imageFile.length()];
            fis.read(imageData);
            fis.close();

            // Set the image data and image name in the SQL statement
            statement.setBytes(1, imageData);
            statement.setString(2, "ZohoLogo.jpg");

            // Execute the SQL statement to insert the image
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Image inserted successfully.");
            } else {
                System.out.println("Failed to insert image.");
            }

            // Close the resources
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}