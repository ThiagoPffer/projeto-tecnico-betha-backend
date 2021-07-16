package com.thiagobetha.projeto_tecnico.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thiagobetha.projeto_tecnico.domain.Cliente;
import com.thiagobetha.projeto_tecnico.domain.Endereco;
import com.thiagobetha.projeto_tecnico.domain.Funcionario;
import com.thiagobetha.projeto_tecnico.domain.ItemOrdemServico;
import com.thiagobetha.projeto_tecnico.domain.OrdemServico;
import com.thiagobetha.projeto_tecnico.repositories.ClienteRepository;
import com.thiagobetha.projeto_tecnico.repositories.FuncionarioRepository;
import com.thiagobetha.projeto_tecnico.repositories.OrdemServicoRepository;

@Service
public class DBService {
	
	@Autowired
	private OrdemServicoRepository ordemServicoRepo;
	@Autowired
	private ClienteRepository clienteRepo;
	@Autowired
	private FuncionarioRepository funcionarioRepo;
	
	public void instantiateTestDatabase() {
		
		Funcionario adm = new Funcionario("Rodrigo Branas", "branas.rodrigoadm@gmail.com", 1);
		Funcionario recep = new Funcionario("Rodrigo Branas", "branas.rodrigorecep@gmail.com", 2);
		Funcionario tec = new Funcionario("Rodrigo Branas", "branas.rodrigotec@gmail.com", 3);
		
		Cliente cli1 = new Cliente("Thiago", "thiago.piffer@hotmail.com", "48998665287");
		Endereco end1 = new Endereco(cli1, "Rua dos Correios Privatizados", "786", "Coloninha", "Araranguá", "Santa Catarina");
		cli1.setEndereco(end1);
		
		Cliente cli2 = new Cliente("Maria", "mariatrdder@gmail.com", "48998223342");
		Endereco end2 = new Endereco(cli2, "Avenida Sete de Setembro", "1086", "Urussanguinha", "Araranguá", "Santa Catarina");
		cli2.setEndereco(end2);
		
		Cliente cli3 = new Cliente("João", "joao@hotmail.com", "48998142352");
		Endereco end3 = new Endereco(cli3, "Rua dos Pinheiros", "203", "Centro", "Araranguá", "Santa Catarina");
		cli3.setEndereco(end3);
		
		List<ItemOrdemServico> lista1 = new ArrayList<>();
		List<ItemOrdemServico> lista2 = new ArrayList<>();
		List<ItemOrdemServico> lista3 = new ArrayList<>();
		
		OrdemServico os1 = new OrdemServico(cli1);
		OrdemServico os2 = new OrdemServico(cli2);
		OrdemServico os3 = new OrdemServico(cli3);
		
		ItemOrdemServico item1 = new ItemOrdemServico("Micro-ondas", 
				"Equipamento para aquecer comida, serie LD3939929, marca ELECTROLUX", 
				"Problemas no display", os1);
		
		ItemOrdemServico item2 = new ItemOrdemServico("Máquina de lavar", 
				"Equipamento para lavar roupas, serie JUDIF12003, marca BRASTEMP", 
				"Problemas no motor", os1);
		
		ItemOrdemServico item3 = new ItemOrdemServico("Air-fryer", 
				"Equipamento para fazer comida, serie POL100303, marca POLISHOP, tipo TURBO-MAX-ALPHA-BRAVO-SUPER-ULTRA", 
				"Equipamento não funciona, cliente relata ter tentado fazer macarrão usando o dispositivo.", os2);

		ItemOrdemServico item4 = new ItemOrdemServico("Roteador w-fi", 
				"Equipamento para internet, serie LS020302, marca TP-LINK", 
				"Problemas com a conexão, cliente relata ter sofrido ataques atavés do aparelho.", os2);
		
		ItemOrdemServico item5 = new ItemOrdemServico("Micro-ondas", 
				"Equipamento para aquecer comida, serie LD3939929", 
				"De acordo com o cliente: 'o prato giratório não gira mais'.", os3);
		
		lista1.add(item1);
		lista1.add(item2);
		lista2.add(item3);
		lista2.add(item4);
		lista3.add(item5);
		
		os1.setItens(lista1);
		os2.setItens(lista2);
		os3.setItens(lista3);
		
		cli1.setOrdensServico(os1);
		cli2.setOrdensServico(os2);
		cli3.setOrdensServico(os3);

		clienteRepo.saveAll(Arrays.asList(cli1, cli2, cli3));
		ordemServicoRepo.saveAll(Arrays.asList(os1, os2, os3));
		funcionarioRepo.saveAll(Arrays.asList(adm, recep, tec));
		
	}
}