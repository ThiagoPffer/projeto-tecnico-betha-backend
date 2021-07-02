package com.thiagobetha.projeto_tecnico.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thiagobetha.projeto_tecnico.domain.ItemOrdemServico;
import com.thiagobetha.projeto_tecnico.repositories.ItemOrdemServicoRepository;

@Service
public class ItemOrdemServicoService {
	
	@Autowired
	private ItemOrdemServicoRepository repo;
	
	public List<ItemOrdemServico> findAll(){
		return repo.findAll();
	}
	
	public ItemOrdemServico findOne(Integer id) {
		Optional<ItemOrdemServico> obj = repo.findById(id);
		return obj.orElse(null);
	}
	
}
