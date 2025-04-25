package excelTitleGet;

	import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

	public class WebScraperTest {
		    public static void main(String[] args) {
		        String url = "https://www.geeksforgeeks.org/tag/zoho/?type=popular";
		        
		        try {
		            Document doc = Jsoup.connect(url).get();
		            Element paginationEnd = doc.selectFirst(".pagination_end");
		            String lastPageUrl = paginationEnd.attr("href");
		            int lastPageNumber = extractPageNumber(lastPageUrl);
		            System.out.println("Last page number: " + lastPageNumber);
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		    
		    private static int extractPageNumber(String url) {
		        // Extract the page number from the URL
		        String[] parts = url.split("/");
		        String pageNumberString = parts[parts.length - 2];
		        return Integer.parseInt(pageNumberString);
		    }
		}
