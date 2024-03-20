/**
 * Auth Controller
 * @Author Ryan Clark
 * @Date February 2024
 * 
 * Controller that handles the creation of users and distribution of JWT tokens
 */

package com.auth.controllers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth.config.jwt.JWTUtil;
import com.auth.model.LoginRequest;
import com.auth.model.LoginResponse;
import com.auth.model.User;

import com.auth.repo.UserRepo;


@RestController
@CrossOrigin(origins = "http://localhost:80")
public class AuthController {
	
	private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);
	
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody User user) {
		
		LOG.info("Route '/signup' reached");
		
		//validate input
		if (user.getUsername() == null || user.getPassword() == null) {
			return new ResponseEntity<String>("Error: must provide username and password", HttpStatus.BAD_REQUEST);
		}
		
		try {
			//create a user
			return UserRepo.instance().createUser(user);
		} catch (Exception e) {
			return new ResponseEntity<String>("Something went wrong. Try again.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	/**
	 * Route to log in a user
	 * 
	 * @param request
	 * @return a token with its expiration time, or an error message
	 */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    	
    	LOG.info("Route '/login' reached");
    	
    	//check nulls
    	if (request == null || request.getUsername() == null || request.getPassword() == null) {
    		return new ResponseEntity<String>("Error: Must provide a username and password", HttpStatus.BAD_REQUEST);
    	}
    	
    	LOG.info("Attempting to log in user [" + request.getUsername() + "]");
    	
    	//attempt to authenticate the user
    	User user = UserRepo.instance().authenticateUser(request);
    	
    	//if user was authenticated
    	if (user != null) {
    		//create the info to be stored within the jwt token
    		Map<String, Object> claims = new HashMap<String, Object>();
    		
    		claims.put("id", 	user.getId());
    		claims.put("role", 	user.getRole());
    		
    		
    		//generate the token
    		final String token = JWTUtil.generateToken(claims, request.getUsername());
    		
    		LoginResponse response = new LoginResponse(token, Integer.toString(JWTUtil.EXPIRATION_TIME));
    		
    		LOG.info("Successful login for user [" + request.getUsername() + "]");
    		
    		return new ResponseEntity<LoginResponse>(response, HttpStatus.OK);
    		
    	} else {
    		LOG.info("Failed to authenticate user [" + request.getUsername() + "]: Username or password is incorrect");
    		return new ResponseEntity<String>("Error: Username or password is incorrect", HttpStatus.UNAUTHORIZED);
    	}
    	

    }
    
}
