package com.udacity.jwdnd.course1.cloudstorage.forms;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage{

    @FindBy(id = "inputUsername")
    private WebElement username;

    @FindBy(id = "inputPassword")
    private WebElement password;

    @FindBy(id = "login-button")
    private WebElement loginButton;
    
    @FindBy(id = "signup-link")
    private WebElement signUp;

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void setUsername(String name) {
    	username.clear();
        username.sendKeys(name);
    }
    
    public void setPassword(String val) {
    	password.clear();
        password.sendKeys(val);
    }

    public void login() {
        loginButton.click();
    }
    
    public void goSignUp() {
    	signUp.click();
    }
}
