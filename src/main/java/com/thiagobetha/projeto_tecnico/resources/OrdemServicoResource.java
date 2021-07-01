package com.thiagobetha.projeto_tecnico.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
	
}