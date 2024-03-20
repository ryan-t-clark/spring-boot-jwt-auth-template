package com.auth.config.jwt;

/**
 * Class containing utilities for using JWT
 */

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

@Component
public class JWTUtil {
	
	private static final Logger LOG = LoggerFactory.getLogger(JWTUtil.class);
	
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
     * Get all claims from a given jwt token
     * @param token
     * @return
     */
    public static Claims getClaims(String token) throws SignatureException, ExpiredJwtException {
    	//this call verifies the signature of the token based on the secret key
    	//tampered with tokens would be detected here
    	Claims claims = Jwts.parser()
    			.setSigningKey(SECRET)
    			.parseClaimsJws(token)
    			.getBody();
    	return claims;
    }
    
    
    /**
     * Validates a given token
     * @param token
     * @return
     */
    public static boolean validateToken(String token) {
    	final String username = getUsernameFromToken(token); 
    	
    	//
    	// TODO -- also need to do verification that this username is valid? 
    	//
    	if (!isTokenExpired(token)) {
    		//valid token
    		return true;
    	} else {
    		//invalid token
    		return false;
    	}
    		
    }
    
    
    /**
     * Check if a token is expired
     * @param token
     * @return
     */
    public static boolean isTokenExpired(String token) {
    	Claims claims = getClaims(token);
    	return claims.getExpiration().before(new Date());
    }
    
    
    /**
     * Gets the username from a token
     * @param token
     * @return
     */
    public static String getUsernameFromToken(String token) {
    	Claims claims = getClaims(token);
    	return (String) claims.getSubject();
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
