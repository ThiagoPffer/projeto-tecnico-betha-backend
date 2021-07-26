package com.thiagobetha.projeto_tecnico.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.thiagobetha.projeto_tecnico.security.UserSS;

public class UserService {
	
	public static UserSS authenticated(){
		try {
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return null;
		}
	}
	
}