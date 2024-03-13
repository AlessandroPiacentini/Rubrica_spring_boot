package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class HomeController {
	@GetMapping("/")
	public String index() {
		return "index";
	}
}
