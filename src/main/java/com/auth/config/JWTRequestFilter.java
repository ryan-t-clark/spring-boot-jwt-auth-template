package com.auth.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component

public class JWTRequestFilter extends OncePerRequestFilter {

	private static final Logger LOG = LoggerFactory.getLogger(JWTRequestFilter.class);
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		
		final String authHeader = request.getHeader("Authorization"); //the token string
		
		//no header recieved, return
		if (authHeader == null || authHeader.isEmpty() || !authHeader.startsWith("Bearer ")) {
			chain.doFilter(request, response);
			return;
		}
		
		String token = authHeader.substring(7);
		
		//get the jwt token and validate it
		if (!jwtUtil.validate(token)) {
			chain.doFilter(request, response);
			return;
		}
		
		LOG.info("in do filter internal");
		LOG.info(authHeader);
		
		chain.doFilter(request, response);
		
	}
	
	
}
