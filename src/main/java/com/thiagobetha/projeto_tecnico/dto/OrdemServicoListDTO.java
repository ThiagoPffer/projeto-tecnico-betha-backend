package com.thiagobetha.projeto_tecnico.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.thiagobetha.projeto_tecnico.domain.OrdemServico;
import com.thiagobetha.projeto_tecnico.domain.enums.EstadoPagamento;
import com.thiagobetha.projeto_tecnico.domain.enums.SituacaoOrdemServico;

public class OrdemServicoListDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer id;
	private SituacaoOrdemServico situacao;
	private EstadoPagamento pagamento;
	private LocalDateTime instante;
	private BigDecimal valorTotal;
	private String cliente;
	
	public OrdemServicoListDTO() {}
	
	public OrdemServicoListDTO(OrdemServico obj) {
		this.id = obj.getId();
		this.situacao = obj.getSituacao();
		this.pagamento = obj.getPagamento();
		this.instante = obj.getInstante();
		this.valorTotal = obj.getValorTotal();
		this.cliente = obj.getCliente().getNome();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SituacaoOrdemServico getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoOrdemServico situacao) {
		this.situacao = situacao;
	}

	public EstadoPagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(EstadoPagamento pagamento) {
		this.pagamento = pagamento;
	}

	public LocalDateTime getInstante() {
		return instante;
	}

	public void setInstante(LocalDateTime instante) {
		this.instante = instante;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	
}