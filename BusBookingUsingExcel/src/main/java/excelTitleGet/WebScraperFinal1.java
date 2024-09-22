package excelTitleGet;


import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebScraperFinal1 {
	static int rowNum = 0;
	static Workbook workbook = new XSSFWorkbook();
	static Sheet sheet = workbook.createSheet("Zoho Articles");
	static Set<String> uniqueEntries = new HashSet<>();

	public static void main(String[] args) throws IOException {
		String surl = "";
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
			row.createCell(3).setCellValue("Page Link");
		} while (false);

		surl = "https://www.geeksforgeeks.org/tag/zoho/?type=popular";

		for (int i = 1; i < 14; i++) {
			if (i != 1)
				surl = "https://www.geeksforgeeks.org/tag/zoho/page/" + i + "/?type=popular";
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
					if (articleDate != null) {
						lastUpdated = articleDate.text().replace("Last Updated: ", "");
					}
					String entry = title + href1 + lastUpdated + url;
					if (!uniqueEntries.contains(entry)) {
						createExcel(title, href1, lastUpdated, url);
						uniqueEntries.add(entry);
					}
				}

			} catch (IOException e) {
				System.out.println("Not found : " + url);
			}
		}
		try (FileOutputStream outputStream = new FileOutputStream("Zoho_Articles.xlsx")) {
			workbook.write(outputStream);
			workbook.close();
			System.out.println("Data written to Excel file successfully.");
			System.out.println("Total Rows : " + rowNum);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void createExcel(String title, String href, String lastUpdated, String url) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH);
		LocalDate lastUpdatedDate = LocalDate.parse(lastUpdated, formatter);
		CreationHelper createHelper = workbook.getCreationHelper();
		CellStyle dateCellStyle = workbook.createCellStyle();
		dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));

		Row row = sheet.createRow(rowNum++);
		row.createCell(0).setCellValue(title);
		row.createCell(1).setCellValue(href);
		Cell cell = row.createCell(2);
		cell.setCellValue(java.sql.Date.valueOf(lastUpdatedDate));
		cell.setCellStyle(dateCellStyle);
		row.createCell(3).setCellValue(url);
	}
}

