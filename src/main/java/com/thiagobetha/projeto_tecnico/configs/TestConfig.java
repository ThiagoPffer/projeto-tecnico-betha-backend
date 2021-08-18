package com.thiagobetha.projeto_tecnico.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.thiagobetha.projeto_tecnico.services.DBService;
import com.thiagobetha.projeto_tecnico.services.EmailService;
import com.thiagobetha.projeto_tecnico.services.MockEmailService;
import com.thiagobetha.projeto_tecnico.services.SmtpEmailService;

@Configuration
@Profile("test")
public class TestConfig {
	
	@Autowired
	private DBService dbService;
	
	@Bean
	public boolean instantiateDatabase() {
		dbService.instantiateTestDatabase();		
		return true;
	}
	/*
	@Bean
	public EmailService emailService() {
		return new MockEmailService();
	}
	*/
	@Bean
	public EmailService emailService() {
		return new SmtpEmailService();
	}
}
