package com.auth.model;

public class LoginResponse {

	private String token;
	private String expiration;
	
	public LoginResponse(String token, String expiration) {
		this.token = token;
		this.expiration = expiration;
	}
	
	public String getToken() {
		return this.token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getExpiration() {
		return this.expiration;
	}
	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}
	
}
