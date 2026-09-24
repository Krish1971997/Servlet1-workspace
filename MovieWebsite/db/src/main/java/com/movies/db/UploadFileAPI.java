package com.movies.db;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

public class UploadFileAPI {

	// Zoho API Configuration
	private static final String CLIENT_ID = "1000.I7L8AIDAW8EIVJ0PW0O84NKMHAXBFV";
	private static final String CLIENT_SECRET = "1c2192e08af94e368a964ac490624ef803f91f14ab";
	private static final String REFRESH_TOKEN = "1000.f101d469d9cb0f5d501e5c11c8748071.c3319f8c5edbbe2eab937a7986d4b9e2";
	private static final String ZOHO_ACCOUNTS_URL = "https://accounts.zoho.com/oauth/v2/token";
	private static final String WORDRIVE_API_URL = "https://workdrive.zoho.com/api/v1/upload";
	private static final String WORKDRIVE_LIST_URL = "https://www.zohoapis.com/workdrive/api/v1/files/";
	private static final String WORKDRIVE_DOWNLOAD_URL = "https://download-accl.zoho.com/v1/workdrive/download/";
	private static final String WORKDRIVE_FOLDER_ID = "e8cpf113df0584d5b419d984b185e2df899bf";
	private static String ACCESS_TOKEN = null;

	private final HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(15))
			.version(HttpClient.Version.HTTP_1_1).build();

	// ------------------------------------------------------------------
	// DOWNLOAD (new)
	// ------------------------------------------------------------------

	/**
	 * Finds the file (by name) inside WORKDRIVE_FOLDER_ID and downloads it.
	 *
	 * @return file bytes, or null if the file does not exist in the folder yet.
	 * @throws IOException on any API / network failure (caller should abort).
	 */
	public byte[] downloadExcelFromWorkDrive(String fileName) throws Exception {
		refreshAccessToken();
		String fileId = findFileIdByName(fileName);
		if (fileId == null) {
			return null;
		}
		System.out.println("Found " + fileName + " in WorkDrive. Resource ID: " + fileId);
		return downloadFile(fileId);
	}

	/** Lists files in the upload folder and returns the resource id of the matching file name. */
	private String findFileIdByName(String fileName) throws IOException {
		String nameWithoutExt = fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf('.')) : fileName;
		int limit = 50;
		int offset = 0;

		try {
			while (true) {
				String url = WORKDRIVE_LIST_URL + WORKDRIVE_FOLDER_ID
						+ "/files?filter%5Btype%5D=allfiles&page%5Blimit%5D=" + limit + "&page%5Boffset%5D=" + offset;

				HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).timeout(Duration.ofSeconds(60))
						.header("Authorization", "Zoho-oauthtoken " + ACCESS_TOKEN)
						.header("Accept", "application/vnd.api+json").GET().build();

				HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
				if (resp.statusCode() != 200) {
					throw new IOException("List files failed [HTTP " + resp.statusCode() + "]: " + resp.body());
				}

				JSONArray data = new JSONObject(resp.body()).optJSONArray("data");
				if (data == null || data.length() == 0) {
					return null;
				}
				
				System.out.println("File Data : "+data.toString());

				for (int i = 0; i < data.length(); i++) {
					JSONObject item = data.getJSONObject(i);
					JSONObject attrs = item.optJSONObject("attributes");
					if (attrs == null) {
						continue;
					}
					String name = attrs.optString("name", "");
					String display = attrs.optString("display_attr_name", "");
					if (fileName.equalsIgnoreCase(name) || (name.isEmpty() && nameWithoutExt.equalsIgnoreCase(display))) {
						return item.getString("id");
					}
				}

				if (data.length() < limit) {
					return null; // last page reached, file not found
				}
				offset += limit;
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new IOException("List files interrupted", e);
		}
	}

	public byte[] downloadFile(String fileId) throws IOException {
		String url = WORKDRIVE_DOWNLOAD_URL + fileId;
		System.out.println("Downloading file: " + fileId);

		try {
			HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).timeout(Duration.ofSeconds(60))
					.header("Authorization", "Zoho-oauthtoken " + ACCESS_TOKEN).GET().build();

			HttpResponse<byte[]> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofByteArray());
			System.out.println("Download HTTP status: " + resp.statusCode());

			if (resp.statusCode() != 200) {
				throw new IOException(
						"Download failed [HTTP " + resp.statusCode() + "]: " + new String(resp.body(), StandardCharsets.UTF_8));
			}

			System.out.println("Downloaded " + resp.body().length + " bytes for fileId: " + fileId);
			return resp.body();

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new IOException("Download interrupted", e);
		}
	}

	// ------------------------------------------------------------------
	// UPLOAD (unchanged)
	// ------------------------------------------------------------------
	public void uploadToWorkDrive(File file) throws Exception {
		if (!file.exists()) {
			throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
		}

		refreshAccessToken();

		String boundary = "Boundary-" + UUID.randomUUID().toString().replace("-", "");

		// Multipart body manually build (no external library)
		ByteArrayOutputStream bodyStream = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(bodyStream));

		// -- parent_id field
		writer.append("--").append(boundary).append("\r\n");
		writer.append("Content-Disposition: form-data; name=\"parent_id\"").append("\r\n\r\n");
		writer.append(WORKDRIVE_FOLDER_ID).append("\r\n");

		// -- override-name-exist field
		writer.append("--").append(boundary).append("\r\n");
		writer.append("Content-Disposition: form-data; name=\"override-name-exist\"").append("\r\n\r\n");
		writer.append("true").append("\r\n");

		// -- file content field header
		writer.append("--").append(boundary).append("\r\n");
		writer.append("Content-Disposition: form-data; name=\"content\"; filename=\"").append(file.getName())
				.append("\"").append("\r\n");
		writer.append("Content-Type: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
				.append("\r\n\r\n");
		writer.flush();

		// File bytes
		bodyStream.write(Files.readAllBytes(file.toPath()));

		// Closing boundary
		writer = new PrintWriter(new OutputStreamWriter(bodyStream));
		writer.append("\r\n--").append(boundary).append("--\r\n");
		writer.flush();

		byte[] body = bodyStream.toByteArray();

		// HTTP Request
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(WORDRIVE_API_URL))
				.header("Authorization", "Zoho-oauthtoken " + ACCESS_TOKEN)
				.header("Content-Type", "multipart/form-data; boundary=" + boundary)
				.POST(HttpRequest.BodyPublishers.ofByteArray(body)).build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		System.out.println("HTTP Status : " + response.statusCode());
		System.out.println("Response    : " + response.body());

		if (response.statusCode() == 200 || response.statusCode() == 201) {
			JSONObject json = new JSONObject(response.body());
			System.out.println("✅ Upload Success!");

			JSONObject fileData = json.getJSONArray("data").getJSONObject(0);
			JSONObject attributes = fileData.getJSONObject("attributes");
			System.out.println(attributes);

			System.out.println("File Name   : " + attributes.optString("FileName", "N/A"));
			System.out.println("Resource ID : " + attributes.optString("resource_id", "N/A"));
			System.out.println("Parent ID   : " + attributes.optString("parent_id", "N/A"));
		}
	}

	private void refreshAccessToken() throws IOException, InterruptedException {
		int maxAttempts = 3;
		for (int attempt = 1; attempt <= maxAttempts; attempt++) {
			try {
				refreshAccessTokenOnce();
				return;
			} catch (java.net.SocketTimeoutException | java.net.ConnectException e) {
				System.err.println("Token refresh attempt " + attempt + "/" + maxAttempts + " failed: " + e.getMessage());
				if (attempt == maxAttempts) {
					throw e;
				}
				Thread.sleep(3000L * attempt);
			}
		}
	}

	private void refreshAccessTokenOnce() throws IOException {

		String body = "grant_type=refresh_token" + "&client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET
				+ "&refresh_token=" + REFRESH_TOKEN;

		HttpURLConnection conn = (HttpURLConnection) new URL(ZOHO_ACCOUNTS_URL).openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setConnectTimeout(30_000);
		conn.setReadTimeout(30_000);

		try (OutputStream os = conn.getOutputStream()) {
			os.write(body.getBytes(StandardCharsets.UTF_8));
		}

		int status = conn.getResponseCode();
		InputStream is = (status == 200) ? conn.getInputStream() : conn.getErrorStream();
		String response = new String(is.readAllBytes(), StandardCharsets.UTF_8);

		if (status != 200) {
			throw new IOException("Token refresh failed [HTTP " + status + "]: " + response);
		}

		JSONObject json = new JSONObject(response);
		ACCESS_TOKEN = json.getString("access_token");
//		int expiresIn = json.optInt("expires_in", 3600);
//		this.expiresAt = Instant.now().plusSeconds(expiresIn);

//      log.trace("EXIT ZohoTokenService.refreshAccessToken()");
		System.out.println("✅ Access Token refreshed successfully.");
	}

}