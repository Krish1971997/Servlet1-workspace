package com.zoho.excelTitleGet2;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParserExample {

	public static void main(String[] args) {

		Document document = Jsoup.parse(StringGet.htmlContent);
		Elements articleContainers = document.select(".mt-0 mb-0");
		System.out.println("Starting....");

		for (Element container : articleContainers) {

			String interviewDate = container.select("time[dateTime]").text();
			String employerLogoUrl = container.select("div[data-test=Interview83067638EmployerLogo] img").attr("src");
			String interviewTitle = container.select("h2[data-test=Interview83067638Title] a").text();
			String offer = container.select("span[class=mb-xxsm]").text();
			String Application = container.select("p[class=mt-xsm mb-std]").text();
			String interview = container.select("p[data-test=Interview83067638Process]").text();
			String candidateLocation = container.select("p[data-test=Interview83067638CandidateSubtext]").text();

			// Print the extracted information
			System.out.println("Interview Date: " + interviewDate);
			System.out.println("Employer Logo URL: " + employerLogoUrl);
			System.out.println("Interview Title: " + interviewTitle);
			System.out.println("Offer : " + offer);
			System.out.println("Application : " + Application);
			System.out.println("interview : " + interview);
			System.out.println("Candidate Location: " + candidateLocation);

			// Extract and print interview questions
			Elements interviewQuestions = container.select("ul[data-test=Interview83067638Questions] li");
			System.out.println("Interview Questions:");
			System.out.println(interviewQuestions.text());
		}
	}
}
