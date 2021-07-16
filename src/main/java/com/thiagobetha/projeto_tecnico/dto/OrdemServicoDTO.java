package com.thiagobetha.projeto_tecnico.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.thiagobetha.projeto_tecnico.domain.ItemOrdemServico;

public class OrdemServicoDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "Preenchimento obrigatório!")
	private Integer idCliente;
	
	@NotEmpty(message = "Pelo menos um item é necessário!")
	private List<ItemOrdemServico> itens = new ArrayList<>();
	
	public OrdemServicoDTO(){}

	public OrdemServicoDTO(Integer idCliente) {
		super();
		this.idCliente = idCliente;
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public List<ItemOrdemServico> getItens() {
		return itens;
	}

	public void setItens(List<ItemOrdemServico> itens) {
		this.itens = itens;
	}
	
}