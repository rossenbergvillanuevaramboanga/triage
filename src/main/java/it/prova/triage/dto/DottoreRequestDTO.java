package it.prova.triage.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DottoreRequestDTO {

	private String codiceDottore;
	private String codiceFiscalePaziente;

	public DottoreRequestDTO() {
		// TODO Auto-generated constructor stub
	}

	public DottoreRequestDTO(String codiceDottore, String codiceFiscalePaziente) {
		super();
		this.codiceDottore = codiceDottore;
		this.codiceFiscalePaziente = codiceFiscalePaziente;
	}
	
	public DottoreRequestDTO(String codiceFiscalePaziente) {
		super();
		this.codiceFiscalePaziente = codiceFiscalePaziente;
	}

	public String getCodiceFiscalePaziente() {
		return codiceFiscalePaziente;
	}

	public void setCodiceFiscalePaziente(String codiceFiscalePaziente) {
		this.codiceFiscalePaziente = codiceFiscalePaziente;
	}

	public String getCodiceDottore() {
		return codiceDottore;
	}

	public void setCodiceDottore(String codiceDottore) {
		this.codiceDottore = codiceDottore;
	}

}
