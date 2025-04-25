package busBookingSystem;

enum gender {
	M, F
};

enum status {
	CONFIRMED, CANCELLED
};

public class Customers {
	String name;
	int age;
	String gender;
	BookingType type;
	BusType busType;
	double fare;
	status status;
	String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Customers() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public BookingType getType() {
		return type;
	}

	public void setType(BookingType type) {
		this.type = type;
	}

	public double getFare() {
		return fare;
	}

	public void setFare(double fare) {
		this.fare = fare;
	}

	public void setAge(int i) {
		this.age = i;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public BusType getBusType() {
		return busType;
	}

	public void setBusType(BusType busType) {
		this.busType = busType;
	}

	public status getStatus() {
		return status;
	}

	public void setStatus(status confirmed) {
		this.status = confirmed;
	}

	@Override
	public String toString() {
		return "Customers [name=" + name + ", age=" + age + ", gender=" + gender + ", type=" + type + ", busType="
				+ busType + ", fare=" + fare + ", status=" + status + "]";
	}

}
