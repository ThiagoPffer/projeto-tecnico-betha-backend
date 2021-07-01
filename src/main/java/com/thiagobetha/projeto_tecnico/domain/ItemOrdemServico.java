package com.thiagobetha.projeto_tecnico.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ItemOrdemServico  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String equipamento;
	private String descricaoEquipamento;
	private String avariaEquipamento;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="ordemServico_id")
	private OrdemServico ordemServico;

	public ItemOrdemServico() {}
	
	public ItemOrdemServico(String equipamento, String descricaoEquipamento, String avariaEquipamento, OrdemServico ordemServico) {
		super();
		this.equipamento = equipamento;
		this.descricaoEquipamento = descricaoEquipamento;
		this.avariaEquipamento = avariaEquipamento;
		this.ordemServico = ordemServico;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEquipamento() {
		return equipamento;
	}

	public void setEquipamento(String equipamento) {
		this.equipamento = equipamento;
	}
	
	public String getDescricaoItem() {
		return descricaoEquipamento;
	}

	public void setDescricaoItem(String descricaoItem) {
		this.descricaoEquipamento = descricaoItem;
	}

	public String getAvaria() {
		return avariaEquipamento;
	}

	public void setAvaria(String avaria) {
		this.avariaEquipamento = avaria;
	}

	public OrdemServico getOrdemServico() {
		return ordemServico;
	}

	public void setOrdemServico(OrdemServico ordemServico) {
		this.ordemServico = ordemServico;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemOrdemServico other = (ItemOrdemServico) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}