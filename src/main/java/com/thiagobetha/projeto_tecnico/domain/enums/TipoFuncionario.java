package com.thiagobetha.projeto_tecnico.domain.enums;

public enum TipoFuncionario {
	ADMINISTRADOR(1, "Administrador"), RECEPCIONISTA(2, "Recepcionista"), TECNICO(3, "Técnico");
	
	private int cod;
	private String desc;
	
	private TipoFuncionario(int cod, String desc) {
		this.cod = cod;
		this.desc = desc;
	}
	
	public int getCod() {
		return cod;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public static TipoFuncionario toEnum(Integer cod) {
		if(cod == null) {
			return null;
		}
		
		for(TipoFuncionario x : TipoFuncionario.values()) {
			if(cod.equals(x.getCod())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("ID Inválido: " + cod);
	}
}
