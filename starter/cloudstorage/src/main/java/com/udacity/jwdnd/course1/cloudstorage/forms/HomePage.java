package com.udacity.jwdnd.course1.cloudstorage.forms;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class HomePage {
	//Note elements first
	@FindBy(id = "note-title")
    private WebElement noteTitle;
	
	@FindBy(id = "note-description")
    private WebElement noteDescription;
	
	// Credentials elements next
	@FindBy(id = "credential-url")
    private WebElement credentialURL;
	
	@FindBy(id = "credential-username")
    private WebElement credentialUsername;
	
	@FindBy(id = "credential-password")
    private WebElement credentialPassword;
	
	// File elements last
	@FindBy(id = "fileUpload")
    private WebElement fileUpload;
	
	@FindBy(id = "uploadButton")
    private WebElement uploadButton;
	
	// Common navigation elements - if necessary!

//    public HomePage(WebDriver driver) {
//        PageFactory.initElements(driver, this);
//    }
//    
//    public void setFirstName(String name) {
//    	firstname.clear();
//        firstname.sendKeys(name);
//    }
//    
//    public void setLastName(String lastname) {
//    	surname.clear();
//        surname.sendKeys(lastname);
//    }
//
//    public void setUsername(String name) {
//    	username.clear();
//        username.sendKeys(name);
//    }
//    
//    public void setPassword(String val) {
//    	password.clear();
//        password.sendKeys(val);
//    }
//
//    public void signup() {
//        signupButton.click();
//    }
//    
//    public void login() {
//        loginButton.click();
//    }
}
