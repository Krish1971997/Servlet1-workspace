package com.zoho;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import excelTitleGet.WebScraperTest;

public class ScheduledTask {

	public static void main(String[] args) {
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

		// Define the task to be executed
		Runnable task = new Runnable() {
			public void run() {
				// Write the code to execute your Java program here
				//WebScraperTest.runProgram();
				//;
				System.out.println("Executing scheduled task...");
			}
		};
		LocalDateTime now = LocalDateTime.now();
		LocalTime desiredTime = LocalTime.of(8, 0); // Adjust the desired execution time here (e.g., 8:00 AM)
		LocalDateTime nextExecutionTime = now.with(desiredTime);
		if (nextExecutionTime.isBefore(now)) {
			nextExecutionTime = nextExecutionTime.plusDays(1); // Schedule for the next day if the desired time has
																// already passed today
		}
		//long initialDelay = now.until(nextExecutionTime, TimeUnit.SECONDS);

		//scheduler.scheduleAtFixedRate(task, initialDelay, 24 * 60 * 60, TimeUnit.SECONDS);

	}
}