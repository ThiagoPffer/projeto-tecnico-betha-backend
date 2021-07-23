package com.thiagobetha.projeto_tecnico.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.thiagobetha.projeto_tecnico.domain.Funcionario;
import com.thiagobetha.projeto_tecnico.repositories.FuncionarioRepository;
import com.thiagobetha.projeto_tecnico.security.UserSS;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService{

	@Autowired
	private FuncionarioRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Funcionario funcionario = repo.findByEmail(email);
		if(funcionario == null) {
			throw new UsernameNotFoundException(email);
		}
		return new UserSS(funcionario.getId(), funcionario.getEmail(), funcionario.getSenha(), funcionario.getTipo());
	}

}
