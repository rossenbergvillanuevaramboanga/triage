package it.prova.triage.web.api.exception;

public class PazienteDeleteException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public PazienteDeleteException(String string) {
		super(string);
	}
}
