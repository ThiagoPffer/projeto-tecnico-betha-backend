package com.thiagobetha.projeto_tecnico.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.thiagobetha.projeto_tecnico.domain.OrdemServico;
import com.thiagobetha.projeto_tecnico.dto.OrdemServicoDTO;
import com.thiagobetha.projeto_tecnico.dto.OrdemServicoListDTO;
import com.thiagobetha.projeto_tecnico.services.OrdemServicoService;

@RestController
@RequestMapping(value="/ordensservico")
public class OrdemServicoResource {
	
	@Autowired
	private OrdemServicoService service;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<OrdemServicoListDTO>> listAll() {	
		List<OrdemServico> list = service.findAll();
		List<OrdemServicoListDTO> listDTO = list.stream().map(obj -> new OrdemServicoListDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDTO);
	}
	
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ResponseEntity<Page<OrdemServicoListDTO>> listPage(
			@RequestParam(value="page", defaultValue = "0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue = "24") Integer linesPerPage, 
			@RequestParam(value="direction", defaultValue = "ASC") String direction, 
			@RequestParam(value="orderBy", defaultValue = "cliente") String orderBy) {	
		Page<OrdemServico> list = service.findPage(page, linesPerPage, direction, orderBy);
		Page<OrdemServicoListDTO> listDTO = list.map(obj -> new OrdemServicoListDTO(obj));
		return ResponseEntity.ok().body(listDTO);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<OrdemServico> getOne(@PathVariable Integer id) {
		OrdemServico obj = service.findOne(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@RequestBody OrdemServicoDTO newObj) {
		OrdemServico obj = service.fromDTO(newObj);
		obj = service.insert(obj);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Void> update(@RequestBody OrdemServicoDTO objDto, @PathVariable Integer id){
		OrdemServico obj = service.fromDTO(objDto);
		obj.setId(id);
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
}