package com.auth.config;

/**
 * Class containing utilities for using JWT
 */

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.auth.demo.DemoApplication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {
	
	private static final Logger LOG = LoggerFactory.getLogger(DemoApplication.class);
	
	private static final String SECRET = "your-secret-key";
    
	private static final int ONE_WEEK = (24*7)*60*60 * 1000;
	private static final int TWO_WEEKS = 2 * ONE_WEEK;
    
	public static final int EXPIRATION_TIME = TWO_WEEKS; // 10 days
	
    /**
     * Generates a new JWT token containing the username
     * @param username
     * @return
     */
    public static String generateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
        	.setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS512, SECRET)
            .compact();
    }
    
    
    /**
     * Extracts the username from the provided JWT token
     * @param token
     * @return
     */
    public static String extractUsername(String token) {
        return Jwts.parser()
            .setSigningKey(SECRET)
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }
    
    
    public boolean validate(String token) {
    	
    	// TODO -- how to validate token?
    	
    	
    	return true;
    }
    
    
    
    /**
     * Get all claims from a given jwt token
     * @param token
     * @return
     */
    public static Claims getClaims(String token) {
    	Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    	return claims;
    }
    
    
    /**
     * Get an id from a token
     * @param token
     * @return
     */
    public static String getIdFromToken(String token) {
    	Claims claims = getClaims(token);
    	return (String) claims.get("id");
    }
    
    
    /**
     * Get the role from a token
     * @param token
     * @return
     */
    public static String getRoleFromToken(String token) {
    	Claims claims = getClaims(token);
    	return (String) claims.get("role");
    }
	
}
