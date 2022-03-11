package com.udacity.jwdnd.course1.cloudstorage.forms;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class SignUpPage {
	@FindBy(id = "inputFirstName")
    private WebElement firstname;

    @FindBy(id = "inputLastName")
    private WebElement surname;

    @FindBy(id = "inputUsername")
    private WebElement username;
    
    @FindBy(id = "inputPassword")
    private WebElement password;
    
    @FindBy(id = "buttonSignUp")
    private WebElement signupButton;
    
    @FindBy(id = "login-link")
    private WebElement loginButton;

    public SignUpPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }
    
    public void setFirstName(String name) {
    	firstname.clear();
        firstname.sendKeys(name);
    }
    
    public void setLastName(String lastname) {
    	surname.clear();
        surname.sendKeys(lastname);
    }

    public void setUsername(String name) {
    	username.clear();
        username.sendKeys(name);
    }
    
    public void setPassword(String val) {
    	password.clear();
        password.sendKeys(val);
    }

    public void signup() {
        signupButton.click();
    }
    
    public void login() {
        loginButton.click();
    }
}
