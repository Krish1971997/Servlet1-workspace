package javaSendapi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class JavaSendapi {
  public static void main(String[] args) throws Exception {
  String postUrl = "https://zeptomail.zoho.com/v1.1/email";
  BufferedReader br = null;
  HttpURLConnection conn = null;
  String output = null;
  StringBuffer sb = new StringBuffer();
  try {
    URL url = new URL(postUrl);
    conn = (HttpURLConnection) url.openConnection();
    conn.setDoOutput(true);
    conn.setRequestMethod("POST");
    conn.setRequestProperty("Content-Type", "application/json");
    conn.setRequestProperty("Accept", "application/json");
    conn.setRequestProperty("Authorization", "[Authorization key]");
    JSONObject object = new JSONObject("{\n" +
    "  \"from\": {\n" +
    "    \"address\": \"yourname@yourdomain.com\"\n" +
    "  },\n" +
    "  \"to\": [\n" +
    "    {\n" +
    "      \"email_address\": {\n" +
    "        \"address\": \"receiver@yourdomain.com\",\n" +
    "        \"name\": \"Receiver\"\n" +
    "      }\n" +
    "    }\n" +
    "  ],\n" +
    "  \"subject\": \"Test Email\",\n" +
    "  \"htmlbody\": \" Test email sent successfully.\"\n" +
    "}");
    OutputStream os = conn.getOutputStream();
    os.write(object.toString().getBytes());
    os.flush();
    br = new BufferedReader(
    new InputStreamReader((conn.getInputStream()))
    );
    while ((output = br.readLine()) != null) {
    sb.append(output);
    }
    System.out.println(sb.toString());
  } catch (Exception e) {
      br = new BufferedReader(
        new InputStreamReader((conn.getErrorStream()))
      );
      while ((output = br.readLine()) != null) {
        sb.append(output);
      }
      System.out.println(sb.toString());
    } finally {
        try {
          if (br != null) {
          br.close();
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
        try {
          if (conn != null) {
            conn.disconnect();
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
  }
}