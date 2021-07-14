package com.thiagobetha.projeto_tecnico.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.thiagobetha.projeto_tecnico.domain.Cliente;
import com.thiagobetha.projeto_tecnico.domain.Endereco;
import com.thiagobetha.projeto_tecnico.dto.ClientePostDTO;
import com.thiagobetha.projeto_tecnico.repositories.ClienteRepository;
import com.thiagobetha.projeto_tecnico.services.exceptions.DataIntegrityException;
import com.thiagobetha.projeto_tecnico.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;

	public List<Cliente> findAll(){
		List<Cliente> list = repo.findAll();
		if(list.isEmpty()) {
			throw new ObjectNotFoundException("Nenhum Cliente foi encontrado!");
		}
		return list;
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String direction, String orderBy){
		PageRequest pageReq = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		if(repo.findAll(pageReq).getContent().isEmpty()) {
			throw new ObjectNotFoundException("Nenhum Cliente foi encontrado!");
		}
		return repo.findAll(pageReq);
	}
	
	public Cliente findOne(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Cliente de id " + id + " não encontrado!"));
	}
	
	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		//LANÇAR ERRO DE VALIDAÇÃO CASO ALGUM ITEM ESTEJA ERRADO OU NULO
		return repo.save(obj);
	}
	
	@Transactional
	public Cliente update(Cliente newObj) {
		/*
		 * A operação abaixo serve para buscar o id do endereço que já está
		 * salvo no banco e dar um setid no endereço do novo objeto que está sendo
		 * enviado. Isso é necessário para evitar a criação de um novo endereço com o
		 * mesmo cliente na tabela Enderecos.
		 */
		newObj.getEnderecoObj().setId(findOne(newObj.getId()).getEnderecoObj().getId());
		//LANÇAR ERRO DE VALIDAÇÃO CASO ALGUM ITEM ESTEJA ERRADO OU NULO
		return repo.save(newObj);
	}
	
	public void delete(Integer id) {
		findOne(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir um cliente com ordens de serviço já cadastradas!");
		}
	}
	
	public Cliente fromDTO(ClientePostDTO newObj) {
		Cliente obj = new Cliente(newObj.getNome(), newObj.getEmail(), newObj.getTelefone());
		Endereco endereco = new Endereco(obj, newObj.getLogradouro(), newObj.getNumero(), 
				newObj.getBairro(), newObj.getCidade(), newObj.getEstado());
		obj.setEndereco(endereco);
		return obj;
	}
}