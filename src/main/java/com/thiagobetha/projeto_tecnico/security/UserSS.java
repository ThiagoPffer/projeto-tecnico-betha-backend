package com.thiagobetha.projeto_tecnico.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.thiagobetha.projeto_tecnico.domain.enums.TipoFuncionario;

public class UserSS implements UserDetails{
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String email;
	private String senha;
	private TipoFuncionario tipo;
	
	public UserSS() {}
	
	public UserSS(Integer id, String email, String senha, TipoFuncionario tipo) {
		super();
		this.id = id;
		this.email = email;
		this.senha = senha;
		this.tipo = tipo;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<TipoFuncionario> tipoSet = new HashSet<>();
		tipoSet.add(tipo);
		
		Collection<? extends GrantedAuthority> authorities = 
				tipoSet.stream()
				.map(item -> new SimpleGrantedAuthority(item.getDesc()))
				.collect(Collectors.toList());
		
		return authorities;
	}

	public Integer getId(){
		return id;
	}
	
	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
