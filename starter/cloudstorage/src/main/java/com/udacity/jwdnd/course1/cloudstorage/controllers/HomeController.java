package com.udacity.jwdnd.course1.cloudstorage.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

@Controller
//@RequestMapping("/home")
public class HomeController {
	
	//add HomeService field here, with constructor for dependency injection!
	private final NoteService noteService;
	private final CredentialService credentialService;
	private final UserService userService;
	
	public HomeController(UserService userService, NoteService noteService, CredentialService credentialService) {
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService = credentialService;
    }
	
	@GetMapping("/home")
    public String homeView(Principal principal, Model model) {
		User currUser = userService.getUser(principal.getName());
		System.out.println("The user returned for home is: " + currUser);
		
		List<Notes> currUserNotes = noteService.getUserNotes(currUser.getUserId());
		System.out.println("Number of notes: " + currUserNotes.size());
		if(currUserNotes.size() > 0) System.out.println(currUserNotes.get(0).getTitle());
		model.addAttribute("userNotes", currUserNotes);
        return "home";
    }
	
	@PostMapping("/addNote")
    public String addNote(Principal principal, Notes note, Model model) {
		System.out.println(principal.getName());
		User currUser = userService.getUser(principal.getName());
		System.out.println("The user returned is: " + currUser);
		noteService.addNote(currUser.getUserId(), note);
        return "redirect:/home";
    }
	
	@GetMapping("/deleteNote/{noteId}")
    public String deleteNote(Principal principal, Model model, @PathVariable int noteId) {
		System.out.println(principal.getName());
		User currUser = userService.getUser(principal.getName());
		System.out.println("The user returned is: " + currUser);
		noteService.deleteNote(currUser.getUserId(), noteId);
        return "redirect:/home";
    }
	
	@PostMapping("/addCredential")
    public String addCredential(Principal principal, Credentials credential, Model model) {
		System.out.println(principal.getName());
		User currUser = userService.getUser(principal.getName());
		System.out.println("The user returned is: " + currUser);
		credentialService.addCredential(currUser.getUserId(), credential);
        return "redirect:/home";
    }
}
