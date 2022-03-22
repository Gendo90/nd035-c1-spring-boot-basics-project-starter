package com.udacity.jwdnd.course1.cloudstorage.controllers;

import java.io.File;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

@Controller
//@RequestMapping("/home")
public class HomeController {
	
	//add HomeService field here, with constructor for dependency injection!
	private final FileService fileService;
	private final NoteService noteService;
	private final CredentialService credentialService;
	private final UserService userService;
	
	public HomeController(UserService userService, FileService fileService, NoteService noteService, CredentialService credentialService) {
        this.userService = userService;
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
    }
	
	@GetMapping("/home")
    public String homeView(Principal principal, Model model, @RequestParam(required = false) String currTab) {
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
        return "home";
    }
	
	@PostMapping("/addFile")
    public String addFile(Principal principal, MultipartFile uploadFile, Model model, RedirectAttributes redirectAttrs) throws IOException {
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
		
		fileService.addFile(currUser.getUserId(), storedFile);
		redirectAttrs.addAttribute("currTab", "fileTab");
        return "redirect:/home";
    }
	
	@GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<byte[]> downloadFile(Principal principal, Model model, @PathVariable int fileId, RedirectAttributes redirectAttrs) {
		System.out.println(principal.getName());
		User currUser = userService.getUser(principal.getName());
		System.out.println("The user returned is: " + currUser);
		Files result = fileService.getFile(currUser.getUserId(), fileId);
//		redirectAttrs.addAttribute("currTab", "fileTab");
//        return "redirect:/home";
//		return result;
		if(result == null) {
			return new ResponseEntity(HttpHeaders.EMPTY, HttpStatus.NOT_FOUND);
		}
		System.out.println(result.getFilename());
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + result.getFilename() + "\"").body(result.getFiledata());
    }
	
	@GetMapping("/deleteFile/{fileId}")
    public String deleteFile(Principal principal, Model model, @PathVariable int fileId, RedirectAttributes redirectAttrs) {
		System.out.println(principal.getName());
		User currUser = userService.getUser(principal.getName());
		System.out.println("The user returned is: " + currUser);
		fileService.deleteFile(currUser.getUserId(), fileId);
		redirectAttrs.addAttribute("currTab", "fileTab");
        return "redirect:/home";
    }
	
	@PostMapping("/addNote")
    public String addNote(Principal principal, Notes note, Model model, RedirectAttributes redirectAttrs) {
		System.out.println(principal.getName());
		User currUser = userService.getUser(principal.getName());
		System.out.println("The user returned is: " + currUser);
		noteService.addNote(currUser.getUserId(), note);
		redirectAttrs.addAttribute("currTab", "noteTab");
        return "redirect:/home";
    }
	
	@PostMapping("/updateNote")
    public String updateNote(Principal principal, Notes note, Model model, RedirectAttributes redirectAttrs) {
		System.out.println(principal.getName());
		User currUser = userService.getUser(principal.getName());
		System.out.println("The user returned is: " + currUser);
		noteService.updateNote(currUser.getUserId(), note);
		redirectAttrs.addAttribute("currTab", "noteTab");
        return "redirect:/home";
    }
	
	@GetMapping("/deleteNote/{noteId}")
    public String deleteNote(Principal principal, Model model, @PathVariable int noteId, RedirectAttributes redirectAttrs) {
		System.out.println(principal.getName());
		User currUser = userService.getUser(principal.getName());
		System.out.println("The user returned is: " + currUser);
		noteService.deleteNote(currUser.getUserId(), noteId);
		redirectAttrs.addAttribute("currTab", "noteTab");
        return "redirect:/home";
    }
	
	
	// NOTE: Need to encrypt password upon adding it to the database - see work TODO
	@PostMapping("/addCredential")
    public String addCredential(Principal principal, Credentials credential, Model model, RedirectAttributes redirectAttrs) {
		System.out.println(principal.getName());
		User currUser = userService.getUser(principal.getName());
		System.out.println("The user returned is: " + currUser);
		credentialService.addCredential(currUser.getUserId(), credential);
		redirectAttrs.addAttribute("currTab", "credentialTab");
        return "redirect:/home";
    }
	
	@PostMapping("/updateCredential")
    public String updateCredential(Principal principal, Credentials credential, Model model, RedirectAttributes redirectAttrs) {
		System.out.println(principal.getName());
		User currUser = userService.getUser(principal.getName());
		System.out.println("The user returned is: " + currUser);
		credentialService.updateCredential(currUser.getUserId(), credential);
		redirectAttrs.addAttribute("currTab", "credentialTab");
        return "redirect:/home";
    }
	
	@GetMapping("/deleteCredential/{credentialId}")
    public String deleteCredential(Principal principal, Model model, @PathVariable int credentialId, RedirectAttributes redirectAttrs) {
		System.out.println(principal.getName());
		User currUser = userService.getUser(principal.getName());
		System.out.println("The user returned is: " + currUser);
		credentialService.deleteCredential(currUser.getUserId(), credentialId);
		redirectAttrs.addAttribute("currTab", "credentialTab");
        return "redirect:/home";
    }
}
