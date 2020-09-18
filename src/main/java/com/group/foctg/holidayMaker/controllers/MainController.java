package com.group.foctg.holidayMaker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
	
	@GetMapping("/")
	public String init() {
		return "landingpage.html";
	}
	
	@GetMapping("/login")
	public String init2() {
		return "login.html";
	}

}
