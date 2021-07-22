package com.thiagobetha.projeto_tecnico.domain.enums;

public enum SituacaoOrdemServico {
	EM_ANALISE(1, "Em Análise"), AGUARDANDO_DECISAO(2, "Aguardando Decisão"), APROVADA(3, "Aprovada"), 
	CONCLUIDA(4, "Concluída"), CANCELADA(5, "Cancelada");
	
	private int cod;
	private String desc;
	
	private SituacaoOrdemServico(int cod, String desc) {
		this.cod = cod;
		this.desc = desc;
	}
	
	public int getCod() {
		return cod;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public static SituacaoOrdemServico toEnum(Integer cod) {
		if(cod == null) {
			return null;
		}
		
		for(SituacaoOrdemServico x : SituacaoOrdemServico.values()) {
			if(cod.equals(x.getCod())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("ID Inválido: " + cod);
	}
}
