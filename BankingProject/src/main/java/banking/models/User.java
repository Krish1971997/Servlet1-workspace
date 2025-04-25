package banking.models;

public class User {
	private String userId;
	private String name;
	private String address;
	private String phoneNumber;
	private String passwd;

	public User(String userId, String name, String address, String phoneNumber) {
		this.userId = userId;
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}

	public User(String name, String address, String phoneNumber) {
		this.userId = "";
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}

	public String getUserId() {
		return this.userId;
	}

	public String getName() {
		return this.name;
	}

	public String getAddress() {
		return this.address;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public String getPasswd() {
		return this.passwd;
	}

	public void setUserId(String id) {
		this.userId = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setPhoneNumber(String number) {
		this.phoneNumber = number;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	@Override
	public String toString() {
		return ("User ID: " + this.userId + "\nName: " + this.name + "\nAddress: " + this.address + "\nPhoneNumber: "
				+ this.phoneNumber);
	}
}
