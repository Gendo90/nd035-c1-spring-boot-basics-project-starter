package com.udacity.jwdnd.course1.cloudstorage.model;
import org.springframework.stereotype.Component;

@Component
public class Notes {
	private Integer id;
	private String title;
	private String description;
	private Integer userId;
	
//	public Notes(Integer id, String title, String description, Integer userId) {
//		this.id = id;
//		this.title = title;
//		this.description = description;
//		this.userId = userId;
//	}
	
	//getters and setters
	
	public Integer getId() {
		return this.id;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public Integer getUserId() {
		return this.userId;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
