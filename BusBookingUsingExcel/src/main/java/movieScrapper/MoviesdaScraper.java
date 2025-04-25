package movieScrapper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MoviesdaScraper {

//Class to store movie data
	static class MovieData {
		private String title;
		private String url;
		private String category;

		public MovieData(String title, String url, String category) {
			this.title = title;
			this.url = url;
			this.category = category;
		}

		public String getTitle() {
			return title;
		}

		public String getUrl() {
			return url;
		}

		public String getCategory() {
			return category;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null || getClass() != obj.getClass())
				return false;
			MovieData movieData = (MovieData) obj;
			return url.equals(movieData.url);
		}

		@Override
		public int hashCode() {
			return url.hashCode();
		}
	}

//Track visited URLs to prevent infinite loops
	private static Set<String> visitedUrls = new HashSet<>();
	private static final int MAX_RETRY = 3;
	private static final int DELAY_BETWEEN_REQUESTS = 1500; // 1.5 seconds to be polite

	public static void main(String[] args) {
		// Base URL of the website (using a working domain)
		String baseUrl = "https://www.moviesda.run";

		try {
			System.out.println("Starting to scrape Moviesda website...");

			// List to store all movie data
			List<MovieData> allMovies = new ArrayList<>();

			// Start with the base URL
			Document mainPage = connectToUrl(baseUrl);
			if (mainPage != null) {
				System.out.println("Successfully connected to main page: " + mainPage.title());

				// First, extract category links from the main page
				Map<String, String> categoryLinks = extractCategoryLinks(mainPage, baseUrl);
				System.out.println("Found " + categoryLinks.size() + " category links");

				// Process each category
				for (Map.Entry<String, String> category : categoryLinks.entrySet()) {
					String categoryName = category.getKey();
					String categoryUrl = category.getValue();

					System.out.println("\nProcessing category: " + categoryName + " at " + categoryUrl);

					if (!visitedUrls.contains(categoryUrl)) {
						visitedUrls.add(categoryUrl);
						processCategoryPage(categoryUrl, categoryName, allMovies);

						// Be polite to the server
						Thread.sleep(DELAY_BETWEEN_REQUESTS);
					}
				}

				// Write data to Excel file
				if (!allMovies.isEmpty()) {
					writeToExcel(allMovies, "moviesda_movies_list.xlsx");
					System.out
							.println("\nSuccessfully scraped " + allMovies.size() + " movies and saved to Excel file.");
				} else {
					System.out.println("No movies found. Please check website structure or connectivity.");
				}
			} else {
				System.err.println(
						"Failed to connect to the main page. Please check the URL or your internet connection.");
			}

		} catch (Exception e) {
			System.err.println("Error in main process: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static Map<String, String> extractCategoryLinks(Document doc, String baseUrl) {
		Map<String, String> categories = new LinkedHashMap<>(); // Maintain order

		// Based on the HTML structure provided, links are in divs with class "f"
		Elements categoryDivs = doc.select("div.f");

		for (Element div : categoryDivs) {
			Element link = div.selectFirst("a");
			if (link != null) {
				String href = link.attr("href");
				String title = link.text().trim();

				// Make sure we have absolute URLs
				String absoluteUrl;
				if (href.startsWith("http")) {
					absoluteUrl = href;
				} else {
					absoluteUrl = baseUrl + (href.startsWith("/") ? "" : "/") + href;
				}

				categories.put(title, absoluteUrl);
				System.out.println("Found category: " + title + " -> " + absoluteUrl);
			}
		}

		return categories;
	}

	private static void processCategoryPage(String categoryUrl, String categoryName, List<MovieData> allMovies) {
		try {
			Document categoryPage = connectToUrl(categoryUrl);
			if (categoryPage == null) {
				System.err.println("Failed to load category page: " + categoryUrl);
				return;
			}

			System.out.println("Processing movies in category: " + categoryName);

			// Extract movie links from this category page
			extractMoviesFromPage(categoryPage, categoryName, allMovies);

			// Look for sub-pages in this category (pagination)
			extractAndProcessSubpages(categoryPage, categoryUrl, categoryName, allMovies);

		} catch (Exception e) {
			System.err.println("Error processing category page " + categoryUrl + ": " + e.getMessage());
		}
	}

	private static void extractAndProcessSubpages(Document doc, String baseUrl, String categoryName,
			List<MovieData> allMovies) {
		try {
			// Find all links that might be movie list pages
			Elements pageLinks = doc.select("div.f a, div.page a, .pgn a, a.page-link, .pagination a");

			for (Element link : pageLinks) {
				String href = link.attr("href");
				String absoluteUrl;

				// Ensure absolute URL
				if (href.startsWith("http")) {
					absoluteUrl = href;
				} else {
					// Extract base path from the current URL
					String basePath = baseUrl.substring(0, baseUrl.lastIndexOf('/') + 1);
					absoluteUrl = basePath + href;
				}

				// Skip if already visited
				if (visitedUrls.contains(absoluteUrl)) {
					continue;
				}

				// Add to visited set
				visitedUrls.add(absoluteUrl);
				System.out.println("Found sub-page: " + absoluteUrl);

				// Process this sub-page
				Document subPage = connectToUrl(absoluteUrl);
				if (subPage != null) {
					extractMoviesFromPage(subPage, categoryName, allMovies);

					// Be polite to the server
					Thread.sleep(DELAY_BETWEEN_REQUESTS);
				}
			}
		} catch (Exception e) {
			System.err.println("Error processing sub-pages: " + e.getMessage());
		}
	}

	private static void extractMoviesFromPage(Document doc, String categoryName, List<MovieData> allMovies) {
		// Based on the HTML structure, we'll look for movie links in multiple places

		// Try multiple patterns to find movie links
		Elements movieLinks = doc
				.select("div.b a, div.m a, .movie-item a, .movies-item a, .list a, .MovieList a, .entry a");

		if (movieLinks.isEmpty()) {
			// If no specific structure found, try general links that might be movies
			movieLinks = doc.select("a[href*=movie], a[href*=download], a[href$=.html]");
		}

		int newMovies = 0;
		for (Element link : movieLinks) {
			String title = link.text().trim();
			String url = link.attr("abs:href");

			// Skip empty or very short titles and non-movie links
			if (title.length() <= 3 || url.isEmpty() || url.contains("#") || url.equals(doc.location())
					|| title.equalsIgnoreCase("home") || title.equalsIgnoreCase("contact us")
					|| title.equalsIgnoreCase("dmca")) {
				continue;
			}

			// Create a movie entry
			MovieData movie = new MovieData(title, url, categoryName);

			// Add if not a duplicate
			if (!allMovies.contains(movie)) {
				allMovies.add(movie);
				newMovies++;
				System.out.println("Added movie: " + title);
			}
		}

		System.out.println("Added " + newMovies + " new movies from this page");
	}

	private static Document connectToUrl(String url) {
		for (int attempt = 1; attempt <= MAX_RETRY; attempt++) {
			try {
				// Add delay between retries
				if (attempt > 1) {
					Thread.sleep(2000 * attempt); // Increase delay with each retry
				}

				return Jsoup.connect(url).userAgent(
						"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36")
						.timeout(30000).header("Accept-Language", "en-US,en;q=0.9")
						.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
						.header("Cache-Control", "max-age=0").header("Connection", "keep-alive").followRedirects(true)
						.get();

			} catch (IOException e) {
				System.err.println("Attempt " + attempt + " failed for URL " + url + ": " + e.getMessage());
				if (attempt == MAX_RETRY) {
					System.err.println("Max retries reached for URL: " + url);
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				System.err.println("Thread interrupted: " + e.getMessage());
			}
		}
		return null;
	}

	private static void writeToExcel(List<MovieData> movies, String fileName) {
		try {
			System.out.println("Creating Excel file with " + movies.size() + " movies...");

			// Create a new workbook
			Workbook workbook = new XSSFWorkbook();

			// Create a sheet
			Sheet sheet = workbook.createSheet("Movies List");

			// Create header row
			Row headerRow = sheet.createRow(0);
			headerRow.createCell(0).setCellValue("No.");
			headerRow.createCell(1).setCellValue("Category");
			headerRow.createCell(2).setCellValue("Movie Title");
			headerRow.createCell(3).setCellValue("URL");

			// Create cell style for headers
			CellStyle headerStyle = workbook.createCellStyle();
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerStyle.setFont(headerFont);

			for (int i = 0; i < 4; i++) {
				headerRow.getCell(i).setCellStyle(headerStyle);
			}

			// Add data rows
			for (int i = 0; i < movies.size(); i++) {
				MovieData movie = movies.get(i);
				Row row = sheet.createRow(i + 1);

				row.createCell(0).setCellValue(i + 1);
				row.createCell(1).setCellValue(movie.getCategory());
				row.createCell(2).setCellValue(movie.getTitle());
				row.createCell(3).setCellValue(movie.getUrl());
			}

			// Auto-size columns
			for (int i = 0; i < 4; i++) {
				sheet.autoSizeColumn(i);
			}

			// Write the output to a file
			try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
				workbook.write(fileOut);
				System.out.println("Excel file created successfully at: " + fileName);
			}

			// Close the workbook
			workbook.close();

		} catch (IOException e) {
			System.err.println("Error creating Excel file: " + e.getMessage());
			e.printStackTrace();
		}
	}
}