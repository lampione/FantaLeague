package model;

import java.sql.Date;

public class User {

	private String firstName;

	private String lastName;

	private String email;

	private Date born;

	public User() {

	}

	public User(String firstName, String lastName, String email, Date born) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.born = born;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getBorn() {
		return born;
	}

	public void setBorn(Date born) {
		this.born = born;
	}

}
