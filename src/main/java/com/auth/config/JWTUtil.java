package com.auth.config;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth.demo.DemoApplication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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
	
}
