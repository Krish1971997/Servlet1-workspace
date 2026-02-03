package com.movies.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MovieScraper {
	private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=Krishna_Testing;trustServerCertificate=true";
	private static final String DB_USER = "sa"; // Replace with your DB username
	private static final String DB_PASSWORD = "15848"; // Replace with your DB password
	private static final String BASE_URL = "https://moviesda16.com/";
	private static final String[] SUBCATEGORIES = { "/tamil-2026-movies/","/tamil-2025-movies/", "/tamil-2024-movies/", "/tamil-2023-movies/",
			"/tamil-2022-movies/", "/tamil-2021-movies/", "/tamil-2020-movies/", "/tamil-2019-movies/",
			"/tamil-2018-movies/", "/tamil-2017-movies/", "/tamil-2016-movies/", "/tamil-2015-movies/",
			"/tamil-2012-movies/", "/tamil-hd-movies-download/", "/thala-ajith-movies-collection-download/",
			"/mgr-movies-collection-download/", "/madhavan-movies-collection-download/",
			"/arjun-movies-collection-download/", "/jiiva-movies-collection-download/",
			"/jayam-ravi-movies-collection-download/", "/vishal-movies-collection-download/",
			"/silambarasan-movies-collection-download/", "/vijay-sethupathi-movies-collection-download/",
			"/dhanush-movies-collection-download/", "/suriya-movies-collections-download/",
			"/vijayakanth-movie-collections-download/", "/rajinikanth-movie-collections-download/",
			"/chiyaan-vikram-movie-collections-download/", "/kamal-haasan-movie-collections-download/",
			"/bhagyaraj-movie-collections-download/", "/actor-sasikumar-movies-collections/",
			"/actor-nakul-movies-collections/", "/actor-siddharth-movies-collection/",
			"/actor-cheran-movies-collection/", "/actor-vimal-movies-collection/", "/actor-vijay-movies-collection/",
			"/actor-ramarajan-movies-collection/", "/actor-simbu-movies-collection/",
			"/actor-sathiyaraj-movies-collection/", "/actor-appukutty-movies-collection/",
			"/actor-surya-movies-collection/", "/actor-murali-movies-collection/", "/actor-mohan-movies-collection/",
			"/actor-sarathkumar-movies-collection/", "/actor-bhagyaraj-movies-collection/",
			"/actor-mgr-movies-collection/", "/actor-vishal-movies-collection/",
			"/actor-vijayakanth-movies-collection/", "/actor-sivakarthikeyan-movies-collection/",
			"/actor-prashanth-movies-collection/", "/actor-prabhu-movies-collection/",
			"/actor-prabhu-deva-movies-collection/", "/actor-parthiepan-movies-collection/",
			"/actor-kamal-hassan-movies-collection/", "/actor-arjun-movies-collection/",
			"/actor-rajinikanth-movies-collection/", "/actor-madhavan-movies-collection/",
			"/actor-vikram-movie-collections/", "/actor-jeeva-movies-collection/", "/actor-dhaunsh-movies-collection/",
			"/actor-dinesh-movies-collection/", "/actor-vijay-sethupathi-movies-collection/",
			"/actor-arya-movies-collection/", "/actor-jayam-ravi-movies-collection/", "/actor-ajith-movies-collection/",
			"/actor-karthik-movies-collection/", "/actor-rajkiran-movies-collection/",
			"/actor-karthi-movies-collection/", "/actor-sivaji-ganesan-movies-collection/",
			"/actor-kunal-movies-collection/" };

	private static final int TIMEOUT = 20000; // Increased to 20 seconds timeout
	private static final int MAX_RETRIES = 3; // Number of retries for failed requests
	private static final int DELAY_MS = 2000; // 2 seconds delay between requests
	private static final int MAX_PAGES_WITHOUT_PAGINATION = 10; // Max pages to try if no pagination found

	public static void main(String[] args) {
		try {
			// Establish database connection
			Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			System.out.println("Connected to database successfully.");

			// Create table if it doesn't exist
			createTable(conn);

			// Process each subcategory
			for (String subcategory : SUBCATEGORIES) {
				try {
					processSubcategory(conn, subcategory);
				} catch (Exception e) {
					System.err.println("Failed to process subcategory: " + subcategory + ", Error: " + e.getMessage());
					e.printStackTrace();
				}
			}

			// Close connection
			conn.close();
			System.out.println("Database connection closed.");
		} catch (SQLException e) {
			System.err.println("Database error: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void createTable(Connection conn) throws SQLException {
		String createTableSQL = "IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Movies') "
				+ "CREATE TABLE Movies (" + "id INT IDENTITY(1,1) PRIMARY KEY, " + "name NVARCHAR(255), "
				+ "sublink NVARCHAR(255), " + "category NVARCHAR(100), " + "link NVARCHAR(255), "  
				+ "pageurl varchar(1000) )";
		try (PreparedStatement stmt = conn.prepareStatement(createTableSQL)) {
			stmt.execute();
			System.out.println("Table 'Movies' created or already exists.");
		}

		String query = "Truncate table Movies";
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.execute();
			System.out.println("Truncated Table 'Movies'");
		}

	}

	private static void processSubcategory(Connection conn, String subcategory) throws Exception {
		String baseSubcategoryUrl = BASE_URL + subcategory;
		int lastPage = getLastPageNumber(baseSubcategoryUrl);
		System.out.println("Processing subcategory: " + subcategory + ", Total pages: " + lastPage);

		for (int page = 1; page <= lastPage; page++) {
			String pageUrl = baseSubcategoryUrl + (page == 1 ? "" : "?page=" + page);
			System.out.println("Scraping page: " + pageUrl);
			try {
				if (!scrapePage(conn, pageUrl, subcategory)) {
					System.err.println(
							"Page not found or empty: " + pageUrl + ". Stopping pagination for this subcategory.");
					break; // Stop if page is not found or empty
				}
				Thread.sleep(DELAY_MS); // Delay to avoid overwhelming the server
			} catch (Exception e) {
				System.err.println("Error scraping page " + pageUrl + ": " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private static int getLastPageNumber(String subcategoryUrl) throws Exception {
		int retries = 0;
		while (retries < MAX_RETRIES) {
			try {
				Document doc = Jsoup.connect(subcategoryUrl).timeout(TIMEOUT).userAgent(
						"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
						.get();
				Elements paginationLinks = doc.select("a.pagination_last");
				if (!paginationLinks.isEmpty()) {
					String lastPageUrl = paginationLinks.first().attr("href");
					String pageParam = lastPageUrl.split("page=")[1];
					try {
						return Integer.parseInt(pageParam);
					} catch (NumberFormatException e) {
						System.err.println("Could not parse last page number from: " + lastPageUrl);
					}
				}
				System.out.println("No pagination link found for " + subcategoryUrl + ". Defaulting to max "
						+ MAX_PAGES_WITHOUT_PAGINATION + " pages.");
				return MAX_PAGES_WITHOUT_PAGINATION; // Default to 10 pages if no pagination link found
			} catch (HttpStatusException e) {
				System.out.println("HTTP error fetching " + e.getStatusCode() + " - " + e.getMessage());
				return MAX_PAGES_WITHOUT_PAGINATION; // Default to 10 pages on HTTP error
			} catch (Exception e) {
				retries++;
				System.err.println(
						"Retry " + retries + "/" + MAX_RETRIES + " for " + subcategoryUrl + ": " + e.getMessage());
				if (retries == MAX_RETRIES) {
					System.err.println("Failed to get last page number for " + subcategoryUrl + " after " + MAX_RETRIES
							+ " retries. Defaulting to max " + MAX_PAGES_WITHOUT_PAGINATION + " pages.");
					return MAX_PAGES_WITHOUT_PAGINATION; // Default to 10 pages after retries
				}
				Thread.sleep(DELAY_MS * retries); // Incremental delay for retries
			}
		}
		return MAX_PAGES_WITHOUT_PAGINATION; // Fallback
	}

	private static boolean scrapePage(Connection conn, String pageUrl, String subcategory) throws Exception {
		int retries = 0;
		while (retries < MAX_RETRIES) {
			try {
				Document doc = Jsoup.connect(pageUrl).timeout(TIMEOUT).userAgent(
						"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
						.get();
				Elements movieDivs = doc.select("div.f a");

				if (movieDivs.isEmpty()) {
					System.err.println("No movie data found on page: " + pageUrl);
					return false; // Indicate page is empty or not found
				}

				String insertSQL = "INSERT INTO Movies (name, sublink, category, link, pageurl) VALUES (?, ?, ?, ?, ?)";
				try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
					for (Element movieLink : movieDivs) {
						String name = movieLink.text().trim();
						String sublink = movieLink.attr("href");
						String fullLink = BASE_URL + sublink;

						// Set parameters
						pstmt.setString(1, name);
						pstmt.setString(2, sublink);
						pstmt.setString(3, subcategory.replace("/", "")); // Remove slashes for cleaner category name
						pstmt.setString(4, fullLink);
						pstmt.setString(5, pageUrl);

						// Execute insert
						pstmt.executeUpdate();
						System.out.println("Inserted: " + name + ", " + sublink + ", " + subcategory + ", " + fullLink);
					}
				}
				return true; // Success, page processed
			} catch (HttpStatusException e) {
				System.err
						.println("HTTP error fetching " + pageUrl + ": " + e.getStatusCode() + " - " + e.getMessage());
				return false; // Stop pagination if page is not found (e.g., 404)
			} catch (Exception e) {
				retries++;
				System.err.println("Retry " + retries + "/" + MAX_RETRIES + " for " + pageUrl + ": " + e.getMessage());
				if (retries == MAX_RETRIES) {
					throw new Exception("Failed to scrape " + pageUrl + " after " + MAX_RETRIES + " retries", e);
				}
				Thread.sleep(DELAY_MS * retries); // Incremental delay for retries
			}
		}
		return false;
	}
}