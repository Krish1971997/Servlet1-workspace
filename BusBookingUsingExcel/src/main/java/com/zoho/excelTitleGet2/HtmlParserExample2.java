package com.zoho.excelTitleGet2;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HtmlParserExample2 {

	public static void main(String[] args) {

	/**	String url = "https://www.glassdoor.co.in/Reviews/Zoho-Reviews-E328604.htm";
		try {
			Document document = Jsoup.connect(url).get();
			Thread.sleep(1000); // 1-second delay

			String test = document.select("span[data-test=pros]").text();
			System.out.println("Interview Date: " + test);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		} */
		
		String url = "https://www.geeksforgeeks.org/tag/zoho/";
		try {
		    Document document = Jsoup.connect(url)
		            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
		            .referrer("https://www.google.com/")
		            .get();
		    
		    Thread.sleep(1000); // 1-second delay

		    String test = document.select("a[class=article_heading]").text();
		    System.out.println("Interview Date: " + test);
		} catch (IOException | InterruptedException e) {
		    e.printStackTrace();
		}

	} 
}
