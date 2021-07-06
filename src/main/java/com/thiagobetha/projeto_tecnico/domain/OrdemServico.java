package com.thiagobetha.projeto_tecnico.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.thiagobetha.projeto_tecnico.domain.enums.EstadoPagamento;
import com.thiagobetha.projeto_tecnico.domain.enums.SituacaoOrdemServico;

@Entity
public class OrdemServico implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private BigDecimal valorTotal = BigDecimal.ZERO;
	
	private Integer situacao;
	private Integer pagamento;
	
	private String cliente; //configurar, falta setar endereço
	
	@OneToMany(mappedBy = "ordemServico", cascade = CascadeType.ALL)
	private List<ItemOrdemServico> itens = new ArrayList<>();
	
	public OrdemServico() {
		this.pagamento = 1;
		this.situacao = 1;
	}

	public OrdemServico(String cliente) {
		super();
		this.pagamento = 1;
		this.situacao = 1;
		this.cliente = cliente;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getValorTotal() {		
		/*for(ItemOrdemServico item : itens) {
			valorTotal.add(item.getOrcamento());
		}*/
		return (valorTotal == null) ? BigDecimal.ZERO : valorTotal;
	}
	
	public void setValorTotal(BigDecimal orcamento) {
		this.valorTotal = valorTotal.add(orcamento);
	}

	public SituacaoOrdemServico getSituacao() {
		return SituacaoOrdemServico.toEnum(situacao);
	}

	public void setSituacao(SituacaoOrdemServico situacao) {
		this.situacao = situacao.getCod();
	}

	public EstadoPagamento getPagamento() {
		return EstadoPagamento.toEnum(pagamento);
	}

	public void setPagamento(EstadoPagamento pagamento) {
		this.pagamento = pagamento.getCod();
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public List<ItemOrdemServico> getItens() {
		return itens;
	}

	public void setItens(List<ItemOrdemServico> itens) {
		this.itens = itens;
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
		OrdemServico other = (OrdemServico) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}