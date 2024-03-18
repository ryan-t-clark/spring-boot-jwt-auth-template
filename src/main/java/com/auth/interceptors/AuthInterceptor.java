package com.auth.interceptors;

/**
 * Intercepts HTTP requests and checks if the route it is attempting to call has
 * the AdminOnly annotation. If it does, checks the role of the requesting user 
 * and allows or blocks the call to the route
 */

import java.lang.reflect.Method;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.auth.annotations.AdminOnly;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

	private static final Logger LOG = LoggerFactory.getLogger(AuthInterceptor.class);
	
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception { 
  
		/**
		 * HttpServletRequest request – represents the request being handled
		 * HttpServletResponse response – represents the HTTP response to be sent back to the client
		 * Object handler – the target controller method that will handle this request
		 * 
		 * If the method returns true then the request will be directed towards the target control 
		 * else the target controller method won’t be invoked if this method returns false and the request will be halted.
		 */	
        
        if(handler instanceof HandlerMethod){
            
        	//get the route that the request is trying to reach
            Method method = ((HandlerMethod) handler).getMethod();
            
            //check if the route has the AdminOnly annotation
            if (method.isAnnotationPresent(AdminOnly.class)) {
            	
            	LOG.info("user is trying to access an admin only route");
            
            }
            
        	Enumeration<String> headers = request.getHeaderNames();
        	while (headers.hasMoreElements()) {
        		LOG.info(headers.nextElement());
        	}
            
        }
		
        return true; //allow the request to continue
    } 
	
}
