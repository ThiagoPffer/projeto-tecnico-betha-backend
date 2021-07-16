package com.thiagobetha.projeto_tecnico.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.thiagobetha.projeto_tecnico.domain.Funcionario;
import com.thiagobetha.projeto_tecnico.repositories.FuncionarioRepository;
import com.thiagobetha.projeto_tecnico.services.exceptions.ObjectNotFoundException;

@Service
public class FuncionarioService {
	
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
		Optional<Funcionario> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Nenhum funcionario de id " + id + " foi encontrado!"));
	}
	
	@Transactional
	public Funcionario insert(Funcionario obj) {
		if(repo.findByEmail(obj.getEmail()) != null) {
			throw new IllegalArgumentException("Email já existente!");
		}
		obj.setId(null);
		return repo.save(obj);
	}
	
	@Transactional
	public Funcionario update(Funcionario newObj) {
		findOne(newObj.getId());
		if(repo.findByEmail(newObj.getEmail()) != null) {
			throw new IllegalArgumentException("Email já existente!");
		}
		return repo.save(newObj);
	}
	
	public void delete(Integer id) {
		findOne(id);
		repo.deleteById(id);
	}
}