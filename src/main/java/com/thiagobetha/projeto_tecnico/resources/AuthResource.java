package com.thiagobetha.projeto_tecnico.resources;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thiagobetha.projeto_tecnico.dto.EmailDTO;
import com.thiagobetha.projeto_tecnico.security.JWTUtil;
import com.thiagobetha.projeto_tecnico.security.UserSS;
import com.thiagobetha.projeto_tecnico.services.AuthService;
import com.thiagobetha.projeto_tecnico.services.UserService;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private AuthService service;
	
	@RequestMapping(value = "/verify-token", method = RequestMethod.POST)
	public ResponseEntity<Void> verifyToken(HttpServletResponse response) {
		UserSS user = UserService.authenticated();
		if(user.equals(null)) {
			throw new AccessDeniedException("A sessão do usuário expirou");
		}
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value = "/refresh-token", method = RequestMethod.POST)
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		UserSS user = UserService.authenticated();
		String token = jwtUtil.generateToken(user.getUsername());
		response.addHeader("Authorization", "Bearer " + token);
		response.addHeader("access-control-expose-headers", "Authorization");
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value = "/forgot", method = RequestMethod.POST)
	public ResponseEntity<Void> forgotPass(@Valid @RequestBody EmailDTO email) {
		service.sendNewPassword(email.getEmail());
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value="/newpassword", method=RequestMethod.POST)
	public ResponseEntity<Void> updatePass(@Valid @RequestParam(value="token") String token){
		service.setNewPassword(token);
		return ResponseEntity.noContent().build();
	}
}
