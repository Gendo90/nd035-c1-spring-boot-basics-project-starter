package com.udacity.jwdnd.course1.cloudstorage.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {
	
	//add HomeService field here, with constructor for dependency injection!
	
	@GetMapping()
    public String homeView() {
        return "home";
    }
}
