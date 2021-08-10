package com.thiagobetha.projeto_tecnico.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ItemOrdemServico  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NotEmpty(message = "Preenchimento obrigatório!")
	private String equipamento;
	@Lob
	@NotEmpty(message = "Preenchimento obrigatório!")
	@Length(min = 25, message = "Forneça uma descrição mais detalhada!")
	private String descricao;
	@Lob
	@NotEmpty(message = "Preenchimento obrigatório!")
	@Length(min = 15, message = "A avaria deve ser mais detalhada!")
	private String avaria;
	private BigDecimal orcamento = BigDecimal.ZERO;
	
	@OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
	private List<ItemImagem> imagens = new ArrayList<>();

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="ordemServico_id")
	private OrdemServico ordemServico;

	public ItemOrdemServico() {}
	
	public ItemOrdemServico(
			String equipamento, 
			String descricao, 
			String avaria, 
			OrdemServico ordemServico) {
		super();
		this.equipamento = equipamento;
		this.descricao = descricao;
		this.avaria = avaria;
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
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getAvaria() {
		return avaria;
	}

	public void setAvaria(String avaria) {
		this.avaria = avaria;
	}

	public BigDecimal getOrcamento() {
		return (orcamento == null) ? BigDecimal.ZERO : orcamento;
	}

	public void setOrcamento(BigDecimal orcamento) {
		this.orcamento = orcamento;
	}
	
	public List<ItemImagem> getImagens() {
		return imagens;
	}

	public void addImagens(ItemImagem imagem) {
		imagens.add(imagem);
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

	@Override
	public String toString() {
		NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
		StringBuilder builder = new StringBuilder();
		builder.append("Equipamento: ");
		builder.append(getEquipamento());
		builder.append("\nDescrição do equipamento: ");
		builder.append(getDescricao());
		builder.append("\nAvaria do equipamento: ");
		builder.append(getAvaria());
		builder.append("\nOrçamento: ");
		builder.append(nf.format(getOrcamento()));
		return builder.toString();
	}
	
}