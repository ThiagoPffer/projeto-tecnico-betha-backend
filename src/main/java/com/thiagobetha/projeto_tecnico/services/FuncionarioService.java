package com.thiagobetha.projeto_tecnico.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.thiagobetha.projeto_tecnico.domain.Funcionario;
import com.thiagobetha.projeto_tecnico.domain.enums.TipoFuncionario;
import com.thiagobetha.projeto_tecnico.dto.FuncionarioDTO;
import com.thiagobetha.projeto_tecnico.repositories.FuncionarioRepository;
import com.thiagobetha.projeto_tecnico.security.UserSS;
import com.thiagobetha.projeto_tecnico.services.exceptions.DataIntegrityException;
import com.thiagobetha.projeto_tecnico.services.exceptions.ObjectNotFoundException;

@Service
public class FuncionarioService {
	
	@Autowired
	private BCryptPasswordEncoder passEncoder;
	
	@Autowired
	private FuncionarioRepository repo;
	
	public List<Funcionario> findAll(){
		List<Funcionario> list = repo.findAll();
		if(list.isEmpty()) {
			throw new ObjectNotFoundException("Nenhum Funcionario foi encontrado!");
		}
		return list;
	}
	
	public Page<Funcionario> findPage(Integer page, Integer linesPerPage, String direction, String orderBy){
		PageRequest pageReq = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		if(repo.findAll(pageReq).getContent().isEmpty()) {
			throw new ObjectNotFoundException("Nenhum Funcionario foi encontrado!");
		}
		return repo.findAll(pageReq);
	}
	
	public Funcionario findOne(Integer id) {
		
		UserSS user = UserService.authenticated();
		if(user==null || !user.hasRole(TipoFuncionario.ADMINISTRADOR) && !id.equals(user.getId())) {
			throw new AccessDeniedException("Acesso negado!");
		}
		
		Optional<Funcionario> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Nenhum funcionario de id " + id + " foi encontrado!"));
	}
	
	public FuncionarioDTO findByEmail(String email) {
		UserSS user = UserService.authenticated();
		if(user==null || !user.hasRole(TipoFuncionario.ADMINISTRADOR) && !email.equals(user.getUsername())) {
			throw new AccessDeniedException("Acesso negado!");
		}
		
		Funcionario obj = repo.findByEmail(email);
		if(obj.equals(null)) {
			throw new ObjectNotFoundException("Funcionario com email "+email+" não encontrado!");
		}
		FuncionarioDTO objDTO = new FuncionarioDTO(obj);
		return objDTO;
	}
	
	@Transactional
	public Funcionario insert(Funcionario obj) {
		if(repo.findByEmail(obj.getEmail()) != null) {
			throw new IllegalArgumentException("Email já existente!");
		}
		obj.setId(null);
		obj.setSenha(passEncoder.encode(obj.getSenha()));
		return repo.save(obj);
	}
	
	@Transactional
	public FuncionarioDTO update(FuncionarioDTO newObj) {
		Funcionario obj = findOne(newObj.getId());
		Funcionario auxObj = repo.findByEmail(newObj.getEmail());
		if(auxObj != null && auxObj.getId() != newObj.getId()) {
			throw new IllegalArgumentException("Email já existente!");
		}
		obj.setEmail(newObj.getEmail());
		obj.setNome(newObj.getNome());
		obj.setTipo(newObj.getTipo());
		repo.save(obj);
		return newObj;
	}
	
	public void delete(Integer id) {
		findOne(id);
		if(UserService.authenticated().getId() == id) {
			throw new DataIntegrityException("Não é possível deletar sua própria conta!");
		}
		repo.deleteById(id);
	}
}