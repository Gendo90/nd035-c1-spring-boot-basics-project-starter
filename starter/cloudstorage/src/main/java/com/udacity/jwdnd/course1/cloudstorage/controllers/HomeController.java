package com.udacity.jwdnd.course1.cloudstorage.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

@Controller
public class HomeController {
	
	private final FileService fileService;
	private final NoteService noteService;
	private final CredentialService credentialService;
	private final UserService userService;
	private final EncryptionService encryptionService;
	
	public HomeController(UserService userService, EncryptionService encryptionService, FileService fileService, NoteService noteService, CredentialService credentialService) {
        this.userService = userService;
        this.encryptionService = encryptionService;
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
    }
	
	@GetMapping("/home")
    public String homeView(Principal principal, Model model, @RequestParam(required = false) String currTab, 
    		@RequestParam(defaultValue = "0") String credentialId, @RequestParam(required = false) String success,
    		@RequestParam(required = false) String error, RedirectAttributes redirectAttrs) {
		try {
			User currUser = userService.getUser(principal.getName());
			System.out.println("The user returned for home is: " + currUser);
			int userId = currUser.getUserId();
			
			List<Files> currUserFiles = fileService.getUserFiles(userId);
			System.out.println("Number of files: " + currUserFiles.size());
			if(currUserFiles.size() > 0) System.out.println(currUserFiles.get(0).getFilename());
			model.addAttribute("userFiles", currUserFiles);
			
			List<Notes> currUserNotes = noteService.getUserNotes(userId);
			System.out.println("Number of notes: " + currUserNotes.size());
			if(currUserNotes.size() > 0) System.out.println(currUserNotes.get(0).getTitle());
			model.addAttribute("userNotes", currUserNotes);
			
			List<Credentials> currUserCredentials = credentialService.getUserCredentials(userId);
			System.out.println("Number of credentials: " + currUserCredentials.size());
			if(currUserCredentials.size() > 0) System.out.println(currUserCredentials.get(0).getUrl());
			model.addAttribute("userCredentials", currUserCredentials);
			
			if(currTab == null) {
				currTab = "fileTab";
			}
			model.addAttribute("currTab", currTab);
			
			System.out.println("The credentialId is " + credentialId);
			
			Integer credentialIdInt = Integer.parseInt(credentialId);
			if(credentialIdInt > 0) {
				Credentials currCredential = getDecryptedPasswordCredential(currUser.getUserId(), credentialIdInt);
				if(currCredential != null) model.addAttribute("currCredential", currCredential);
				if(currCredential != null) System.out.println("The decrypted password for home is: " + currCredential.getPassword());
			}
			
			if(success != null) {
				model.addAttribute("success", success);
			}
			else if(error != null) {
				model.addAttribute("error", error);
			}
			
	        return "home";
        
		} catch(Exception e) {
	    	System.out.println(e);
	    	model.addAttribute("currTab", "fileTab");
	    	model.addAttribute("error", "Something went wrong!");
	    	return "home";
	    }
    }
	
	@PostMapping("/addFile")
    public String addFile(Principal principal, MultipartFile uploadFile, Model model, RedirectAttributes redirectAttrs) throws IOException {
		try {
			redirectAttrs.addAttribute("currTab", "fileTab");
			// Do not allow uploads with empty files
			if(uploadFile.getSize() == 0) return "redirect:/home";
			
			System.out.println(principal.getName());
			User currUser = userService.getUser(principal.getName());
			System.out.println("The user returned is: " + currUser);
			
			Files storedFile = new Files();
			storedFile.setFilename(uploadFile.getOriginalFilename());
			storedFile.setFiledata(uploadFile.getBytes());
			storedFile.setContentType(uploadFile.getContentType());
			storedFile.setFilesize("" + uploadFile.getSize());
			
			int result = fileService.addFile(currUser.getUserId(), storedFile);
			// Duplicate filename condition
			if(result < 0) {
				redirectAttrs.addAttribute("error", "Duplicate filenames not allowed!");
				return "redirect:/home";
			}
			
			redirectAttrs.addAttribute("success", "File uploaded successfully!");
	        return "redirect:/home";
		} catch(Exception e) {
        	System.out.println(e);
        	redirectAttrs.addAttribute("currTab", "fileTab");
        	redirectAttrs.addAttribute("error", "Something went wrong!");
	        return "redirect:/home";
        }
    }
	
	@GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<byte[]> downloadFile(Principal principal, Model model, @PathVariable int fileId, RedirectAttributes redirectAttrs) {
		System.out.println(principal.getName());
		User currUser = userService.getUser(principal.getName());
		System.out.println("The user returned is: " + currUser);
		Files result = fileService.getFile(currUser.getUserId(), fileId);

		if(result == null) {
			return new ResponseEntity(HttpHeaders.EMPTY, HttpStatus.NOT_FOUND);
		}
		System.out.println(result.getFilename());
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + result.getFilename() + "\"").body(result.getFiledata());
    }
	
	@GetMapping("/deleteFile/{fileId}")
    public String deleteFile(Principal principal, Model model, @PathVariable int fileId, RedirectAttributes redirectAttrs) {
		try {
			System.out.println(principal.getName());
			User currUser = userService.getUser(principal.getName());
			System.out.println("The user returned is: " + currUser);
			fileService.deleteFile(currUser.getUserId(), fileId);
			redirectAttrs.addAttribute("currTab", "fileTab");
			redirectAttrs.addAttribute("success", "File removed successfully!");
	        return "redirect:/home";
		} catch(Exception e) {
        	System.out.println(e);
        	redirectAttrs.addAttribute("currTab", "fileTab");
        	redirectAttrs.addAttribute("error", "Something went wrong!");
	        return "redirect:/home";
        }
    }
	
	@PostMapping("/addNote")
    public String addNote(Principal principal, Notes note, Model model, RedirectAttributes redirectAttrs) {
		try {
			System.out.println(principal.getName());
			User currUser = userService.getUser(principal.getName());
			System.out.println("The user returned is: " + currUser);
			noteService.addNote(currUser.getUserId(), note);
			redirectAttrs.addAttribute("currTab", "noteTab");
			
			redirectAttrs.addAttribute("success", "Note created successfully!");
	        return "redirect:/home";
		} catch(Exception e) {
        	System.out.println(e);
        	redirectAttrs.addAttribute("currTab", "noteTab");
        	redirectAttrs.addAttribute("error", "Something went wrong!");
	        return "redirect:/home";
        }
    }
	
	@PostMapping("/updateNote")
    public String updateNote(Principal principal, Notes note, Model model, RedirectAttributes redirectAttrs) {
		try {
			System.out.println(principal.getName());
			User currUser = userService.getUser(principal.getName());
			System.out.println("The user returned is: " + currUser);
			noteService.updateNote(currUser.getUserId(), note);
			redirectAttrs.addAttribute("currTab", "noteTab");
			redirectAttrs.addAttribute("success", "Note updated successfully!");
	        return "redirect:/home";
		} catch(Exception e) {
        	System.out.println(e);
        	redirectAttrs.addAttribute("currTab", "noteTab");
        	redirectAttrs.addAttribute("error", "Something went wrong!");
	        return "redirect:/home";
        }
    }
	
	@GetMapping("/deleteNote/{noteId}")
    public String deleteNote(Principal principal, Model model, @PathVariable int noteId, RedirectAttributes redirectAttrs) {
		try {
			System.out.println(principal.getName());
			User currUser = userService.getUser(principal.getName());
			System.out.println("The user returned is: " + currUser);
			noteService.deleteNote(currUser.getUserId(), noteId);
			redirectAttrs.addAttribute("currTab", "noteTab");
			redirectAttrs.addAttribute("success", "Note removed successfully!");
	        return "redirect:/home";
		} catch(Exception e) {
        	System.out.println(e);
        	redirectAttrs.addAttribute("currTab", "noteTab");
        	redirectAttrs.addAttribute("error", "Something went wrong!");
	        return "redirect:/home";
        }
    }
	
	
	@PostMapping("/addCredential")
    public String addCredential(Principal principal, Credentials credential, Model model, RedirectAttributes redirectAttrs) {
		try {
			System.out.println(principal.getName());
			User currUser = userService.getUser(principal.getName());
			System.out.println("The user returned is: " + currUser);
			
			credential.setKey(encryptionService.generateRandomKey());
			credential.setPassword(encryptionService.encryptValue(credential.getPassword(), credential.getKey()));
			
			credentialService.addCredential(currUser.getUserId(), credential);
			redirectAttrs.addAttribute("currTab", "credentialTab");
			redirectAttrs.addAttribute("success", "Credential added successfully!");
	        return "redirect:/home";
		} catch(Exception e) {
        	System.out.println(e);
        	redirectAttrs.addAttribute("currTab", "credentialTab");
        	redirectAttrs.addAttribute("error", "Something went wrong!");
	        return "redirect:/home";
        }
    }
	
	//passes through to home only if user can access that credential!
	@GetMapping("/getCredential/{credentialId}")
    public String getCredential(Principal principal, @PathVariable Integer credentialId, Model model, RedirectAttributes redirectAttrs) {
		try {
			System.out.println(principal.getName());
			User currUser = userService.getUser(principal.getName());
			System.out.println("The user returned is: " + currUser);
			
			redirectAttrs.addAttribute("currTab", "credentialTab");
			
			Credentials currCredential = getDecryptedPasswordCredential(currUser.getUserId(), credentialId);
			
			System.out.println("The retrieved credential is: " + currCredential);
			
			//Block here if the credential is not one of the user's!
			if(currCredential == null) {
				redirectAttrs.addAttribute("error", "Something went wrong!");
				return "redirect:/home";
			}
			
			redirectAttrs.addAttribute("credentialId", "" + credentialId);
			
			return "redirect:/home";
		} catch(Exception e) {
        	System.out.println(e);
        	redirectAttrs.addAttribute("currTab", "credentialTab");
        	redirectAttrs.addAttribute("error", "Something went wrong!");
	        return "redirect:/home";
        }
    }
	
	@PostMapping("/updateCredential")
    public String updateCredential(Principal principal, Credentials credential, Model model, RedirectAttributes redirectAttrs) {
		try {
			System.out.println(principal.getName());
			User currUser = userService.getUser(principal.getName());
			System.out.println("The user returned is: " + currUser);
			
			redirectAttrs.addAttribute("currTab", "credentialTab");
			
			Credentials modCredential = credentialService.getCredential(currUser.getUserId(), credential.getId());
			if(modCredential == null) {
				redirectAttrs.addAttribute("error", "Something went wrong!");
				return "redirect:/home";
			}
			//Encrypt password here - since it can only be changed by the user when decrypted!
			String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), modCredential.getKey());
			modCredential.setPassword(encryptedPassword);
			modCredential.setUsername(credential.getUsername());
			
			
			//Service checks if the credential can be updated for this user
			credentialService.updateCredential(currUser.getUserId(), modCredential);
			redirectAttrs.addAttribute("success", "Credential updated successfully!");
	        return "redirect:/home";
		} catch(Exception e) {
        	System.out.println(e);
        	redirectAttrs.addAttribute("currTab", "credentialTab");
        	redirectAttrs.addAttribute("error", "Something went wrong!");
	        return "redirect:/home";
        }
    }
	
	@GetMapping("/deleteCredential/{credentialId}")
    public String deleteCredential(Principal principal, Model model, @PathVariable int credentialId, RedirectAttributes redirectAttrs) {
		try {
			System.out.println(principal.getName());
			User currUser = userService.getUser(principal.getName());
			System.out.println("The user returned is: " + currUser);
			credentialService.deleteCredential(currUser.getUserId(), credentialId);
			redirectAttrs.addAttribute("currTab", "credentialTab");
			redirectAttrs.addAttribute("success", "Credential removed successfully!");
	        return "redirect:/home";
		} catch(Exception e) {
        	System.out.println(e);
        	redirectAttrs.addAttribute("currTab", "credentialTab");
        	redirectAttrs.addAttribute("error", "Something went wrong!");
	        return "redirect:/home";
        }
    }
	
	public Credentials getDecryptedPasswordCredential(int userId, int credentialId) {
		Credentials currCredential = credentialService.getCredential(userId, credentialId);
		if(currCredential == null) {
			return null;
		}
		
		
		String encryptedPassword = currCredential.getPassword();
		String key = currCredential.getKey();
		
		String decryptedPassword = encryptionService.decryptValue(encryptedPassword, key);
		currCredential.setPassword(decryptedPassword);
		
		return currCredential;
	}
}
