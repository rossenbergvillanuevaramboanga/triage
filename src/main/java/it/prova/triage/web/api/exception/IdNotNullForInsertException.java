package it.prova.triage.web.api.exception;

public class IdNotNullForInsertException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public IdNotNullForInsertException(String string) {
		super(string);
	}

}
