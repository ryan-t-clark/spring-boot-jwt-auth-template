package com.auth.config.jwt;

/**
 * https://medium.com/@minadev/authentication-and-authorization-with-spring-security-bf22e985f2cb
 */

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

	private static final Logger LOG = LoggerFactory.getLogger(JWTAuthFilter.class);
	
	@Autowired
	private JWTUtil jwtUtil;
	
	private final JWTUserDetailsService userDetailsService;
	
	public JWTAuthFilter(JWTUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
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
				
				try {
					username = jwtUtil.getUsernameFromToken(token);
				} catch (SignatureException e) {
					LOG.error("Error: Bad token signature. Access denied.");
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				    response.getWriter().write("Access Denied");
					return;
				} catch (ExpiredJwtException e) {
					LOG.error("Error: Token is expired.");
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				    response.getWriter().write("Access Denied");
					return;
				}
				
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
		        if (jwtUtil.validateToken(token, userDetails)) {
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
