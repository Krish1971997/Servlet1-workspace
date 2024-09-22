package busBookingSystem;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class BookingHistory {

	static Map<Integer, Customers> CancelTickets = new LinkedHashMap<>();
	static int cancelTicketsSeries = 1;

	public static void bookingHistory(Map<String, Customers> history) {
		for (Entry<String, Customers> map : history.entrySet()) {
			if (map.getValue() != null) {
				System.out.printf("Ticket :%s\tName: %s\tAge: %d\tGender: %s\tBus Type: %s\tBooking type: %s\tFare: %f",
						map.getKey(), map.getValue().getName(), map.getValue().getAge(), map.getValue().getGender(),
						map.getValue().getBusType(), map.getValue().getType(), map.getValue().getFare());
				System.out.println();
				
			}
		}
	}

	public static double cancelAllTickets(BusType busType, BookingType bookingType, Map<String, Customers> maps) {
		double busFare = 0, remainingFare = 0, remainingFareRefund = 0.00;

		for (Entry<String, Customers> map : maps.entrySet()) {
			String key = map.getKey();
			if (map.getValue() != null) {
				busFare = map.getValue().getFare();
				remainingFare = cancellationFeeCalc(busType, bookingType, busFare);
				remainingFareRefund += remainingFare;
				Customers customerGet = maps.get(key);
				customerGet.setFare(customerGet.getFare() - remainingFare);
				customerGet.setStatus(status.CANCELLED);
				customerGet.setKey(key);
				CancelTickets.put(cancelTicketsSeries++, customerGet);
				map.setValue(null);
			}
		}
		return remainingFareRefund;
	}

	public static void cancelPartialTickets(List<String> list, BusType busType, BookingType bookingType) {
		Map<String, Customers> booking = BookingTicket.bookingTypeCheck(busType, bookingType);
		double busFare = 0, remainingFare = 0, remainingFareRefund = 0.00;
		int bookedSeats = getBookedSeatsCount(booking), i = 0;
		if (bookedSeats > 0) {
			for (Entry<String, Customers> map : booking.entrySet()) {
				if (list.size() > i) {
					if (map.getKey().equalsIgnoreCase(list.get(i))) {
						busFare = map.getValue().getFare();
						remainingFare = cancellationFeeCalc(busType, bookingType, busFare);
						remainingFareRefund += remainingFare;
						Customers customerGet = booking.get(list.get(i));
						customerGet.setFare(customerGet.getFare() - remainingFare);
						customerGet.setKey(list.get(i));
						customerGet.setStatus(status.CANCELLED);
						CancelTickets.put(cancelTicketsSeries++, customerGet);
						map.setValue(null);
						i++;
					}
				}
			}
			System.out.println("Cancelled the tickets. You will get the refund amount is " + remainingFareRefund);
		} else
			System.out.println("There is no seats booked...");
	}

	private static int getBookedSeatsCount(Map<String, Customers> booking) {
		int count = 0;
		for (Entry<String, Customers> map : booking.entrySet()) {
			if (map.getValue() != null)
				count++;
		}
		return count;
	}

	private static double cancellationFeeCalc(BusType busType, BookingType bookingType, double busFare) {
		switch (busType) {
		case AC: {
			if (bookingType == BookingType.SEATER)
				return busFare * 50 / 100;
			else
				return busFare * 50 / 100;
		}
		case NONAC: {
			if (bookingType == BookingType.SEATER)
				return busFare * 75 / 100;
			else
				return busFare * 75 / 100;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + busType);
		}
	}

	public static void CancellationHistory() {
		// Seater AC
		for (Entry<Integer, Customers> map : CancelTickets.entrySet()) {
			if (map.getValue() != null) {
				System.out.println("ID : " + map.getKey() + " Ticket: " + map.getValue().getKey() + " Name: "
						+ map.getValue().getName() + " Age: " + map.getValue().getAge() + " Gender: "
						+ map.getValue().getGender() + " Bus Type: " + map.getValue().getBusType() + " Booking type: "
						+ map.getValue().getType() +" Status "+map.getValue().getStatus() +" Fare: " + map.getValue().getFare());
			}
		}
	}
}
