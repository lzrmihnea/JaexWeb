package jaex.dto;

import java.text.MessageFormat;

public class JaexUser {

	public static final String FIELD_USERS = "users";
	public static final String FIELD_USER = "user";
	public static final String FIELD_USERNAME = "username";
	public static final String FIELD_EMAIL = "email";

	private static final String JAEX_USER_TOSTRING_TEMPLATE = "Class{0} username{1} email{2}";
	// private static final String JAEX_USER_TOSTRING_TEMPLATE1 = "Class%-20s
	// username%-25s email%-20s";

	private String username;
	private String email;

	public JaexUser() {
	}

	public JaexUser(String username, String email) {
		this.username = username;
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return MessageFormat.format(JAEX_USER_TOSTRING_TEMPLATE, inBrackets(this.getClass().getName()),
				inBrackets(username), inBrackets(email)).toString();
	}

	private String inBrackets(final String textToPlaceInBrackets) {
		return "[" + textToPlaceInBrackets + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj == null) || (obj.getClass() != this.getClass())) {
			return false;
		}
		JaexUser objAsJaexUser = (JaexUser) obj;
		if (this.username == null) {
			if (objAsJaexUser.getUsername() == null) {
				if (this.email == null) {
					if (objAsJaexUser.getEmail() == null) {
						return true;
					}
				} else {
					return this.email.equals(objAsJaexUser.getEmail());
				}
			}
		} else
			return this.username.equals(objAsJaexUser.getUsername());
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}

}
