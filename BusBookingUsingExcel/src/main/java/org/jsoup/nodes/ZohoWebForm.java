package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

public class ZohoWebForm {
	public static void main(String[] args) {
		String url = "https://careers.zohocorp.com/forms/";

		try {
			Document doc = Jsoup.connect(url).get();
			Elements forms = doc.select("form");

			if (forms.size() > 0) {
				System.out.println("Forms found on the website:");

				for (Element form : forms) {
					String formId = form.id();
					System.out.println("Form ID: " + formId);

					// Get information about each form using its ID
					String formDetailsUrl = url + formId;
					Document formDoc = Jsoup.connect(formDetailsUrl).get();
					// Extract and print information about the form, you can customize this part
					System.out.println("Form Title: " + formDoc.title());
					// Add more details as needed
				}
			} else {
				System.out.println("No forms found on the website.");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
