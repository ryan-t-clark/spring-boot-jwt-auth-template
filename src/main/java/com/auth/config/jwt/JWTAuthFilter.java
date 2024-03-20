package com.auth.config.jwt;

/**
 * https://medium.com/@minadev/authentication-and-authorization-with-spring-security-bf22e985f2cb
 */

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

	private final JWTUserDetailsService userDetailsService;
	private final ObjectMapper objectMapper;
	
	public JWTAuthFilter(JWTUserDetailsService userDetailsService, ObjectMapper objectMapper) {
		this.userDetailsService = userDetailsService;
		this.objectMapper = objectMapper;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		      throws ServletException, IOException {
		
		try {
			
			String authHeader = request.getHeader("Authorization");
			String token = null;
			String username = null;
			
			
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				token = authHeader.substring(7);
				username = JWTUtil.getUsernameFromToken(token);
			}
			
			//if no token is present, move to next filter in the chain
			if (token == null) {
				filterChain.doFilter(request, response);
				return;
			}
			
			//if there is a token, validate it and authenticate it in the security context
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
		        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		        
		        //
		        // TODO -- pass user details here
		        //
		        if (JWTUtil.validateToken(token)) {
		          UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, null);
		          authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		        }
			}
			
			filterChain.doFilter(request, response);
			
		} catch (AccessDeniedException e) {
			
		      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		      response.getWriter().write("Access Denied");
			
		}
	
	}
	
}
