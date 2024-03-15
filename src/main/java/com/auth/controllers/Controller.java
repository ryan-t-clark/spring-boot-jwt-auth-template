package com.auth.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.demo.DemoApplication;

@RestController
@CrossOrigin(origins = "http://localhost:80") //add origin of requests -- port 80 is http
public class Controller {

	private static final Logger LOG = LoggerFactory.getLogger(DemoApplication.class);
	
	@GetMapping("/")
	public ResponseEntity<String> index() {
		LOG.info("Route '/' reached");
		return new ResponseEntity<>("Accessible by anyone", HttpStatus.OK);
	}
	
	
	@GetMapping("/user")
	public ResponseEntity<String> userProtected() {
		LOG.info("Route '/user' reached");
		return new ResponseEntity<>("Accessible by users and admins ONLY", HttpStatus.OK);
	}
	
	
	@GetMapping("/admin")
	public ResponseEntity<String> adminProtected() {
		LOG.info("Route '/admin' reached");
		return new ResponseEntity<>("Accessible by admins ONLY.", HttpStatus.OK);
	}
	
}
