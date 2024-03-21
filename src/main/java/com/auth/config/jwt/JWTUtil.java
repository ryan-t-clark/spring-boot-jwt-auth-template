package com.auth.config.jwt;

/**
 * Class containing utilities for using JWT
 */

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

@Component
public class JWTUtil {
	
	private static final Logger LOG = LoggerFactory.getLogger(JWTUtil.class);
	
	@Value("${jwt.secret}")
	private String SECRET_KEY;
    
	@Value("${jwt.expiration}")
	public int EXPIRATION_TIME;
	
    /**
     * Generates a new JWT token containing the username
     * @param username
     * @return
     */
    public String generateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
        	.setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
            .compact();
    }
        
    
    /**
     * Get all claims from a given jwt token
     * @param token
     * @return
     */
    public Claims getClaims(String token) throws SignatureException, ExpiredJwtException {
    	//this call verifies the signature of the token based on the secret key
    	//tampered with tokens would be detected here
    	Claims claims = Jwts.parser()
    			.setSigningKey(SECRET_KEY)
    			.parseClaimsJws(token)
    			.getBody();
    	return claims;
    }
    
    
    /**
     * Validates a given token
     * @param token
     * @return
     */
    public boolean validateToken(String token, UserDetails userDetails) throws SignatureException, ExpiredJwtException {
    	final String username = getUsernameFromToken(token); 
    	return !isTokenExpired(token) && username.equals(userDetails.getUsername()); 
    }
    
    
    /**
     * Check if a token is expired
     * @param token
     * @return
     */
    public boolean isTokenExpired(String token) throws SignatureException, ExpiredJwtException{
    	Claims claims = getClaims(token);
    	return claims.getExpiration().before(new Date());
    }
    
    
    /**
     * Gets the username from a token
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token) throws SignatureException, ExpiredJwtException {
    	Claims claims = getClaims(token);
    	return (String) claims.getSubject();
    }
    
    
    /**
     * Get an id from a token
     * @param token
     * @return
     */
    public String getIdFromToken(String token) throws SignatureException, ExpiredJwtException {
    	Claims claims = getClaims(token);
    	return (String) claims.get("id");
    }
    
    
    /**
     * Get the role from a token
     * @param token
     * @return
     */
    public String getRoleFromToken(String token) throws SignatureException, ExpiredJwtException {
    	Claims claims = getClaims(token);
    	return (String) claims.get("role");
    }
	
}
