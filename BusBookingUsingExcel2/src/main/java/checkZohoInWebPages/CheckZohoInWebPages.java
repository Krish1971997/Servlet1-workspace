//package checkZohoInWebPages;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.List;
//import java.util.ArrayList;
//
//public class CheckZohoInWebPages {
//
//    public static void main(String[] args) {
//        // List of URLs to check
//        List<String> urls = new ArrayList<>();
//        urls.add("https://zoho.to/uqD");
//        urls.add("https://careers.zohocorp.com/forms/fcc89b5ebd373d598e0224d10f2199d1dbfb668a24f5a6790dd6d5cd0eea3b13");
//        // Add more URLs as needed
//
//        // Check each URL for the word "Zoho"
//        for (String url : urls) {
//            try {
//                if (isZohoPresent(url)) {
//                    System.out.println("The word 'Zoho' was found on the page: " + url);
//                } else {
//                    System.out.println("The word 'Zoho' was NOT found on the page: " + url);
//                }
//            } catch (Exception e) {
//                System.out.println("Failed to check the page: " + url);
//                e.printStackTrace();
//            }
//        }
//    }
//
//    // Method to check if the word "Zoho" is present in the content of the given URL
//    public static boolean isZohoPresent(String urlString) throws Exception {
//        URL url = new URL(urlString);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("GET");
//
//        // Read the content from the URL
//        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//        String inputLine;
//        StringBuilder content = new StringBuilder();
//        while ((inputLine = in.readLine()) != null) {
//            content.append(inputLine);
//        }
//        in.close();
//        connection.disconnect();
//
//        // Check if the content contains the word "Zoho"
//        return content.toString().toLowerCase().contains("zoho");
//    }
//}
