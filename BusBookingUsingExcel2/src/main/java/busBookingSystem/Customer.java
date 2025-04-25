package busBookingSystem;

import java.util.HashSet;
import java.util.Set;

public class Customer {
	String name;
	String email;
	String password;
	static Set<Customer> newLogin;

	public Customer() {
	}

	public Customer(String name, String email, String password) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Customer [name=" + name + ", email=" + email + ", password=" + password + "]";
	}

	public boolean createLogin(Customer customer) {
		newLogin = new HashSet<>();
		if (customer != null) {
			newLogin.add(customer);
			return true;
		}
		return false;
	}

	public boolean loginCheck(String email2, String password2) {
		for (Customer customer : newLogin) {
			if (email2.equals( customer.email) && password2.equals(customer.password))
				return true;
		}
		return false;
	}
}
