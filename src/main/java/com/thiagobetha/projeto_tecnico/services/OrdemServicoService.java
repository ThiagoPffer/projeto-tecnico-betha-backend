package com.thiagobetha.projeto_tecnico.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thiagobetha.projeto_tecnico.domain.ItemOrdemServico;
import com.thiagobetha.projeto_tecnico.domain.OrdemServico;
import com.thiagobetha.projeto_tecnico.repositories.OrdemServicoRepository;

@Service
public class OrdemServicoService {
	
	@Autowired
	private OrdemServicoRepository repo;
	
	public List<OrdemServico> findAll(){
		return repo.findAll();
	}
	
	public List<ItemOrdemServico> findAllItems(Integer id){
		OrdemServico obj = findOne(id);
		return obj.getItens();
	}
	
	public OrdemServico findOne(Integer id) {
		Optional<OrdemServico> obj = repo.findById(id);
		return obj.orElse(null);
	}
	
	public ItemOrdemServico findOneItem(Integer id, Integer itemId){
		ItemOrdemServico item;
		List<ItemOrdemServico> list = findAllItems(id);
		for(ItemOrdemServico itemAux : list) {
			if(itemId == itemAux.getId()) {
				item = itemAux;
				return item;
			}
		}
		item = null;
		return item;
	}
	
	public OrdemServico insert(OrdemServico obj) {
		obj.setId(null);
		return repo.save(obj);
	}
	
	/*public List<ItemOrdemServico> insertItem(List<ItemOrdemServico> list) {
		obj.setId(null);
		return repo.save(obj);
	}
	POST DE ITENS
	*/ 
	
}
