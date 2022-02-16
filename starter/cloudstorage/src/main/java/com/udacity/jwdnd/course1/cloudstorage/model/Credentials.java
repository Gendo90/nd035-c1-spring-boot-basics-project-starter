package com.udacity.jwdnd.course1.cloudstorage.model;
import org.springframework.stereotype.Component;

@Component
public class Credentials {
	private Integer id;
	private String url;
	private String username;
	private String key;
	private String password;
	private Integer userId;
	
	public Credentials(int id, String url, String username, String key, String password, int userId) {
		this.id = id;
		this.url = url;
		this.username = username;
		this.key = key;
		this.password = password;
		this.userId = userId;
	}
	
	//getters and setters
	
	public Integer getId() {
		return this.id;
	}
	
	public String getUrl() {
		return this.url;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getKey() {
		return this.key;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public Integer getUserId() {
		return this.userId;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
}