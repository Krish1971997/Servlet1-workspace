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
}
