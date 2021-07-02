package com.thiagobetha.projeto_tecnico.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thiagobetha.projeto_tecnico.domain.OrdemServico;
import com.thiagobetha.projeto_tecnico.repositories.OrdemServicoRepository;

@Service
public class OrdemServicoService {
	
	@Autowired
	private OrdemServicoRepository repo;
	
	public List<OrdemServico> findAll(){
		return repo.findAll();
	}
	
	public OrdemServico findOne(Integer id) {
		Optional<OrdemServico> obj = repo.findById(id);
		return obj.orElse(null);
	}
	
	public OrdemServico insert(OrdemServico obj) {
		obj.setId(null); //?????
		return repo.save(obj);
	}
	
}
