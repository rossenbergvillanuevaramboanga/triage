package it.prova.triage.web.api.exception;

public class DottoreNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public DottoreNotFoundException(String string) {
		super(string);
	}


}
