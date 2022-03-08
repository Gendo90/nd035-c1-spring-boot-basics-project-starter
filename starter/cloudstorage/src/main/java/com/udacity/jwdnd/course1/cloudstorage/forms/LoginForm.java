package com.udacity.jwdnd.course1.cloudstorage.forms;

public class LoginForm {
	private String username;
	private String password;
	private boolean isInvalid;
	private boolean justLoggedOut;
	
	public LoginForm(String username, String password, boolean isInvalid, boolean justLoggedOut) {
		this.username = username;
		this.password = password;
		this.isInvalid = isInvalid;
		this.justLoggedOut = justLoggedOut;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public boolean getIsInvalid() {
		return this.isInvalid;
	}
	
	public boolean getJustLoggedOut() {
		return this.justLoggedOut;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setIsInvalid(boolean isInvalid) {
		this.isInvalid = isInvalid;
	}
	
	public void setJustLoggedOut(boolean justLoggedOut) {
		this.justLoggedOut = justLoggedOut;
	}
}
