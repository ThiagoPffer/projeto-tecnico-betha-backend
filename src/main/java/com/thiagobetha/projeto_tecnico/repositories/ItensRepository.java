package com.thiagobetha.projeto_tecnico.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thiagobetha.projeto_tecnico.domain.ItemOrdemServico;

public interface ItensRepository extends JpaRepository<ItemOrdemServico, Integer>{

}
