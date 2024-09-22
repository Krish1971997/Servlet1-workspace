package busBookingSystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.zoho.BusBookingUsingExcel.FtpDownloader;

public class Driver {
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) throws IOException {
		Customer customer = new Customer();
		int loginChoice;
		do {
			System.out.println("1. To creaet new account\n2. To login account");
			loginChoice = sc.nextInt();
			switch (loginChoice) {
			case 1: {
				System.out.println("Enter the Name");
				customer.setName(sc.next());
				System.out.println("Enter the Email");
				customer.setEmail(sc.next());
				System.out.println("Enter the Password");
				customer.setPassword(sc.next());

				boolean signUp = customer.createLogin(customer);
				System.out.println(signUp ? "New User created....." : "User creation failed.....");
				break;
			}
			case 2: {
				System.out.println("Enter the Email:");
				String email = sc.next();
				System.out.println("Enter the Password:");
				String password = sc.next();
				if (customer.loginCheck(email, password)) {
					System.out.println("Login Successful......\nWelcome Bus booking application");
					int choice = 0;
					BookingTicket bookingTicket = new BookingTicket();
					while (true) {
						System.out.println("\n1.Booking ticket\n2.Booing History\n3.Cancel ticket\n4.Cancel History"
								+ "\n5.Admin Login\n6.Import the excel\n7.Available Tickets\n8.FTP\n9.Logout");
						System.out.println("Enter the your choice....");
						choice = sc.nextInt();
						switch (choice) {
						case 1: {
							bookingTicket.availableTickets();
							System.out.println("\nDo you want to book AC or NONAC");
							BusType busType = BusType.valueOf(sc.next().toUpperCase());

							System.out.println("Do you want to book Seater or Sleeper");
							BookingType bookingType = BookingType.valueOf(sc.next().toUpperCase());

							System.out.println("How many tickets do you want to book");
							int person = sc.nextInt();
							double sum = 0.0;
							List<Customers> list = new ArrayList<>();
							int maxTickets=bookingTicket.maxTicketsValidation(busType,bookingType);
							if(maxTickets>=person) {
								for (int i = 0; i < person; i++) {
									Customers customers = new Customers();
									System.out.println("Enter your Name");
									customers.setName(sc.next());
									System.out.println("Enter your age");
									customers.setAge(sc.nextInt());
									System.out.println("Enter your Gender(M/F)");
									customers.setGender(sc.next().toUpperCase());
									customers.setBusType(busType);
									customers.setType(bookingType);
									customers.setStatus(status.CONFIRMED);
									double busFare = bookingTicket.fareCalculation(busType, bookingType);
									customers.setFare(busFare);
									sum += busFare;
									list.add(customers);
								}
								boolean bookingCheck = bookingTicket.bookTicket(list, busType, bookingType, person, sum);
								if (bookingCheck) {
									System.out.println("Booking is successful.......");
								} else
									System.out.println("Booking is not successful....");
								break;
							}else
								System.out.println("Please check available tickets. You have entered tickets more than available tickets");

							break;
						}
						case 2: {
							bookingTicket.history();
							break;
						}
						case 3: {
							bookingTicket.cancellation();
							break;
						}
						case 4: {
							BookingHistory.CancellationHistory();
							break;
						}
						case 5: {
							bookingTicket.adminLogin();
							break;
						}
						case 6: {
							File file = new File("C:\\Users\\Admin\\Documents\\BusbookingExcelsheet.xls");
							BookingTicket.importExcel(file);
							break;
						}
						case 7:{
							bookingTicket.availableTickets();
							break;
						}
						case 8: {
							FtpDownloader.ftpImport();
							break;
						}
						case 9: {
							System.out.println("Logout successful...");
							System.exit(0);
							break;
						}
						default:
							System.out.println("Please enter the correct choice...");
						}

					}
				} else
					System.out.println("Login failed......");

			}
			default:
				System.out.println("Please enter correct choice");
			}
		} while (loginChoice != 2);
	}
}
