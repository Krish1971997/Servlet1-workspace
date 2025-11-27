package com.movies.db;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;

public class ZohoSheetExporter {

	// Zoho API Configuration
	private static final String CLIENT_ID = "1000.I7L8AIDAW8EIVJ0PW0O84NKMHAXBFV";
	private static final String CLIENT_SECRET = "1c2192e08af94e368a964ac490624ef803f91f14ab";
	private static final String REFRESH_TOKEN = "1000.400e4b534a669d1387429d94153462a0.68b5cac928f0bfc5fa46e4b72a17bf6d";
	private static final String ZOHO_ACCOUNTS_URL = "https://accounts.zoho.com"; // .com for US
	private static final String ZOHO_SHEET_API_URL = "https://sheet.zoho.com/api/v2/"; // .com for US

	// Existing Workbook Configuration
	private static final String EXISTING_WORKBOOK_ID = "2pd2w3ecc4aec5130433f8a0c9439f2829e46";

	// Database Configuration
	private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=Krishna_Testing;trustServerCertificate=true";
	private static final String DB_USER = "sa";
	private static final String DB_PASSWORD = "15848";

	private static String accessToken;
	private static HttpClient httpClient = HttpClient.newHttpClient();

	public static void main(String[] args) {
		try {
			// Step 1: Get Access Token
			System.out.println("Getting Zoho access token...");
			accessToken = getAccessToken();
			System.out.println("Access token: " + accessToken);

			// Step 2: Get worksheet ID from existing workbook
			System.out.println("Getting worksheet details from existing workbook...");
			String worksheetId = getWorksheetId(EXISTING_WORKBOOK_ID);
			System.out.println("worksheetId : " + worksheetId);

			// Step 3: Clear existing data (optional)
			System.out.println("Do you want to clear existing data? (Skipping for safety)");
			// clearWorksheet(EXISTING_WORKBOOK_ID, worksheetId); // Uncomment to clear

			// In main:
			String worksheetName = getWorksheetName(EXISTING_WORKBOOK_ID);
			System.out.println("worksheetName : " + worksheetName);
			exportDataToZohoSheet(EXISTING_WORKBOOK_ID, worksheetName);

			System.out.println("\n=== Export completed successfully! ===");
			System.out.println("Workbook Link: https://sheet.zoho.com/sheet/open/" + EXISTING_WORKBOOK_ID);

		} catch (Exception e) {
			System.err.println("Error during export: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Get OAuth2 access token using refresh token
	 */
	private static String getAccessToken() throws IOException, InterruptedException {
		String tokenUrl = ZOHO_ACCOUNTS_URL + "/oauth/v2/token";

		String requestBody = "refresh_token=" + REFRESH_TOKEN + "&client_id=" + CLIENT_ID + "&client_secret="
				+ CLIENT_SECRET + "&grant_type=refresh_token";

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(tokenUrl))
				.header("Content-Type", "application/x-www-form-urlencoded")
				.POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		if (response.statusCode() != 200) {
			throw new RuntimeException("Failed to get access token: " + response.body());
		}

		JSONObject jsonResponse = new JSONObject(response.body());
		return jsonResponse.getString("access_token");
	}

	/**
	 * Get worksheet name from workbook (returns the first one's name)
	 */
	private static String getWorksheetName(String workbookId) throws IOException, InterruptedException {
		String url = ZOHO_SHEET_API_URL + workbookId;

		String requestBody = "method=worksheet.list"; // Form-urlencoded body

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
				.header("Authorization", "Zoho-oauthtoken " + accessToken)
				.header("Content-Type", "application/x-www-form-urlencoded")
				.POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		if (response.statusCode() != 200) {
			throw new RuntimeException("Failed to get workbook details: " + response.body());
		}

		JSONObject jsonResponse = new JSONObject(response.body());
		if (!jsonResponse.getString("status").equals("success")) {
			throw new RuntimeException("API returned failure: " + jsonResponse.toString());
		}

		JSONArray worksheets = jsonResponse.getJSONArray("worksheet_names");

		// Get the first worksheet
		String worksheetName = worksheets.getJSONObject(0).getString("worksheet_name");
		String worksheetId = worksheets.getJSONObject(0).getString("worksheet_id"); // Optional: log ID if needed

		System.out.println("Found worksheet: " + worksheetName + " (ID: " + worksheetId + ")");
		return worksheetName;
	}

	/**
	 * Get worksheet ID from workbook
	 */
	private static String getWorksheetId(String workbookId) throws IOException, InterruptedException {
		String url = ZOHO_SHEET_API_URL + workbookId;

		String requestBody = "method=worksheet.list"; // Form-urlencoded body

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
				.header("Authorization", "Zoho-oauthtoken " + accessToken)
				.header("Content-Type", "application/x-www-form-urlencoded") // Key: form-urlencoded, not JSON
				.POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		if (response.statusCode() != 200) {
			throw new RuntimeException("Failed to get workbook details: " + response.body());
		}

		JSONObject jsonResponse = new JSONObject(response.body());
		if (!jsonResponse.getString("status").equals("success")) {
			throw new RuntimeException("API returned failure: " + jsonResponse.toString());
		}

		JSONArray worksheets = jsonResponse.getJSONArray("worksheet_names");

		// Get the first worksheet (usually "Sheet1")
		String worksheetId = worksheets.getJSONObject(0).getString("worksheet_id");
		String worksheetName = worksheets.getJSONObject(0).getString("worksheet_name");

		System.out.println("Found worksheet: " + worksheetName + " (ID: " + worksheetId + ")");
		return worksheetId;
	}

	/**
	 * Fetch all data row indices (1-based, skips row 1 for headers) for deletion.
	 * Paginates if >1000 rows.
	 */
	private static JSONArray getDataRowIndices(String workbookId, String worksheetName)
			throws IOException, InterruptedException {
		JSONArray allIndices = new JSONArray();
		int startIndex = 2; // Start after potential header (row 1)
		int count = 1000; // Max per fetch
		boolean hasMore = true;

		while (hasMore) {
			String url = ZOHO_SHEET_API_URL + workbookId + "?records_start_index=" + startIndex + "&count=" + count;

			JSONObject payload = new JSONObject();
			payload.put("method", "worksheet.records.fetch");
			payload.put("worksheet_name", worksheetName);

			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
					.header("Authorization", "Zoho-oauthtoken " + accessToken)
					.header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(payload.toString())).build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() != 200) {
				throw new RuntimeException("Failed to fetch rows for clear: " + response.body());
			}

			JSONObject jsonResp = new JSONObject(response.body());
			if (!"success".equals(jsonResp.optString("status"))) {
				throw new RuntimeException("Fetch failed: " + jsonResp.toString());
			}

			JSONArray records = jsonResp.optJSONArray("records");
			if (records == null || records.length() == 0) {
				hasMore = false;
				break;
			}

			for (int i = 0; i < records.length(); i++) {
				JSONObject record = records.getJSONObject(i);
				allIndices.put(record.getInt("row_index"));
			}

			startIndex += records.length();
			hasMore = records.length() == count; // Continue if full batch
		}

		return allIndices;
	}

	/**
	 * Clear all data from worksheet (optional - use with caution!)
	 */
	private static void clearWorksheet(String workbookId, String worksheetName)
			throws IOException, InterruptedException {
		// Step 1: Fetch all data row indices (skip row 1 if headers)
		JSONArray rowIndices = getDataRowIndices(workbookId, worksheetName);
		if (rowIndices.length() == 0) {
			System.out.println("No data rows to clear");
			return;
		}

		String url = ZOHO_SHEET_API_URL + workbookId;

		JSONObject payload = new JSONObject();
		payload.put("method", "worksheet.records.delete");
		payload.put("worksheet_name", worksheetName);
		payload.put("row_array", rowIndices); // Array of 1-based indices to delete

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
				.header("Authorization", "Zoho-oauthtoken " + accessToken).header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(payload.toString())).build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		if (response.statusCode() == 200) {
			JSONObject jsonResp = new JSONObject(response.body());
			if ("success".equals(jsonResp.optString("status"))) {
				System.out.println("Worksheet cleared successfully (" + rowIndices.length() + " rows deleted)");
			} else {
				System.err.println("Clear failed (status not success): " + response.body());
			}
		} else {
			System.err.println("Failed to clear worksheet: " + response.body());
		}
	}

	private static void exportDataToZohoSheet(String workbookId, String worksheetName) throws Exception { // Changed
																											// param to
																											// worksheetName
		Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

		clearWorksheet(workbookId, worksheetName); // Now safe; comment out if skipping
		addHeaders(workbookId, worksheetName);

		// Fetch data...
		String query = "SELECT id, name, sublink, category, link FROM Movies ORDER BY id";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);

		JSONArray batchData = new JSONArray();
		int rowIndex = 2; // After headers
		int totalRows = 0;

		while (rs.next()) {
			JSONObject row = new JSONObject();
			row.put("0", rs.getInt("id")); // Column A (index 0)
			row.put("1", rs.getString("name") != null ? rs.getString("name") : ""); // B (1)
			row.put("2", rs.getString("sublink") != null ? rs.getString("sublink") : ""); // C (2)
			row.put("3", rs.getString("category") != null ? rs.getString("category") : ""); // D (3)
			row.put("4", rs.getString("link") != null ? rs.getString("link") : ""); // E (4)

			batchData.put(row);
			totalRows++;

			if (batchData.length() >= 50) {
				addDataRows(workbookId, worksheetName, rowIndex, batchData);
				rowIndex += batchData.length();
				batchData = new JSONArray();
				System.out.println("Exported " + totalRows + " rows...");
				Thread.sleep(500);
			}
		}

		if (batchData.length() > 0) {
			addDataRows(workbookId, worksheetName, rowIndex, batchData);
			System.out.println("Total rows exported: " + totalRows);
		}

		rs.close();
		stmt.close();
		conn.close();
	}

	/**
	 * Add header row to worksheet at row 1 using indices (avoids header mismatch)
	 */
	private static void addHeaders(String workbookId, String worksheetName) throws IOException, InterruptedException {
		String url = ZOHO_SHEET_API_URL + workbookId + "?start_row=1";

		JSONArray data = new JSONArray();
		JSONObject headerRow = new JSONObject();
		headerRow.put("0", "ID"); // Column A
		headerRow.put("1", "Name"); // Column B
		headerRow.put("2", "Sublink"); // Column C
		headerRow.put("3", "Category"); // Column D
		headerRow.put("4", "Link"); // Column E
		data.put(headerRow);

		JSONObject payload = new JSONObject();
		payload.put("method", "worksheet.records.add");
		payload.put("worksheet_name", worksheetName);
		payload.put("row_data", data);

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
				.header("Authorization", "Zoho-oauthtoken " + accessToken).header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(payload.toString())).build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		if (response.statusCode() != 200) {
			System.err.println("Warning: Failed to add headers (may already exist): " + response.body());
		} else {
			System.out.println("Headers added successfully");
		}
	}

	/**
	 * Add data rows to worksheet (appends to end; uses indices for consistency)
	 */
	private static void addDataRows(String workbookId, String worksheetName, int startRow, JSONArray data)
			throws IOException, InterruptedException {

		String url = ZOHO_SHEET_API_URL + workbookId + (startRow > 0 ? "?start_row=" + startRow : ""); // Use start_row
																										// if provided,
																										// else append

		JSONObject payload = new JSONObject();
		payload.put("method", "worksheet.records.add");
		payload.put("worksheet_name", worksheetName);
		payload.put("row_data", data);

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
				.header("Authorization", "Zoho-oauthtoken " + accessToken).header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(payload.toString())).build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		if (response.statusCode() != 200) {
			throw new RuntimeException("Failed to add data rows: " + response.body());
		}
	}
}