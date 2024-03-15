package com.auth.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@SpringBootApplication
@ComponentScan("com.auth")
public class DemoApplication {

	private static final Logger LOG = LoggerFactory.getLogger(DemoApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
	@Bean
    ApplicationRunner applicationRunner(Environment environment) 
	{
        return args -> 
        {
        	//initialize parts of application here
        	//=============================================

        	
        	
            
        	LOG.info("Application successfully launched.");
        	//=============================================
        };
    }

}
