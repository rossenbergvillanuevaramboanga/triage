package it.prova.triage.dto;

public class DottoreResponseDTO {
	
	private String nome;
	private String cognome;
	private String codiceDottore;
	private String codiceFiscalePaziente;
	private Boolean inServizio;
	private Boolean inVisita;
	
	public DottoreResponseDTO() {
		// TODO Auto-generated constructor stub
	}

	public DottoreResponseDTO(String nome, String cognome, String codiceDottore, String codiceFiscalePaziente) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.codiceDottore = codiceDottore;
		this.codiceFiscalePaziente = codiceFiscalePaziente;
	}
	

	public DottoreResponseDTO(String nome, String cognome, String codiceDottore, Boolean inServizio, Boolean inVisita) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.codiceDottore = codiceDottore;
		this.inServizio = inServizio;
		this.inVisita = inVisita;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getCodiceDottore() {
		return codiceDottore;
	}

	public void setCodiceDottore(String codiceDottore) {
		this.codiceDottore = codiceDottore;
	}

	public String getCodiceFiscalePaziente() {
		return codiceFiscalePaziente;
	}

	public void setCodiceFiscalePaziente(String codiceFiscalePaziente) {
		this.codiceFiscalePaziente = codiceFiscalePaziente;
	}

	public Boolean getInServizio() {
		return inServizio;
	}

	public void setInServizio(Boolean inServizio) {
		this.inServizio = inServizio;
	}

	public Boolean getInVisita() {
		return inVisita;
	}

	public void setInVisita(Boolean inVisita) {
		this.inVisita = inVisita;
	}
	
	
	
	

}
