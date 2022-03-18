package com.udacity.jwdnd.course1.cloudstorage.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String homeView(Principal principal, Model model, @RequestParam(required = false) String currTab) {
		User currUser = userService.getUser(principal.getName());
		System.out.println("The user returned for home is: " + currUser);
		
		List<Notes> currUserNotes = noteService.getUserNotes(currUser.getUserId());
		System.out.println("Number of notes: " + currUserNotes.size());
		if(currUserNotes.size() > 0) System.out.println(currUserNotes.get(0).getTitle());
		model.addAttribute("userNotes", currUserNotes);
		if(currTab == null) {
			currTab = "fileTab";
		}
		model.addAttribute("currTab", currTab);
        return "home";
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
	
	@GetMapping("/deleteNote/{noteId}")
    public String deleteNote(Principal principal, Model model, @PathVariable int noteId, RedirectAttributes redirectAttrs) {
		System.out.println(principal.getName());
		User currUser = userService.getUser(principal.getName());
		System.out.println("The user returned is: " + currUser);
		noteService.deleteNote(currUser.getUserId(), noteId);
		redirectAttrs.addAttribute("currTab", "noteTab");
        return "redirect:/home";
    }
	
	@PostMapping("/addCredential")
    public String addCredential(Principal principal, Credentials credential, Model model, RedirectAttributes redirectAttrs) {
		System.out.println(principal.getName());
		User currUser = userService.getUser(principal.getName());
		System.out.println("The user returned is: " + currUser);
		credentialService.addCredential(currUser.getUserId(), credential);
		redirectAttrs.addAttribute("currTab", "credentialTab");
        return "redirect:/home";
    }
}
