package com.zoho.BusBookingUsingExcel;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.nio.file.Files;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import busBookingSystem.BookingTicket;

class FtpConnector {
	public FTPClient connect() throws IOException {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect("localhost", 21);

			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				ftpClient.disconnect();
				System.out.println("FTP is not connected");
			}
			boolean success = ftpClient.login("testuser1", "1234");
			if (!success)
				ftpClient.disconnect();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			boolean changedRemoteDir = ftpClient.changeWorkingDirectory("/New directory");
			if (!changedRemoteDir) {
				System.out.println("Directory not found");
			}
		} catch (UnknownHostException E) {
			System.out.println("No such ftp server");
		} catch (IOException e) {
			System.out.println(e);
		}
		return ftpClient;
	}
}

public class FtpDownloader {
	public static void ftpImport() throws IOException {
		FtpConnector ftpConnector = new FtpConnector();
		FTPClient ftpClient = ftpConnector.connect();
		FTPFile[] ftpFiles = ftpClient.listFiles();
		String downloading_dir = "D:/FTP Test/";

		for (FTPFile file : ftpFiles) {
			File fileObj = new File(downloading_dir + file.getName());
			Files.createFile(fileObj.toPath());
			try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileObj))) {
				
				boolean isFileRetrieve = ftpClient.retrieveFile(file.getName(), outputStream);
				System.out.println(isFileRetrieve ? "File is imported " : "file is not imported");
				outputStream.close();
				BookingTicket.importExcel(fileObj);

				if (fileObj.delete())
					System.out.println(fileObj.getName() + " deleted");
				else
					System.err.println(fileObj.getName() + " not deleted");
			}
		}
	}
}
