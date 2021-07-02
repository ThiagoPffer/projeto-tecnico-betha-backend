package com.thiagobetha.projeto_tecnico.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.thiagobetha.projeto_tecnico.domain.ItemOrdemServico;
import com.thiagobetha.projeto_tecnico.services.ItemOrdemServicoService;

@RestController
@RequestMapping(value="/ordensservico/{id}/itens")
public class ItemOrdemServicoResource {
	
	@Autowired
	private ItemOrdemServicoService service;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<ItemOrdemServico>> listAll() {	
		List<ItemOrdemServico> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public ResponseEntity<ItemOrdemServico> listOne(@PathVariable Integer id) {	
		ItemOrdemServico obj = service.findOne(id);
		return ResponseEntity.ok().body(obj);
	}
	
}