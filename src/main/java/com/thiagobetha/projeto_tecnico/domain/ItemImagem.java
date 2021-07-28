package com.thiagobetha.projeto_tecnico.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ItemImagem implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NotEmpty(message = "A url da imagem deve ser preenchida!")
	private String uri;
	private String nomeArquivo;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="itemOrdemServico_id")
	private ItemOrdemServico item;

	public ItemImagem() {}

	public ItemImagem(String uri, ItemOrdemServico item) {
		super();
		this.uri = uri;
		this.item = item;
		this.nomeArquivo = uri.substring(58);
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
	
	public ItemOrdemServico getItem() {
		return item;
	}

	public void setItem(ItemOrdemServico item) {
		this.item = item;
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
		ItemImagem other = (ItemImagem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}