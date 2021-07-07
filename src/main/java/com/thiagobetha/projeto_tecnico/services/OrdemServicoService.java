package com.thiagobetha.projeto_tecnico.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thiagobetha.projeto_tecnico.domain.Cliente;
import com.thiagobetha.projeto_tecnico.domain.ItemOrdemServico;
import com.thiagobetha.projeto_tecnico.domain.OrdemServico;
import com.thiagobetha.projeto_tecnico.dto.OrdemServicoPostDTO;
import com.thiagobetha.projeto_tecnico.repositories.OrdemServicoRepository;
import com.thiagobetha.projeto_tecnico.services.exceptions.ObjectNotFoundException;

@Service
public class OrdemServicoService {
	
	@Autowired
	private OrdemServicoRepository repo;
	
	@Autowired
	private ClienteService clienteService;
	
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
			throw new ObjectNotFoundException("Nenhum item no serviço de id " + id + " foi encontrado!");
		}
		return list;
	}
	
	public OrdemServico findOne(Integer id) {
		Optional<OrdemServico> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Serviço de id " + id + " não encontrado!"));
	}
	
	public ItemOrdemServico findOneItem(Integer id, Integer itemId){
		List<ItemOrdemServico> list = findAllItems(id);
		for(ItemOrdemServico itemAux : list) {
			if(itemId == itemAux.getId()) {
				return itemAux;
			}
		}
    	throw new ObjectNotFoundException("Nenhum item de id " + itemId + 
					" no serviço de id " + id + " foi encontrado!");
	}
	
	@Transactional
	public OrdemServico insert(OrdemServicoPostDTO newObj) {
		Cliente cliente = clienteService.findOne(newObj.getIdCliente());
		OrdemServico obj = new OrdemServico(cliente);
		
		obj.setId(null);
		return repo.save(obj);
	}
	
	public OrdemServico insertItems(Integer id, List<ItemOrdemServico> list) {
		OrdemServico obj = findOne(id);
		
		for(ItemOrdemServico item : list) {
			item.setOrdemServico(obj);
			obj.setValorTotal(item.getOrcamento());
		}
		
		obj.setItens(list);
		
		return repo.save(obj);
	} 
	
}
