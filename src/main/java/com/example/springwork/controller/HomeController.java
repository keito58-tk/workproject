package com.example.springwork.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class HomeController {
	@GetMapping("/")
	public String index() {
		System.out.println("こんにちは");
		return "index";	
		}
}
