package excelTitleGet;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class WebScraperFinal {

	private static final Workbook workbook = new XSSFWorkbook();
	private static final Sheet sheet = workbook.createSheet("Zoho Articles");
	private static final Map<Integer, UniqueData> uniqueEntries = new HashMap<>();
	private static int rowNum = 0;
	static long startTime;

	public static void main(String[] args) throws IOException, SQLException, InterruptedException, ParseException {
		startTime = System.currentTimeMillis();
		System.out.println("Start time: " + startTime + "ms");
		System.out.println("Starting...");
		logCurrentDateTime();

		initializeExcelSheet();

		int lastPageNumber = getLastPageNumber();
		System.out.println("Last page number: " + lastPageNumber);

		if (lastPageNumber > 0) {
			Jdbc_Connection.truncateTable();
			System.out.println("Table truncated successfully...");
		}

		fetchResultsFromWebsite(lastPageNumber, "/?type=recent");
		fetchResultsFromWebsite(lastPageNumber, "/?type=popular");

		createExcel();
		writeExcelToFile();
		insertDataToDB();
	}

	private static void logCurrentDateTime() {
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formattedDateTime = currentDateTime.format(formatter);
		System.out.println("Date and time: " + formattedDateTime);
	}

	private static void initializeExcelSheet() {
		Row headerRow = sheet.createRow(rowNum++);
		headerRow.createCell(0).setCellValue("Title");
		headerRow.createCell(1).setCellValue("Link");
		headerRow.createCell(2).setCellValue("Created date");
	}

	private static void fetchResultsFromWebsite(int lastPageNumber, String suffix) {
		String baseUrl = "https://www.geeksforgeeks.org/tag/zoho" + suffix;
		if (suffix.contains("popular")) {
			baseUrl = "https://www.geeksforgeeks.org/tag/zoho/";
		}

		for (int i = 1; i <= lastPageNumber; i++) {
			String url = (i == 1) ? baseUrl : baseUrl + "/page/" + i;
			try {
				Document doc = Jsoup.connect(url).get();
				Elements articleContainers = doc.select(".article_heading_container");

				for (Element container : articleContainers) {
					Element articleHeading = container.selectFirst(".article_subheading a");
					Element articleDate = container.selectFirst(".article_date");

					String title = articleHeading != null ? articleHeading.text() : "";
					String href = articleHeading != null ? articleHeading.attr("href") : "";
					String lastUpdated = articleDate != null ? articleDate.text().replace("Last Updated: ", "") : "";

					addUniqueEntries(title, href, lastUpdated);
				}
			} catch (IOException e) {
				System.out.println("Error fetching data from URL: " + url);
				e.printStackTrace();
			}
		}
	}

	private static void addUniqueEntries(String title, String href, String lastUpdated) {
		UniqueData unique = new UniqueData(title, lastUpdated, href);
		uniqueEntries.put(unique.hashCode(), unique); // Uses hashcode as key for uniqueness
	}

	private static void createExcel() {
		for (Map.Entry<Integer, UniqueData> entry : uniqueEntries.entrySet()) {
			UniqueData uniqueData = entry.getValue();
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(uniqueData.title);
			row.createCell(1).setCellValue(uniqueData.href);

			Cell dateCell = row.createCell(2);
			dateCell.setCellValue(uniqueData.lastUpdated);
		}
	}

	private static void writeExcelToFile() throws IOException {
		try (FileOutputStream outputStream = new FileOutputStream("Zoho_Articles.xlsx")) {
			workbook.write(outputStream);
			workbook.close();
			System.out.println("Data written to Excel file successfully.");
		}
	}

	private static int getLastPageNumber() {
		String url = "https://www.geeksforgeeks.org/tag/zoho/";
		try {
			Document doc = Jsoup.connect(url).get();
			Element paginationEnd = doc.selectFirst(".pagination_end");
			String lastPageUrl = paginationEnd.attr("href");
			return extractPageNumber(lastPageUrl);
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}

	private static int extractPageNumber(String url) {
		String[] parts = url.split("/");
		String pageNumberString = parts[parts.length - 2];
		return Integer.parseInt(pageNumberString);
	}

	private static void insertDataToDB() throws SQLException, ParseException {
		long dbStartTime = System.currentTimeMillis();
		System.out.println("Inserting data into DB, Start time: " + dbStartTime + "ms");

		for (UniqueData unique : uniqueEntries.values()) {
			Jdbc_Connection.insertOperation(unique.title, unique.lastUpdated, unique.href);
		}
		
		Jdbc_Connection.sent_email();

		System.out.println("Insert DB End time: " + (System.currentTimeMillis() - dbStartTime) / 1000 + " sec");
	}
}

class UniqueData {
	String title;
	String lastUpdated;
	String href;

	public UniqueData(String title, String lastUpdated, String href) {
		this.title = title;
		this.lastUpdated = lastUpdated;
		this.href = href;
	}

	@Override
	public int hashCode() {
		return Objects.hash(href, lastUpdated, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		UniqueData that = (UniqueData) obj;
		return Objects.equals(href, that.href) && Objects.equals(lastUpdated, that.lastUpdated)
				&& Objects.equals(title, that.title);
	}
}
