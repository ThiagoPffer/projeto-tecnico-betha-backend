package com.thiagobetha.projeto_tecnico.services.exceptions;

public class InvalidSituationException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public InvalidSituationException(String msg) {
		super(msg);
	}
	
	public InvalidSituationException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
