package com.thiagobetha.projeto_tecnico.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thiagobetha.projeto_tecnico.domain.ItemOrdemServico;

@Repository
public interface ItemOrdemServicoRepository extends JpaRepository<ItemOrdemServico, Integer>{
	
}