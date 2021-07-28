package com.thiagobetha.projeto_tecnico.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.thiagobetha.projeto_tecnico.domain.Funcionario;
import com.thiagobetha.projeto_tecnico.repositories.FuncionarioRepository;
import com.thiagobetha.projeto_tecnico.security.JWTUtil;
import com.thiagobetha.projeto_tecnico.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService {
	
	@Autowired
	private BCryptPasswordEncoder passEncoder;
	
	@Autowired
	private FuncionarioRepository funcionarioRepo;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private EmailService emailService;
	
	private Random random = new Random();
	
	public void sendNewPassword(String email) {
		Funcionario funcionario = funcionarioRepo.findByEmail(email);
		if(funcionario == null) {
			throw new ObjectNotFoundException("Funcionário não encontrado!");
		}
		
		String token = jwtUtil.generateToken(email);	
		emailService.sendPasswordRequestEmail(funcionario, token);
	}
	
	public void setNewPassword(String token) {
		if(!jwtUtil.tokenValido(token)) {
			throw new IllegalArgumentException("O token da requisição é inválido!");
		}
		String email = jwtUtil.getUsername(token);
		
		Funcionario funcionario = funcionarioRepo.findByEmail(email);
		if(funcionario == null) {
			throw new ObjectNotFoundException("Funcionário não encontrado!");
		}
		
		String newPass = newPassword();
		funcionario.setSenha(passEncoder.encode(newPass));;
		funcionarioRepo.save(funcionario);
		
		emailService.sendNewPasswordEmail(funcionario, newPass);
	}
	
	/*public void sendNewPassword(String email) {
		Funcionario funcionario = funcionarioRepo.findByEmail(email);
		
		if(funcionario == null) {
			throw new ObjectNotFoundException("Funcionário não encontrado!");
		}
		
		String newPass = newPassword();
		funcionario.setSenha(passEncoder.encode(newPass));;
		
		funcionarioRepo.save(funcionario);
		emailService.sendNewPasswordEmail(funcionario, newPass);
	}*/
	
	private String newPassword() {
		char[] password = new char[10];
		for(int i=0; i<10; i++) {
			password[i] = randomChar();
		}
		return new String(password);
	}
	
	private char randomChar() {
		int opt = random.nextInt(3);
		if (opt == 0) {
			return (char) (random.nextInt(10) + 48);
		}
		else if (opt == 1) {
			return (char) (random.nextInt(26) + 65);
		}
		else {
			return (char) (random.nextInt(26) + 97);
		}
	}
	
}