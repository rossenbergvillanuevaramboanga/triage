package it.prova.triage.web.api.exception;

public class ErrorConnectionException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public ErrorConnectionException(String string) {
		super(string);
	}

}
