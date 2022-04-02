package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

import java.io.File;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private NoteService noteService;
	
	@Autowired
	private User elmerFudd;
	
	@Autowired
	private Notes existingNote;

	@BeforeAll
	void beforeAll() {
		WebDriverManager.chromedriver().setup();
		
		// add a user here that serves as an existing user (so signups are not repeated for tests)
		// directly add to DB - do not use signup form!
		elmerFudd.setUsername("WabbitHunter007");
		elmerFudd.setPassword("W4bbits");
		elmerFudd.setFirstname("Elmer");
		elmerFudd.setLastname("Fudd");
		
		int fuddId = userService.createUser(elmerFudd);
		
		// add an existing note for the existing user
		existingNote.setTitle("Buy New Outfit");
		existingNote.setDescription("I need to buy more hunting clothes to get the wabbits. Maybe camouflage will work better than dwab bwown!");
		
		int existingNoteId = noteService.addNote(fuddId, existingNote);
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depending on the rest of your code.
		*/
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}

	
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a successful sign up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");
		
		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}
	
	// Test that the home page is not accessible without logging in
	@Test
	public void testHomePageAccess() {
		driver.get("http://localhost:" + this.port + "/home");
		
		Assertions.assertFalse(driver.getTitle().contains("Home"));
	}

	// Test that the the user can sign up, login, and access the home page
	// and then logout and no longer have access to the home page
	@Test
	public void testLogout() {
		// Create a test account
		doMockSignUp("URL","Test","UT2","123");
		doLogIn("UT2", "123");
		
		// Check home page access
		driver.get("http://localhost:" + this.port + "/home");
		
		Assertions.assertTrue(driver.getTitle().contains("Home"));
		
		// Logout
		WebElement logoutButton = driver.findElement(By.id("logout"));
		logoutButton.click();
		
		// Check home page access
		driver.get("http://localhost:" + this.port + "/home");
		
		Assertions.assertFalse(driver.getTitle().contains("Home"));
	}
	
	// Test that the the user can sign up, login, access the home page,
	// and navigate to the note tab and add a new note
	@Test
	public void testAddNote() {
		doLogIn("WabbitHunter007", "W4bbits");
		
		// add note
		WebElement noteTab = driver.findElement(By.id("nav-notes-tab"));
		noteTab.click();
		
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addNote")));
		WebElement addNoteButton = driver.findElement(By.id("addNote"));
		addNoteButton.click();
		
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement inputNoteTitle = driver.findElement(By.id("note-title"));
		inputNoteTitle.click();
		inputNoteTitle.sendKeys("Note Title Here");
		
		WebElement inputNoteDescription = driver.findElement(By.id("note-description"));
		inputNoteDescription.click();
		inputNoteDescription.sendKeys("Note Description Here");
		
		WebElement saveNoteButton = driver.findElement(By.id("postNote"));
		saveNoteButton.click();
		
		WebElement addedNoteTitle = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div[1]/table/tbody/tr[3]/th"));
		Assertions.assertEquals("Note Title Here", addedNoteTitle.getText());
		
		WebElement addedNoteDesc = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div[1]/table/tbody/tr[3]/td[2]"));
		Assertions.assertEquals("Note Description Here", addedNoteDesc.getText());
	}
	
	// Test that the the user can sign up, login, access the home page,
	// and navigate to the note tab and edit an existing note
	@Test
	public void testEditNote() {
		doLogIn("WabbitHunter007", "W4bbits");
		
		// edit note
		WebElement noteTab = driver.findElement(By.id("nav-notes-tab"));
		noteTab.click();
		
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addNote")));
		WebElement addNoteButton = driver.findElement(By.id("editNote1"));
		addNoteButton.click();
		
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement inputNoteTitle = driver.findElement(By.id("note-title"));
		inputNoteTitle.click();
		inputNoteTitle.clear();
		inputNoteTitle.sendKeys("Edited Note Title");
		
		WebElement inputNoteDescription = driver.findElement(By.id("note-description"));
		inputNoteDescription.click();
		inputNoteDescription.clear();
		inputNoteDescription.sendKeys("Edited Note Description");
		
		WebElement saveNoteButton = driver.findElement(By.id("postNote"));
		saveNoteButton.click();
		
		WebElement firstNoteTitle = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div[1]/table/tbody/tr[2]/th"));
		Assertions.assertEquals("Edited Note Title", firstNoteTitle.getText());
		
		WebElement firstNoteDesc = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div[1]/table/tbody/tr[2]/td[2]"));
		Assertions.assertEquals("Edited Note Description", firstNoteDesc.getText());
	}
	
	// Test that the the user can sign up, login, access the home page,
	// and navigate to the note tab and delete an existing note
	@Test
	public void testDeleteNote() {
		doLogIn("WabbitHunter007", "W4bbits");
		
		// delete note
		WebElement noteTab = driver.findElement(By.id("nav-notes-tab"));
		noteTab.click();
		
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		
		// Get the existing note's title and description - may be changed by testEditNote
		// depending on the randomized test order, so not hard-coded here!
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("deleteNote1")));
		
		WebElement firstNoteTitle = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div[1]/table/tbody/tr[2]/th"));
		String existingNoteTitle = firstNoteTitle.getText();
		
		WebElement firstNoteDesc = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div[1]/table/tbody/tr[2]/td[2]"));
		String existingNoteDesc = firstNoteDesc.getText();
		
		// delete the existing note (first note)
		WebElement deleteNoteButton = driver.findElement(By.id("deleteNote1"));
		deleteNoteButton.click();
		
		// Check that the title and description are no longer on the page
		webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("deleteNote1")));

		Assertions.assertFalse(driver.getPageSource().contains(existingNoteTitle) && driver.getPageSource().contains(existingNoteDesc));		
	}

}
