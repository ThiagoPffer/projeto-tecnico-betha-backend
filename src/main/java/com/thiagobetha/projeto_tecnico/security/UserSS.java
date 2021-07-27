package com.thiagobetha.projeto_tecnico.security;

import java.util.Collection;
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
	private Collection<? extends GrantedAuthority> authorities;
	
	public UserSS() {}
	
	public UserSS(Integer id, String email, String senha, Collection<TipoFuncionario> tipo) {
		super();
		this.id = id;
		this.email = email;
		this.senha = senha;
		this.authorities = tipo.stream().map(item -> new SimpleGrantedAuthority(item.getDesc())).collect(Collectors.toList());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
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
	
	public boolean hasRole(TipoFuncionario tipo) {
		System.out.println(getAuthorities());
		return getAuthorities().contains(new SimpleGrantedAuthority(tipo.getDesc()));
	}

}
