package com.movies.db;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MovieScraperAPI {

	private static List<Movie> movieList = new ArrayList<>();
	private static String BASE_URL = null; // "https://moviesda16.com/";

	/**
	 * Category -> "Y" / "N". Only categories marked "Y" are re-scraped. Their old
	 * rows are removed from the downloaded Excel first, then the fresh data is
	 * added. Categories marked "N" are left untouched in the Excel.
	 */
	private static final Map<String, String> CATEGORY_FLAGS = new LinkedHashMap<>();

	private static void add(String flag, String subcategory) {
		CATEGORY_FLAGS.put(subcategory, flag);
	}

	static {
		// ---- Year wise ----
		add("Y", "/tamil-2026-movies/");
		add("Y", "/moviesda-tamil-movies-2026/");
		add("N", "/tamil-2025-movies/");
		add("N", "/tamil-2024-movies/");
		add("N", "/tamil-2023-movies/");
		add("N", "/tamil-2022-movies/");
		add("N", "/tamil-2021-movies/");
		add("N", "/tamil-2020-movies/");
		add("N", "/tamil-2019-movies/");
		add("N", "/tamil-2018-movies/");
		add("N", "/tamil-2017-movies/");
		add("N", "/tamil-2016-movies/");
		add("N", "/tamil-2015-movies/");
		add("N", "/tamil-2012-movies/");
		add("N", "/tamil-hd-movies-download/");

		// ---- Collections ----
		add("N", "/thala-ajith-movies-collection-download/");
		add("N", "/mgr-movies-collection-download/");
		add("N", "/madhavan-movies-collection-download/");
		add("N", "/arjun-movies-collection-download/");
		add("N", "/jiiva-movies-collection-download/");
		add("N", "/jayam-ravi-movies-collection-download/");
		add("N", "/vishal-movies-collection-download/");
		add("N", "/silambarasan-movies-collection-download/");
		add("N", "/vijay-sethupathi-movies-collection-download/");
		add("N", "/dhanush-movies-collection-download/");
		add("N", "/suriya-movies-collections-download/");
		add("N", "/vijayakanth-movie-collections-download/");
		add("N", "/rajinikanth-movie-collections-download/");
		add("N", "/chiyaan-vikram-movie-collections-download/");
		add("N", "/kamal-haasan-movie-collections-download/");
		add("N", "/bhagyaraj-movie-collections-download/");

		// ---- Actor wise ----
		add("N", "/actor-sasikumar-movies-collections/");
		add("N", "/actor-nakul-movies-collections/");
		add("N", "/actor-siddharth-movies-collection/");
		add("N", "/actor-cheran-movies-collection/");
		add("N", "/actor-vimal-movies-collection/");
		add("N", "/actor-vijay-movies-collection/");
		add("N", "/actor-ramarajan-movies-collection/");
		add("N", "/actor-simbu-movies-collection/");
		add("N", "/actor-sathiyaraj-movies-collection/");
		add("N", "/actor-appukutty-movies-collection/");
		add("N", "/actor-surya-movies-collection/");
		add("N", "/actor-murali-movies-collection/");
		add("N", "/actor-mohan-movies-collection/");
		add("N", "/actor-sarathkumar-movies-collection/");
		add("N", "/actor-bhagyaraj-movies-collection/");
		add("N", "/actor-mgr-movies-collection/");
		add("N", "/actor-vishal-movies-collection/");
		add("N", "/actor-vijayakanth-movies-collection/");
		add("N", "/actor-sivakarthikeyan-movies-collection/");
		add("N", "/actor-prashanth-movies-collection/");
		add("N", "/actor-prabhu-movies-collection/");
		add("N", "/actor-prabhu-deva-movies-collection/");
		add("N", "/actor-parthiepan-movies-collection/");
		add("N", "/actor-kamal-hassan-movies-collection/");
		add("N", "/actor-arjun-movies-collection/");
		add("N", "/actor-rajinikanth-movies-collection/");
		add("N", "/actor-madhavan-movies-collection/");
		add("N", "/actor-vikram-movie-collections/");
		add("N", "/actor-jeeva-movies-collection/");
		add("N", "/actor-dhaunsh-movies-collection/");
		add("N", "/actor-dinesh-movies-collection/");
		add("N", "/actor-vijay-sethupathi-movies-collection/");
		add("N", "/actor-arya-movies-collection/");
		add("N", "/actor-jayam-ravi-movies-collection/");
		add("N", "/actor-ajith-movies-collection/");
		add("N", "/actor-karthik-movies-collection/");
		add("N", "/actor-rajkiran-movies-collection/");
		add("N", "/actor-karthi-movies-collection/");
		add("N", "/actor-sivaji-ganesan-movies-collection/");
		add("N", "/actor-kunal-movies-collection/");

		// ---- Alphabet wise ----
		add("N", "/tamil-movies/a/");
		add("N", "/tamil-movies/b/");
		add("N", "/tamil-movies/c/");
		add("N", "/tamil-movies/d/");
		add("N", "/tamil-movies/e/");
		add("N", "/tamil-movies/f/");
		add("N", "/tamil-movies/g/");
		add("N", "/tamil-movies/h/");
		add("N", "/tamil-movies/i/");
		add("N", "/tamil-movies/j/");
		add("N", "/tamil-movies/k/");
		add("N", "/tamil-movies/l/");
		add("N", "/tamil-movies/m/");
		add("N", "/tamil-movies/n/");
		add("N", "/tamil-movies/o/");
		add("N", "/tamil-movies/p/");
		add("N", "/tamil-movies/q/");
		add("N", "/tamil-movies/r/");
		add("N", "/tamil-movies/s/");
		add("N", "/tamil-movies/t/");
		add("N", "/tamil-movies/u/");
		add("N", "/tamil-movies/v/");
		add("N", "/tamil-movies/w/");
		add("N", "/tamil-movies/x/");
		add("N", "/tamil-movies/y/");
		add("N", "/tamil-movies/z/");
	}

	private static final int TIMEOUT = 20000; // Increased to 20 seconds timeout
	private static final int MAX_RETRIES = 3; // Number of retries for failed requests
	private static final int DELAY_MS = 2000; // 2 seconds delay between requests
	private static final int MAX_PAGES_WITHOUT_PAGINATION = 20; // Max pages to try if no pagination found
	private static final String filePath = "Movies.xlsx";
	private static final File file = new File(filePath);
	public static Scanner sc = new Scanner(System.in);

	/** Old rows removed from Excel, kept in memory so we can restore them if scraping brings nothing. */
	private static final Map<String, List<String[]>> removedRows = new LinkedHashMap<>();

	public static void main(String[] args) throws Exception {
		List<String> selected = getSelectedSubcategories();
		if (selected.isEmpty()) {
			System.out.println("No category is marked 'Y'. Nothing to process.");
			return;
		}
		System.out.println("Categories marked 'Y' (" + selected.size() + "): " + selected);

		System.out.println("Enter the Base URL: Example: https://moviesda33.com");
		BASE_URL = sc.next();

		UploadFileAPI uploadFile = new UploadFileAPI();

		// STEP 1 & 2: download existing Excel, read all rows, drop 'Y' category rows.
		// If this fails, we abort here so the old file on WorkDrive is never overwritten.
		loadExistingData(uploadFile, selected);

		// STEP 3: scrape only the 'Y' categories
		Set<String> scrapedCategories = new HashSet<>();
		try {
			for (String subcategory : selected) {
				int before = movieList.size();
				try {
					processSubcategory(subcategory);
				} catch (Exception e) {
					System.err.println("Failed to process subcategory: " + subcategory + ", Error: " + e.getMessage());
				}
				if (movieList.size() > before) {
					scrapedCategories.add(toCategoryKey(subcategory));
				}
			}
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			// Safety: if a 'Y' category returned nothing (site down etc.), put its old rows back
			restoreOldRows(scrapedCategories);

			// STEP 4: write Excel and upload
			moviesExtract();
			uploadFile.uploadToWorkDrive(file);
		}
	}

	private static List<String> getSelectedSubcategories() {
		List<String> selected = new ArrayList<>();
		for (Map.Entry<String, String> entry : CATEGORY_FLAGS.entrySet()) {
			if ("Y".equalsIgnoreCase(entry.getValue())) {
				selected.add(entry.getKey());
			}
		}
		return selected;
	}

	/** Same format that was already being stored in the Category column: "/tamil-2026-movies/" -> "tamil-2026-movies" */
	private static String toCategoryKey(String subcategory) {
		return subcategory.replace("/", "");
	}

	private static void loadExistingData(UploadFileAPI api, List<String> selected) throws Exception {
		Set<String> removeKeys = new HashSet<>();
		for (String s : selected) {
			removeKeys.add(toCategoryKey(s));
		}

		System.out.println("Downloading existing " + filePath + " from WorkDrive...");
		byte[] excelBytes = api.downloadExcelFromWorkDrive(filePath);
		if (excelBytes == null) {
			System.out.println(filePath + " not found in WorkDrive folder. Starting with empty data.");
			return;
		}

		Movie.setIdGenerater(1); // ids are re-numbered sequentially
		int kept = 0;
		int removed = 0;

		try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(excelBytes))) {
			Sheet sheet = workbook.getSheet("Movies DB");
			if (sheet == null) {
				sheet = workbook.getSheetAt(0);
			}
			DataFormatter fmt = new DataFormatter();

			// Row 0 is header. Columns: 0 ID, 1 Name, 2 Sublink, 3 Category, 4 Link, 5 PageURL
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				String name = fmt.formatCellValue(row.getCell(1));
				String subLink = fmt.formatCellValue(row.getCell(2));
				String category = fmt.formatCellValue(row.getCell(3)).trim();
				String link = fmt.formatCellValue(row.getCell(4));
				String pageUrl = fmt.formatCellValue(row.getCell(5));

				if (name.isEmpty() && category.isEmpty()) {
					continue; // blank row
				}

				if (removeKeys.contains(category)) {
					removedRows.computeIfAbsent(category, k -> new ArrayList<>())
							.add(new String[] { name, subLink, category, link, pageUrl });
					removed++;
				} else {
					movieList.add(new Movie(name, subLink, category, link, pageUrl));
					kept++;
				}
			}
		}
		System.out.println("Existing data read. Kept rows: " + kept + ", Removed rows (Y categories): " + removed);
	}

	private static void restoreOldRows(Set<String> scrapedCategories) {
		for (Map.Entry<String, List<String[]>> entry : removedRows.entrySet()) {
			if (scrapedCategories.contains(entry.getKey())) {
				continue;
			}
			System.err.println("No new data scraped for '" + entry.getKey() + "'. Restoring "
					+ entry.getValue().size() + " old rows.");
			for (String[] r : entry.getValue()) {
				movieList.add(new Movie(r[0], r[1], r[2], r[3], r[4]));
			}
		}
	}

	private static void processSubcategory(String subcategory) throws Exception {
		String baseSubcategoryUrl = BASE_URL + subcategory;
		int lastPage = getLastPageNumber(baseSubcategoryUrl);
		System.out.println("Processing subcategory: " + subcategory + ", Total pages: " + lastPage);

		for (int page = 1; page <= lastPage; page++) {
			String pageUrl = baseSubcategoryUrl + (page == 1 ? "" : "?page=" + page);
			System.out.println("Scraping page: " + pageUrl);
			try {
				if (!scrapePage(pageUrl, subcategory)) {
					System.err.println(
							"Page not found or empty: " + pageUrl + ". Stopping pagination for this subcategory.");
					break; // Stop if page is not found or empty
				}
				Thread.sleep(DELAY_MS); // Delay to avoid overwhelming the server
			} catch (Exception e) {
				System.err.println("Error scraping page " + pageUrl + ": " + e.getMessage());
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
				return MAX_PAGES_WITHOUT_PAGINATION; // Default if no pagination link found
			} catch (HttpStatusException e) {
				System.out.println("HTTP error fetching " + e.getStatusCode() + " - " + e.getMessage());
				return MAX_PAGES_WITHOUT_PAGINATION; // Default on HTTP error
			} catch (Exception e) {
				retries++;
				System.err.println(
						"Retry " + retries + "/" + MAX_RETRIES + " for " + subcategoryUrl + ": " + e.getMessage());
				if (retries == MAX_RETRIES) {
					System.err.println("Failed to get last page number for " + subcategoryUrl + " after " + MAX_RETRIES
							+ " retries. Defaulting to max " + MAX_PAGES_WITHOUT_PAGINATION + " pages.");
					return MAX_PAGES_WITHOUT_PAGINATION; // Default after retries
				}
				Thread.sleep(DELAY_MS * retries); // Incremental delay for retries
			}
		}
		return MAX_PAGES_WITHOUT_PAGINATION; // Fallback
	}

	private static boolean scrapePage(String pageUrl, String subcategory) throws Exception {
		String category = toCategoryKey(subcategory);
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

				for (Element movieLink : movieDivs) {
					String name = movieLink.text().trim();
					String sublink = movieLink.attr("href");
					String fullLink = BASE_URL + sublink;

					Movie movie = new Movie(name, sublink, category, fullLink, pageUrl);
					movieList.add(movie);
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

	private static void moviesExtract() {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Movies DB");
		CreationHelper createHelper = workbook.getCreationHelper();
		CellStyle hyperlinkStyle = workbook.createCellStyle();
		Font hyperlinkFont = workbook.createFont();
		hyperlinkFont.setUnderline(Font.U_SINGLE);
		hyperlinkFont.setColor(IndexedColors.BLUE.getIndex());
		hyperlinkStyle.setFont(hyperlinkFont);
		int rowNum = 1;

		// Create header row
		Row headerRow = sheet.createRow(0);
		sheet.createFreezePane(0, 1); // Freeze header row
		// Create bold font
		CellStyle headerStyle = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBold(true);
		headerStyle.setFont(font);

		String[] headers = { "ID", "Name", "Sublink", "Category", "Link", "PageURL" };

		for (int i = 0; i < headers.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
			cell.setCellStyle(headerStyle);
		}

		for (Movie movierow : movieList) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(movierow.getId());
			row.createCell(1).setCellValue(movierow.getName());
			row.createCell(2).setCellValue(movierow.getSubLink());
			row.createCell(3).setCellValue(movierow.getCategory());
			// Column 5 - URL (index 4)
			String url1 = movierow.getLink();
			Cell cell4 = row.createCell(4);
			if (url1 != null && !url1.isEmpty()) {
				org.apache.poi.ss.usermodel.Hyperlink link1 = createHelper.createHyperlink(HyperlinkType.URL);
				link1.setAddress(url1);
				cell4.setCellValue(url1);
				cell4.setHyperlink(link1);
				cell4.setCellStyle(hyperlinkStyle);
			} else {
				cell4.setCellValue("");
			}

			// Column 6 - URL (index 5)
			String url2 = movierow.getPageurl();
			Cell cell5 = row.createCell(5);
			if (url2 != null && !url2.isEmpty()) {
				org.apache.poi.ss.usermodel.Hyperlink link2 = createHelper.createHyperlink(HyperlinkType.URL);
				link2.setAddress(url2);
				cell5.setCellValue(url2);
				cell5.setHyperlink(link2);
				cell5.setCellStyle(hyperlinkStyle);
			} else {
				cell5.setCellValue("");
			}
		}

		// Auto-size all columns based on content
		for (int i = 0; i < headers.length; i++) {
			sheet.autoSizeColumn(i);
			sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 512); // add small padding
		}

		try (FileOutputStream fos1 = new FileOutputStream(filePath)) {
			workbook.write(fos1);
			workbook.close();
			System.out.println("Data written to Excel file successfully.");
			System.out.println("Total Rows : " + rowNum);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}