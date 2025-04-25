package banking.utils;

import banking.models.User;

public class ThreadlocalClass {
	private static final InheritableThreadLocal<User> currentUser = new InheritableThreadLocal<>() {
		@Override
		protected User initialValue() {
			return null;
		}
	};
	private static final ThreadLocal<User> destinationUser = ThreadLocal.withInitial(() -> null);

	public static void setCurrentUser(User user) {
		currentUser.set(user);
	}

	public static void setDestinationUser(User user) {
		destinationUser.set(user);
	}

	public static User getCurrentUser() {
		return currentUser.get();
	}

	public static User getDestinationUser() {
		return destinationUser.get();
	}

	public static void remove() {
		currentUser.remove();
		destinationUser.remove();
	}
}
