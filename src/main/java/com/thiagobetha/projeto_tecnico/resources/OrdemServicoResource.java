package com.thiagobetha.projeto_tecnico.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.thiagobetha.projeto_tecnico.domain.OrdemServico;
import com.thiagobetha.projeto_tecnico.domain.enums.SituacaoOrdemServico;

@RestController
@RequestMapping(value="/ordensservico")
public class OrdemServicoResource {
	
	/*@Autowired
	private OrdemServicoService service;*/
	
	@RequestMapping(method = RequestMethod.GET)
	public List<OrdemServico> listAll() {
		
		List<OrdemServico> lista = new ArrayList<>();
		
		String item1 = "Televisor";
		String item2 = "Ferro de passar";
		
		OrdemServico os1 = new OrdemServico(SituacaoOrdemServico.EM_ANALISE, "PENDENTE", "Maria Silva");
		os1.setItens(item1);

		OrdemServico os2 = new OrdemServico(SituacaoOrdemServico.EM_ANALISE, "PENDENTE", "Pedro Silva");
		os2.setItens(item2);
		
		lista.add(os1);
		lista.add(os2);
		
		return lista;
		
		/*List<OrdemServico> list = service.findAll();
		return ResponseEntity.ok().body(list);*/
	}
	
}