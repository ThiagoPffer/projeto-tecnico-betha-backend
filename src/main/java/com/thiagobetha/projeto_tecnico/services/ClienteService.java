package com.thiagobetha.projeto_tecnico.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thiagobetha.projeto_tecnico.domain.Cliente;
import com.thiagobetha.projeto_tecnico.domain.Endereco;
import com.thiagobetha.projeto_tecnico.domain.OrdemServico;
import com.thiagobetha.projeto_tecnico.dto.ClientePostDTO;
import com.thiagobetha.projeto_tecnico.repositories.ClienteRepository;
import com.thiagobetha.projeto_tecnico.repositories.EnderecoRepository;
import com.thiagobetha.projeto_tecnico.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	@Autowired
	private EnderecoRepository enderecoRepo;
	
	public List<Cliente> findAll(){
		List<Cliente> list = repo.findAll();
		if(list.isEmpty()) {
			throw new ObjectNotFoundException("Nenhum Cliente do tipo " + 
					OrdemServico.class.getName() + " foi encontrado!");
		}
		return list;
	}
	
	public Cliente findOne(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Cliente de id " + id + " não encontrado!"));
	}
	
	public List<OrdemServico> findAllOrdensServico(Integer id){
		List<OrdemServico> list = findOne(id).getOrdensServico();
		if(list.isEmpty()) {
			throw new ObjectNotFoundException("Nenhum serviço para o cliente de id " + 
					id + " foi encontrado!");
		}
		return list;
	}
	
	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		return repo.save(obj);
	}
	
	public Cliente fromDTO(ClientePostDTO newObj) {
		Cliente obj = new Cliente(newObj.getNome(), newObj.getEmail(), newObj.getTelefone());
		Endereco endereco = new Endereco(newObj.getLogradouro(), newObj.getNumero(), 
				newObj.getBairro(), newObj.getCidade(), newObj.getEstado());
		obj.setEndereco(endereco);
		enderecoRepo.save(endereco);
		return obj;
	}
}