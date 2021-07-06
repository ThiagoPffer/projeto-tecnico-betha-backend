package com.thiagobetha.projeto_tecnico.resources;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.thiagobetha.projeto_tecnico.domain.ItemOrdemServico;
import com.thiagobetha.projeto_tecnico.domain.OrdemServico;
import com.thiagobetha.projeto_tecnico.services.OrdemServicoService;

@RestController
@RequestMapping(value="/ordensservico")
public class OrdemServicoResource {
	
	@Autowired
	private OrdemServicoService service;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<OrdemServico>> listAll() {	
		List<OrdemServico> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<OrdemServico> listOne(@PathVariable Integer id) {
		OrdemServico obj = service.findOne(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@RequestMapping(value = "/{id}/itens", method = RequestMethod.GET)
	public ResponseEntity<List<ItemOrdemServico>> listAllItems(@PathVariable Integer id) {	
		List<ItemOrdemServico> list = service.findAllItems(id);
		return ResponseEntity.ok().body(list);
	}
	
	@RequestMapping(value = "/{id}/itens/{itemId}", method = RequestMethod.GET)
	public ResponseEntity<ItemOrdemServico> listOneItem(@PathVariable Integer id, @PathVariable Integer itemId) {	
		ItemOrdemServico obj = service.findOneItem(id, itemId);
		return ResponseEntity.ok().body(obj);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@RequestBody OrdemServico obj) {
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@RequestMapping(value = "/{id}/itens", method = RequestMethod.POST)
	public ResponseEntity<Void> insertItens(@PathVariable Integer id, @RequestBody List<ItemOrdemServico> list) {
		OrdemServico obj = service.insertItems(id, list);
				
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
}