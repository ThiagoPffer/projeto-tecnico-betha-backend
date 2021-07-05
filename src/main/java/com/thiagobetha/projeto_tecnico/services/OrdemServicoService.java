package com.thiagobetha.projeto_tecnico.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thiagobetha.projeto_tecnico.domain.ItemOrdemServico;
import com.thiagobetha.projeto_tecnico.domain.OrdemServico;
import com.thiagobetha.projeto_tecnico.repositories.OrdemServicoRepository;
import com.thiagobetha.projeto_tecnico.services.exceptions.ObjectNotFoundException;

@Service
public class OrdemServicoService {
	
	@Autowired
	private OrdemServicoRepository repo;
	
	public List<OrdemServico> findAll(){
		List<OrdemServico> list = repo.findAll();
		if(list.isEmpty()) {
			throw new ObjectNotFoundException("Nenhum serviço do tipo " + 
					OrdemServico.class.getName() + " foi encontrado!");
		}
		return list;
	}
	
	public List<ItemOrdemServico> findAllItems(Integer id){
		OrdemServico obj = findOne(id);
		List<ItemOrdemServico> list = obj.getItens();
		if(list.isEmpty()) {
			throw new ObjectNotFoundException("Nenhum item do tipo " + ItemOrdemServico.class.getName() + 
					" no serviço de id " + id + " foi encontrado!");
		}
		return list;
	}
	
	public OrdemServico findOne(Integer id) {
		Optional<OrdemServico> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Serviço de id " + id + " do tipo " + 
		OrdemServico.class.getName() + " não encontrado!"));
	}
	
	@SuppressWarnings("unused")
	public ItemOrdemServico findOneItem(Integer id, Integer itemId){
		ItemOrdemServico item = null;
		List<ItemOrdemServico> list = findAllItems(id);
		for(ItemOrdemServico itemAux : list) {
			if(itemId == itemAux.getId()) {
				item = itemAux;
				return item;
			}
		}
		if(item == null) {
			throw new ObjectNotFoundException("Nenhum item de id " + itemId + " do tipo " + ItemOrdemServico.class.getName() + 
					" no serviço de id " + id + " foi encontrado!");
		} else return item;
	}
	
	public OrdemServico insert(OrdemServico obj) {
		obj.setId(null);
		return repo.save(obj);
	}
	
	/* public ItemOrdemServico insertItem(Integer id, ItemOrdemServico obj) {
		List<ItemOrdemServico> list;
		OrdemServico os = findOne(id);
		list.add(obj);
		os.setItens(list);
		
		obj.setId(null);
		return repo.save(os);
	} */
	
}
