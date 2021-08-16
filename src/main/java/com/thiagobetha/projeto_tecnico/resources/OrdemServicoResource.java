package com.thiagobetha.projeto_tecnico.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.thiagobetha.projeto_tecnico.domain.OrdemServico;
import com.thiagobetha.projeto_tecnico.domain.enums.EstadoPagamento;
import com.thiagobetha.projeto_tecnico.domain.enums.SituacaoOrdemServico;
import com.thiagobetha.projeto_tecnico.dto.OrdemServicoDTO;
import com.thiagobetha.projeto_tecnico.dto.OrdemServicoListDTO;
import com.thiagobetha.projeto_tecnico.services.OrdemServicoService;

@RestController
@RequestMapping(value="/ordensservico")
public class OrdemServicoResource {
	
	@Autowired
	private OrdemServicoService service;

	@PreAuthorize("hasAnyRole('TECNICO','RECEPCIONISTA', 'ADMIN')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<OrdemServicoListDTO>> listAll() {	
		List<OrdemServico> list = service.findAll();
		List<OrdemServicoListDTO> listDTO = list.stream().map(obj -> new OrdemServicoListDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDTO);
	}
	
	@PreAuthorize("hasAnyRole('TECNICO','RECEPCIONISTA', 'ADMIN')")
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
	
	@PreAuthorize("hasAnyRole('TECNICO','RECEPCIONISTA', 'ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<OrdemServico> getOne(@PathVariable Integer id) {
		OrdemServico obj = service.findOne(id);
		return ResponseEntity.ok().body(obj);
	}

	@PreAuthorize("hasAnyRole('RECEPCIONISTA', 'ADMIN')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody OrdemServicoDTO newObj){
		OrdemServico obj = service.fromDTO(newObj);
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	//upload de fotos
	@PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
	@RequestMapping(value = "/{id}/itens/{idItem}/fotos", method = RequestMethod.POST)
	public ResponseEntity<Void> insertPicture(
			@PathVariable Integer id, 
			@PathVariable Integer idItem, 
			@RequestParam(name = "file") MultipartFile file){
		URI uri = service.uploadItensPictures(id, idItem, file);
		return ResponseEntity.created(uri).build();
	}
	
	//PUT para realizar alterações na ordem de serviço: ID do cliente e itens.
	@PreAuthorize("hasAnyRole('RECEPCIONISTA', 'TECNICO', 'ADMIN')")
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody OrdemServicoDTO objDto, @PathVariable Integer id){
		OrdemServico obj = service.fromDTO(objDto);
		obj.setId(id);
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
	@RequestMapping(value="/{id}/situacoes", method=RequestMethod.PUT)
	public ResponseEntity<Void> updateSituacao(
			@Valid @RequestParam(value="value") SituacaoOrdemServico situacao, 
			@PathVariable Integer id){
		service.updateSituacao(situacao, id);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('RECEPCIONISTA', 'ADMIN')")
	@RequestMapping(value="/{id}/pagamentos", method=RequestMethod.PUT)
	public ResponseEntity<Void> updateEstadoPagamento(
			@Valid @RequestParam(value="value") EstadoPagamento estado, 
			@PathVariable Integer id){
		service.updateEstadoPagamento(estado, id);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('RECEPCIONISTA', 'ADMIN')")
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
	@RequestMapping(value="/{id}/itens/{idItem}/fotos/{idImagem}", method=RequestMethod.DELETE)
	public ResponseEntity<Void> delete(
			@PathVariable Integer id, 
			@PathVariable Integer idItem, 
			@PathVariable Integer idImagem) {
		service.deleteItensPictures(idImagem);
		return ResponseEntity.noContent().build();
	}
	
}