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
import org.openqa.selenium.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

import java.io.File;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private NoteService noteService;
	
	@Autowired
	private CredentialService credentialService;
	
	@Autowired
	private EncryptionService encryptionService;
	
	@Autowired
	private User elmerFudd;
	
	@Autowired
	private Notes existingNote;
	
	@Autowired
	private Credentials existingCredential;

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
		
		noteService.addNote(fuddId, existingNote);
		
		// add an existing credential for the existing user
		existingCredential.setUrl("www.gmail.com");
		existingCredential.setUsername("WabbitMan");
		existingCredential.setPassword("huhuhuhuhuh");
		
		existingCredential.setKey(encryptionService.generateRandomKey());
		existingCredential.setPassword(encryptionService.encryptValue(existingCredential.getPassword(), existingCredential.getKey()));
		
		credentialService.addCredential(fuddId, existingCredential);
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
		
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("successToast")));
		
		// get number of rows in table now! Needed because the first element could be deleted before this
		// is added, then the hardcoded xpath will be wrong!
		int lastRowCount = driver.findElement(By.id("noteTable")).findElements(By.tagName("tr")).size()-1;
		
		WebElement addedNoteTitle = driver.findElement(By.id("noteTable")).findElement(By.xpath("tbody/tr[" + lastRowCount + "]/th"));
		Assertions.assertEquals("Note Title Here", addedNoteTitle.getText());
		
		WebElement addedNoteDesc = driver.findElement(By.id("noteTable")).findElement(By.xpath("tbody/tr[" + lastRowCount + "]/td[2]"));
		Assertions.assertEquals("Note Description Here", addedNoteDesc.getText());
	}
	
	// Test that the the user can sign up, login, access the home page,
	// and navigate to the note tab and edit an existing note
	// This test is executed before the delete note test so there is something to edit!
	@Test
	@Order(1)
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
		
		// Get the existing note's title and description
		int existingNoteId = existingNote.getId();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("deleteNote" + existingNoteId)));
		
		WebElement firstNoteTitle = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div[1]/table/tbody/tr[2]/th"));
		String existingNoteTitle = firstNoteTitle.getText();
		
		WebElement firstNoteDesc = driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div[1]/table/tbody/tr[2]/td[2]"));
		String existingNoteDesc = firstNoteDesc.getText();
		
		// delete the existing note (first note)
		WebElement deleteNoteButton = driver.findElement(By.id("deleteNote" + existingNoteId));
		deleteNoteButton.click();
		
		// Check that the title and description are no longer on the page
		webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("deleteNote" + existingNoteId)));

		Assertions.assertFalse(driver.getPageSource().contains(existingNoteTitle) && driver.getPageSource().contains(existingNoteDesc));		
	}
	
	// Test that the the user can sign up, login, access the home page,
	// and navigate to the credential tab and add a new credential
	@Test
	public void testAddCredential() {
		doLogIn("WabbitHunter007", "W4bbits");
		
		// add note
		WebElement noteTab = driver.findElement(By.id("nav-credentials-tab"));
		noteTab.click();
		
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addCredential")));
		WebElement addNoteButton = driver.findElement(By.id("addCredential"));
		addNoteButton.click();
		
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement inputCredentialUrl = driver.findElement(By.id("credential-url"));
		inputCredentialUrl.click();
		inputCredentialUrl.sendKeys("Cred URL Here");
		
		WebElement inputCredentialUsername = driver.findElement(By.id("credential-username"));
		inputCredentialUsername.click();
		inputCredentialUsername.sendKeys("Cred Username");
		
		WebElement inputCredentialPassword = driver.findElement(By.id("credential-password"));
		inputCredentialPassword.click();
		inputCredentialPassword.sendKeys("Cred Password");
		
		WebElement saveCredentialButton = driver.findElement(By.id("postCredential"));
		saveCredentialButton.click();
		
		// get number of rows in table now! Needed because the first element could be deleted before this
		// is added, then the hardcoded xpath will be wrong!
		WebElement credentialTable = driver.findElement(By.id("credentialTable"));
		int lastRowCount = credentialTable.findElements(By.tagName("tr")).size() - 1;
		
		WebElement addedCredentialUrl = credentialTable.findElement(By.xpath("tbody/tr[" + lastRowCount + "]/th"));
		Assertions.assertEquals("Cred URL Here", addedCredentialUrl.getText());
		
		WebElement addedCredentialUsername = credentialTable.findElement(By.xpath("tbody/tr[" + lastRowCount + "]/td[2]"));
		Assertions.assertEquals("Cred Username", addedCredentialUsername.getText());
		
		// Check password text field against encrypted password in DB
		WebElement addedCredentialPassword = credentialTable.findElement(By.xpath("tbody/tr[" + lastRowCount + "]/td[3]"));
		String encryptedPassword = credentialService.getCredential(1, 2).getPassword();
		Assertions.assertEquals(encryptedPassword, addedCredentialPassword.getText());
	}
	
	// Test that the the user can sign up, login, access the home page,
	// and navigate to the credential tab and edit an existing credential
	@Test
	@Order(2)
	public void testEditCredential() {
		doLogIn("WabbitHunter007", "W4bbits");
		
		// add note
		WebElement noteTab = driver.findElement(By.id("nav-credentials-tab"));
		noteTab.click();
		
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		WebElement credentialTable = driver.findElement(By.id("credentialTable"));
		
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addCredential")));
		
		// get number of rows in table now! This will always run before adding a new credential, so
		// the existing credential is the last row! Row count will not change upon editing (in this test)
		int lastRowCount = credentialTable.findElements(By.tagName("tr")).size() - 1;	
				
		// get initial encrypted password to check if it changes when edited
		String initialEncryptedPassword = credentialTable.findElement(By.xpath("tbody/tr[" + lastRowCount + "]/td[3]")).getText();
		
		WebElement editCredentialButton = driver.findElement(By.id("editCredential1"));
		editCredentialButton.click();
		
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModal")));
		
		// Credential URL not updateable due to design decision - a user would just add a new credential
		// for different domain/account and would not change this value unless removing an account
//		WebElement inputCredentialUrl = driver.findElement(By.id("credential-url"));
//		inputCredentialUrl.click();
//		inputCredentialUrl.clear();
//		inputCredentialUrl.sendKeys("Edited Cred URL!");
		
		WebElement inputCredentialUsername = driver.findElement(By.id("credential-username"));
		inputCredentialUsername.click();
		inputCredentialUsername.clear();
		inputCredentialUsername.sendKeys("Edited Cred Username!");
		
		WebElement inputCredentialPassword = driver.findElement(By.id("credential-password"));
		inputCredentialPassword.click();
		inputCredentialPassword.clear();
		inputCredentialPassword.sendKeys("Edited Cred Password!");
		
		WebElement saveCredentialButton = driver.findElement(By.id("postCredential"));
		saveCredentialButton.click();
		
		// refresh credentialTable webElement
		credentialTable = driver.findElement(By.id("credentialTable"));
		
//		WebElement editedCredentialUrl = credentialTable.findElement(By.xpath("tbody/tr[" + lastRowCount + "]/th"));
//		Assertions.assertEquals("Edited Cred URL!", editedCredentialUrl.getText());
		
		WebElement editedCredentialUsername = credentialTable.findElement(By.xpath("tbody/tr[" + lastRowCount + "]/td[2]"));
		Assertions.assertEquals("Edited Cred Username!", editedCredentialUsername.getText());
		
		// check that new encrypted password is different from original encrypted password
		WebElement addedCredentialPassword = credentialTable.findElement(By.xpath("tbody/tr[" + lastRowCount + "]/td[3]"));
		Assertions.assertNotEquals(initialEncryptedPassword, addedCredentialPassword.getText());
	}
	
	// Test that the the user can sign up, login, access the home page,
	// and navigate to the credential tab and delete an existing credential
	@Test
	public void testDeleteCredential() {
		doLogIn("WabbitHunter007", "W4bbits");
		
		// add note
		WebElement noteTab = driver.findElement(By.id("nav-credentials-tab"));
		noteTab.click();
		
		int existingCredentialId = existingCredential.getId();
		
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		
		// assert existing credential is on webpage and can be deleted
		WebElement deleteExistingCredentialButton = driver.findElement(By.id("deleteCredential" + existingCredentialId));
		Assertions.assertNotNull(deleteExistingCredentialButton);
		
		// delete the existing credential
		deleteExistingCredentialButton.click();
		
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("successToast")));
		
		// check that the credential delete button is gone (credential has been deleted!)
		// Selenium throws an error if true		
		Assertions.assertThrows(NoSuchElementException.class,  
				() -> {
					driver.findElement(By.id("deleteCredential" + existingCredentialId));
					}
				);
	}

}
