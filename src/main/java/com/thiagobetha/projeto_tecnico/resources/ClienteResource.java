package com.thiagobetha.projeto_tecnico.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.thiagobetha.projeto_tecnico.domain.Cliente;
import com.thiagobetha.projeto_tecnico.services.ClienteService;

@RestController
@RequestMapping(value="/clientes")
public class ClienteResource {
	
	@Autowired
	private ClienteService service;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Cliente>> listAll() {	
		List<Cliente> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Cliente> getOne(@PathVariable Integer id) {
		Cliente obj = service.findOne(id);
		return ResponseEntity.ok().body(obj);
	}
	
}