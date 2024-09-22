package excelTitleGet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class uniqueData {
	String title;
	String lastUpdated;
	String href;

	public uniqueData(String title, String lastUpdated, String href) {
		this.title = title;
		this.lastUpdated = lastUpdated;
		this.href = href;
	}

	@Override
	public int hashCode() {
		return Objects.hash(href, lastUpdated, title);
	}
}


class uniqueDataForExcel {
	String title;
	Date lastUpdated;
	String href;

	public uniqueDataForExcel(String title, Date lastUpdated, String href) {
		this.title = title;
		this.lastUpdated = lastUpdated;
		this.href = href;
	}

	@Override
	public int hashCode() {
		return Objects.hash(href, lastUpdated, title);
	}
}

public class WebScraperFinal {
	static int rowNum = 0;
	static Workbook workbook = new XSSFWorkbook();
	static Sheet sheet = workbook.createSheet("Zoho Articles");
	static Map<Integer, uniqueData> uniqueEntries = new HashMap<>();
	static Map<Integer, uniqueDataForExcel> uniqueEntriesForExcel = new HashMap<>();
	static long startTime;

	public static void main(String[] args) throws IOException, SQLException, InterruptedException, ParseException {
		startTime = System.currentTimeMillis();
		System.out.println("Start time : " + startTime + "ms");
		System.out.println("Starting...");
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formattedDateTime = currentDateTime.format(formatter);

		System.out.println("Date and time : " + formattedDateTime);
		do {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue("Title");
			row.createCell(1).setCellValue("Link");
			row.createCell(2).setCellValue("Created date");
		} while (false);

		int lastPageNumber = getLastPageNumber();
		System.out.println("Last page number: " + lastPageNumber);

		if (lastPageNumber > 0) {
			Jdbc_Connection.truncateTable();
			System.out.println("Truncate successfully...");
		}

		fetchResultsFromWebsite(lastPageNumber, "/?type=recent");
		fetchResultsFromWebsite(lastPageNumber, "/?type=popular");
		
		createExcel();

		try (FileOutputStream outputStream = new FileOutputStream("Zoho_Articles.xlsx")) {
			workbook.write(outputStream);
			workbook.close();
			System.out.println("Data written to Excel file successfully.");
			System.out.println("Total Rows : " + uniqueEntries.size());
			long startTime = System.currentTimeMillis();
			System.out.println("Insert DB Start time : " + startTime + "ms");
			insertDB();
			System.out.println("Insert DB End time : " + (System.currentTimeMillis() - startTime) / 1000 + " sec");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Jdbc_Connection.sent_email();
		}
	}

	private static void fetchResultsFromWebsite(int lastPageNumber, String suffix) {
		String surl = "https://www.geeksforgeeks.org/tag/zoho" + suffix;
		if (suffix.contains("popular"))
			surl = "https://www.geeksforgeeks.org/tag/zoho/";
		for (int i = 1; i < lastPageNumber; i++) {
			if (i != 1)
				surl = "https://www.geeksforgeeks.org/tag/zoho/page/" + i + suffix;
			String url = String.valueOf(surl);
			try {
				Document doc1 = Jsoup.connect(url).get();
				Elements articleContainers1 = doc1.select(".article_heading_container");

				for (Element container : articleContainers1) {
					Element articleHeading = container.selectFirst(".article_subheading a");
					Element articleDate = container.selectFirst(".article_date");
					

					String title = "";
					String href1 = "";
					String lastUpdated = "";

					if (articleHeading != null) {
						title = articleHeading.text();
						href1 = articleHeading.attr("href");
					}
					if (articleDate != null)
						lastUpdated = articleDate.text().replace("Last Updated: ", "");

					addUniqueEntries(title, href1, lastUpdated);
					
				}

			} catch (IOException e) {
				System.out.println("Not found : " + url);
			}
		}
	}

	private static void addUniqueEntries(String title, String href, String lastUpdated) {
		uniqueData unique = new uniqueData(title, lastUpdated, href);
		int hashcode = unique.hashCode();
		uniqueEntries.put(hashcode, unique);
	}

	private static void createExcel() {
		for (Map.Entry<Integer, uniqueDataForExcel> unique : uniqueEntriesForExcel.entrySet()) {
		CellStyle dateCellStyle = workbook.createCellStyle();

		Row row = sheet.createRow(rowNum++);
		row.createCell(0).setCellValue(unique.getValue().title);
		row.createCell(1).setCellValue(unique.getValue().href);
		Cell cell = row.createCell(2);
		cell.setCellValue(unique.getValue().lastUpdated);
		cell.setCellStyle(dateCellStyle);
		}
	}

	private static int getLastPageNumber() {
		String url = "https://www.geeksforgeeks.org/tag/zoho/";
		int lastPageNumber = 0;
		try {
			Document doc = Jsoup.connect(url).get();
			Element paginationEnd = doc.selectFirst(".pagination_end");
			String lastPageUrl = paginationEnd.attr("href");
			lastPageNumber = extractPageNumber(lastPageUrl);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return lastPageNumber;
	}

	private static int extractPageNumber(String url) {
		String[] parts = url.split("/");
		String pageNumberString = parts[parts.length - 2];
		return Integer.parseInt(pageNumberString);
	}

	public static void insertDB() throws SQLException, ParseException {
		for (Map.Entry<Integer, uniqueData> unique : uniqueEntries.entrySet()) {
			Jdbc_Connection.insertOperation(unique.getValue().title, unique.getValue().lastUpdated,
					unique.getValue().href);
		}
	}
}
