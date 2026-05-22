package com.workdrive.organizer.service;

import com.workdrive.organizer.model.WorkDriveFile;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class WorkDriveApiService {

	private static final Logger log = LoggerFactory.getLogger(WorkDriveApiService.class);
	private static final String BASE_URL = "https://workdrive.zoho.com/api/v1";

	private final ZohoTokenService tokenService;
	private final HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(15)).build();

	public WorkDriveApiService(ZohoTokenService tokenService) {
		this.tokenService = tokenService;
	}

	public List<WorkDriveFile> listFiles(String folderId) throws IOException {
		String url = BASE_URL + "/files/" + folderId + "/files";
		log.debug("Listing files in folder: {}", folderId);
		String responseBody = get(url);
		log.debug("Response: {}", responseBody);

		JSONObject json = new JSONObject(responseBody);
		JSONArray data = json.optJSONArray("data");
		List<WorkDriveFile> files = new ArrayList<>();
		if (data == null) {
			log.warn("No data array in response");
			return files;
		}

		for (int i = 0; i < data.length(); i++) {
			JSONObject item = data.getJSONObject(i);
			JSONObject attrs = item.optJSONObject("attributes");
			if (attrs == null || attrs.optBoolean("is_folder", false))
				continue;

			WorkDriveFile f = new WorkDriveFile();
			f.setId(item.optString("id"));
			f.setName(attrs.optString("name"));
			f.setExtension(attrs.optString("extn"));
			f.setType(attrs.optString("type"));
			f.setFolder(false);
			f.setModifiedTime(attrs.optString("modified_time"));
			f.setParentId(attrs.optString("parent_id"));
			f.setPermalink(attrs.optString("permalink"));
			JSONObject storage = attrs.optJSONObject("storage_info");
			if (storage != null)
				f.setSizeInBytes(storage.optLong("size_in_bytes", 0));

			log.debug("Found: {} ({})", f.getName(), f.getExtension());

			files.add(f);
			log.info("WorkDriveFile : " + files.size() + " : {}", f.toString());
		}
		log.info("Listed {} files in folder {}", files.size(), folderId);
		return files;
	}

	public boolean moveFile(String fileId, String destinationFolderId) throws IOException {
		String url = BASE_URL + "/files/" + fileId;
		JSONObject body = new JSONObject();
		body.put("data", new JSONObject().put("attributes", new JSONObject().put("parent_id", destinationFolderId))
				.put("type", "files"));

		log.debug("PATCH {} body: {}", url, body);
		int status = patch(url, body.toString());
		boolean success = (status == 200 || status == 204);
		if (success)
			log.info("Moved {} → {} [HTTP {}]", fileId, destinationFolderId, status);
		else
			log.error("Move failed {} → {} [HTTP {}]", fileId, destinationFolderId, status);
		return success;
	}

	private String get(String url) throws IOException {
		try {
			HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).timeout(Duration.ofSeconds(15))
					.header("Authorization", "Zoho-oauthtoken " + tokenService.getAccessToken())
					.header("Accept", "application/vnd.api+json").GET().build();

			HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
			log.debug("GET {} → HTTP {}", url, resp.statusCode());
			if (resp.statusCode() != 200)
				throw new IOException("GET failed [HTTP " + resp.statusCode() + "]: " + resp.body());
			return resp.body();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new IOException("Interrupted", e);
		}
	}

	private int patch(String url, String jsonBody) throws IOException {
		try {
			HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).timeout(Duration.ofSeconds(15))
					.header("Authorization", "Zoho-oauthtoken " + tokenService.getAccessToken())
					.header("Accept", "application/vnd.api+json")
					.header("Content-Type", "application/vnd.api+json")
					.method("PATCH", HttpRequest.BodyPublishers.ofString(jsonBody)).build();

			HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
			log.debug("PATCH {} → HTTP {} | Body: {}", url, resp.statusCode(), resp.body());
			return resp.statusCode();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new IOException("Interrupted", e);
		}
	}
}