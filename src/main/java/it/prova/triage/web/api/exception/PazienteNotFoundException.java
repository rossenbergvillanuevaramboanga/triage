package it.prova.triage.web.api.exception;

public class PazienteNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public PazienteNotFoundException(String string) {
		super(string);
	}
}
