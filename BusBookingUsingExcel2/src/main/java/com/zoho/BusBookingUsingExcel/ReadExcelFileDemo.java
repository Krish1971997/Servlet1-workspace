package com.zoho.BusBookingUsingExcel;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

import busBookingSystem.BookingTicket;

public class ReadExcelFileDemo {
	public static void main(String[] args) {
		String server = "localhost";
		int port = 21;
		String username = "testuser1";
		String password = "1234";
		String remoteFilePath = "/New directory";
		String ftpUrl = "ftp://%s:%s@%s/%s;type=d";
		ftpUrl = String.format(ftpUrl, username, password, server, remoteFilePath);
		System.out.println("URL ---> " + ftpUrl);

		try {
			URL url = new URL(ftpUrl);
			URLConnection conn = url.openConnection();
			InputStream inputStream = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

			String line = null;
			System.out.println("--- START ---");
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				String downloading_dir = "D:/FTP Test/";
			}
			System.out.println("--- END ---");
		} catch (Exception e) {
		}
	}
}