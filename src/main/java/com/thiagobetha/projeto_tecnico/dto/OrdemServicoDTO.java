package com.thiagobetha.projeto_tecnico.dto;

import java.io.Serializable;

public class OrdemServicoDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer idCliente;
	
	OrdemServicoDTO(){}

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
	
}