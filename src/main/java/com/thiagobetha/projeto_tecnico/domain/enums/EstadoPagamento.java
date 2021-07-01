package com.thiagobetha.projeto_tecnico.domain.enums;

public enum EstadoPagamento {
	PENDENTE(1, "Pendente"), PAGO(2, "Pago"), CANCELADO(3, "Cancelado");
	
	private int cod;
	private String desc;
	
	private EstadoPagamento(int cod, String desc) {
		this.cod = cod;
		this.desc = desc;
	}
	
	public int getCod() {
		return cod;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public static EstadoPagamento toEnum(Integer cod) {
		if(cod == null) {
			return null;
		}
		
		for(EstadoPagamento x : EstadoPagamento.values()) {
			if(cod.equals(x.getCod())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("ID Inválido: " + cod);
	}
	
}
