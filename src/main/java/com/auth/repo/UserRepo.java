package com.auth.repo;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.auth.model.LoginRequest;
import com.auth.model.SignupRequest;
import com.auth.model.User;

public class UserRepo {

	private static final Logger LOG = LoggerFactory.getLogger(UserRepo.class);

	public static final UserRepo instance = new UserRepo();

	private UserRepo() {};

	public static UserRepo instance()
	{
	    return instance;
	}
	
	
	/**
	 * Creates a user
	 * 
	 * @param user
	 * @return
	 */
	public ResponseEntity<?> createUser(SignupRequest signupRequest) {
		
		LOG.info("Creating user [" + signupRequest.getUsername() + "]");
		
		// hash the password
        String hashedPassword = new BCryptPasswordEncoder().encode(signupRequest.getPassword());
		
        User newUser = new User(
        	UUID.randomUUID().toString(),
        	signupRequest.getUsername(),
        	hashedPassword,
        	"ROLE_USER"
        );
        		
        //
        // database insertion logic goes here -- delete before use
        //
        
        // just for testing -- wouldn't want to return this data to the user
        return new ResponseEntity<User>(newUser, HttpStatus.OK);
	}
	
	
	/**
	 * Authenticates a user attempting to log in
	 * 
	 * @param request
	 * @return
	 */
	public User authenticateUser(LoginRequest request) {
		
		LOG.info("Attempting to authenticate user [" + request.getUsername() + "]");
		
		//
		// make database call here to retrieve user by username
		//
		
		// to be deleted -- just an example of a user
		User sampleUser = new User();
		sampleUser.setId("1");
		sampleUser.setPassword("$2a$10$QYu0v5Omk.xIVAxGlTzW.eCxhglVkpMItvLTdYgY0cy.xFfHZAV6."); //hashed version of password "password"
		sampleUser.setUsername("user");
		sampleUser.setRole("ROLE_USER");
		
		// to be deleted -- just an example of an admin
		User sampleAdmin = new User();
		sampleAdmin.setId("2");
		sampleAdmin.setPassword("$2a$10$QYu0v5Omk.xIVAxGlTzW.eCxhglVkpMItvLTdYgY0cy.xFfHZAV6.");
		sampleAdmin.setUsername("admin");
		sampleAdmin.setRole("ROLE_ADMIN");
		
		//to be deleted -- just an example of logging into a user or admin
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if (request.getUsername().equals("user") && encoder.matches(request.getPassword(), sampleUser.getPassword())) {
			return sampleUser;
		} else if (request.getUsername().equals("admin") && encoder.matches(request.getPassword(), sampleAdmin.getPassword())) {
			return sampleAdmin;
		} else {
			return null;
		}
		
		
		/*
		Actual usage:
		User would be retrieved from the database, and that retrieved hashed password would be compared against
		the request password. If they match, return the User object, if they don't return null
		 
		String hashedPassword = ""; //retrieve from database
		
		if (new BCryptPasswordEncoder().matches(request.getPassword(), hashedPassword)) {
			return user; //user retrieved from database
		} 
		else {
			return null;
		}
		*/
		
	}
	
	
}
