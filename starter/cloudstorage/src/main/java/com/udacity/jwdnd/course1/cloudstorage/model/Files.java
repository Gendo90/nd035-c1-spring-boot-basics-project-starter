package com.udacity.jwdnd.course1.cloudstorage.model;
import org.springframework.stereotype.Component;
import java.sql.Blob;

@Component
public class Files {
	private Integer id;
	private String filename;
	private String contentType;
	private String filesize;
	private byte[] filedata;
	private Integer userId;

//	public Files(int id, String filename, String contentType, String filesize, Blob filedata, int userId) {
//		this.id = id;
//		this.filename = filename;
//		this.contentType = contentType;
//		this.filesize = filesize;
//		this.filedata = filedata;
//		this.userId = userId;
//	}
	
	//getters and setters
	
	public Integer getId() {
		return this.id;
	}
	
	public String getFilename() {
		return this.filename;
	}
	
	public String getContentType() {
		return this.contentType;
	}
	
	public String getFilesize() {
		return this.filesize;
	}
	
	public byte[] getFiledata() {
		return this.filedata;
	}
	
	public Integer getUserId() {
		return this.userId;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}
	
	public void setFiledata(byte[] filedata) {
		this.filedata = filedata;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
