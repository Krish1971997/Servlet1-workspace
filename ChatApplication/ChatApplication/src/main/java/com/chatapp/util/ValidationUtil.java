package com.chatapp.util;

import org.apache.commons.validator.routines.EmailValidator;

public class ValidationUtil {

	private static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance();

	public static boolean isValidEmail(String email) {
		return email != null && EMAIL_VALIDATOR.isValid(email.trim());
	}

	public static boolean isValidUsername(String username) {
		// 3-50 chars, alphanumeric + underscore
		return username != null && username.matches("^[a-zA-Z0-9_]{3,50}$");
	}

	public static boolean isValidPassword(String password) {
		// Min 6 chars
		return password != null && password.length() >= 6;
	}

	public static boolean isValidRole(String role) {
		return ("user".equalsIgnoreCase(role)) || ("admin".equalsIgnoreCase(role));
	}
	
	public static String isSignUpValid(String username, String email,
			String password, String confirm, String role) {
		
		if (!ValidationUtil.isValidUsername(username)) {
			return "Username must be 3-50 chars (letters, digits, _).";
		}
		if (!ValidationUtil.isValidEmail(email)) {
			return "Invalid email address.";
		}
		if (!ValidationUtil.isValidPassword(password)) {
			return "Password must be at least 6 characters.";
		}
		if (!password.equals(confirm)) {
			return "Passwords do not match.";
		}
		if (!ValidationUtil.isValidRole(role)) {
			return "Invalid role.";
		}
		return null;
	}
}
