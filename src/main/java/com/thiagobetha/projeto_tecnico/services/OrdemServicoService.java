package com.thiagobetha.projeto_tecnico.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thiagobetha.projeto_tecnico.domain.Cliente;
import com.thiagobetha.projeto_tecnico.domain.ItemOrdemServico;
import com.thiagobetha.projeto_tecnico.domain.OrdemServico;
import com.thiagobetha.projeto_tecnico.dto.OrdemServicoDTO;
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
	
	public OrdemServico findOne(Integer id) {
		Optional<OrdemServico> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Serviço de id " + id + " não encontrado!"));
	}
	
	@Transactional
	public OrdemServico insert(OrdemServico obj) {
		obj.setId(null);
		atualizarValorTotal(obj);
		return repo.save(obj);
	}
	
	@Transactional //CÓDIGO IMCOMPLETO: REMOÇÃO DE ITENS NÃO FUNCIONANDO (PROBLEMA 8)
	public OrdemServico update(OrdemServico newObj) {
		Boolean mustRemove = true;
		OrdemServico obj = findOne(newObj.getId());
		obj.setCliente(newObj.getCliente());
		
		/*if(obj.getItens().size() > newObj.getItens().size()) {
		}*/
		
		for(ItemOrdemServico item : obj.getItens()) {
			for(ItemOrdemServico novoItem : newObj.getItens()) {
				if(novoItem.getId() == item.getId()) {
					mustRemove = false;
					item = novoItem;
				}
			}
			if(mustRemove == true) {
				obj.getItens().remove(item);
			}
		}
		
		atualizarValorTotal(newObj);
		return repo.save(newObj);
	}

	public void delete(Integer id) {
		findOne(id);
		repo.deleteById(id);
	}
	
	public OrdemServico fromDTO(OrdemServicoDTO newObj) {
		Cliente cliente = clienteService.findOne(newObj.getIdCliente());
		OrdemServico os = new OrdemServico(cliente);
		os.setItens(newObj.getItens());
		
		for(ItemOrdemServico i : newObj.getItens()) {
			i.setOrdemServico(os);
		}
		
		return os;
	}
	
	private void atualizarValorTotal(OrdemServico obj) {
		List<ItemOrdemServico> list = obj.getItens();
		obj.setValorTotal(BigDecimal.ZERO);
		for(ItemOrdemServico item : list) {
			obj.setValorTotal(item.getOrcamento());
		}
	}
	
}
