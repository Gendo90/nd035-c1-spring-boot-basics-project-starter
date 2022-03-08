package com.udacity.jwdnd.course1.cloudstorage.forms;


public class SignUpForm {
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private boolean signUpError;
	private boolean signUpSuccess;
	
	public SignUpForm(String firstName, String lastName, String username, String password, boolean signUpError, boolean signUpSuccess) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.signUpError = signUpError;
		this.signUpSuccess = signUpSuccess;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public boolean getSignUpError() {
		return this.signUpError;
	}
	
	public boolean getSignUpSuccess() {
		return this.signUpSuccess;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setSignUpError(boolean signUpError) {
		this.signUpError = signUpError;
	}
	
	public void setSignUpSuccess(boolean signUpSuccess) {
		this.signUpSuccess= signUpSuccess;
	}
}
