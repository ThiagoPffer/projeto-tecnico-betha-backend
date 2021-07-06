package com.thiagobetha.projeto_tecnico;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.thiagobetha.projeto_tecnico.domain.ItemOrdemServico;
import com.thiagobetha.projeto_tecnico.domain.OrdemServico;
import com.thiagobetha.projeto_tecnico.repositories.OrdemServicoRepository;

@SpringBootApplication
public class ProjetoTecnicoApplication implements CommandLineRunner{

	@Autowired
	private OrdemServicoRepository ordemServicoRepo;
	
	public static void main(String[] args) {
		SpringApplication.run(ProjetoTecnicoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		OrdemServico os1 = new OrdemServico("Maria");
		OrdemServico os2 = new OrdemServico("Thiago");
		OrdemServico os3 = new OrdemServico("João");
		
		List<ItemOrdemServico> lista1 = new ArrayList<>();
		List<ItemOrdemServico> lista2 = new ArrayList<>();
		List<ItemOrdemServico> lista3 = new ArrayList<>();
		
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
		
		ordemServicoRepo.saveAll(Arrays.asList(os1, os2, os3));
	}
	
	

}
