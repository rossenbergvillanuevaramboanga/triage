package it.prova.triage.dto;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import it.prova.triage.model.Ruolo;
import it.prova.triage.model.StatoUtente;
import it.prova.triage.model.Utente;

public class UtenteDTO {

	private Long id;

	@NotBlank(message = "{username.notblank}")
	@Size(min = 3, max = 15, message = "Il valore inserito '${validatedValue}' deve essere lungo tra {min} e {max} caratteri")
	private String username;

	@NotBlank(message = "{password.notblank}")
	@Size(min = 8, max = 15, message = "Il valore inserito deve essere lungo tra {min} e {max} caratteri")
	private String password;

	private String confermaPassword;

	@NotBlank(message = "{nome.notblank}")
	private String nome;

	@NotBlank(message = "{cognome.notblank}")
	private String cognome;

	private LocalDate dataRegistrazione;

	private StatoUtente stato;

	private Long[] ruoliIds;

	public UtenteDTO() {
		// TODO Auto-generated constructor stub
	}

	public UtenteDTO(Long id, String username, String password, String confermaPassword, String nome, String cognome,
			LocalDate dataRegistrazione, StatoUtente stato) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.confermaPassword = confermaPassword;
		this.nome = nome;
		this.cognome = cognome;
		this.dataRegistrazione = dataRegistrazione;
		this.stato = stato;
	}

	public UtenteDTO(Long id, String username, String password, String nome, String cognome,
			LocalDate dataRegistrazione, StatoUtente stato) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.nome = nome;
		this.cognome = cognome;
		this.dataRegistrazione = dataRegistrazione;
		this.stato = stato;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfermaPassword() {
		return confermaPassword;
	}

	public void setConfermaPassword(String confermaPassword) {
		this.confermaPassword = confermaPassword;
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

	public LocalDate getDataRegistrazione() {
		return dataRegistrazione;
	}

	public void setDataRegistrazione(LocalDate dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}

	public StatoUtente getStato() {
		return stato;
	}

	public void setStato(StatoUtente stato) {
		this.stato = stato;
	}

	public Utente buildUtenteModel(boolean includeRuoli) {
		Utente result = new Utente(id, username, confermaPassword, nome, cognome, dataRegistrazione, stato);
		if (includeRuoli && ruoliIds != null)
			result.setRuoli(Arrays.asList(ruoliIds).stream().map(id -> new Ruolo(id)).collect(Collectors.toSet()));
		return result;

	}

	public static UtenteDTO buildUtenteDTOFromModel(Utente utenteModel) {
		UtenteDTO result = new UtenteDTO(utenteModel.getId(), utenteModel.getUsername(), utenteModel.getPassword(),
				utenteModel.getNome(), utenteModel.getCognome(), utenteModel.getDataRegistrazione(),
				utenteModel.getStato());

		if (!utenteModel.getRuoli().isEmpty())
			result.ruoliIds = utenteModel.getRuoli().stream().map(r -> r.getId()).collect(Collectors.toList())
					.toArray(new Long[] {});

		return result;
	}

	public static List<UtenteDTO> buildUtenteDTOListFromModelList(List<Utente> modelList) {
		return modelList.stream().map(entity -> UtenteDTO.buildUtenteDTOFromModel(entity)).collect(Collectors.toList());
	}

	public static Set<UtenteDTO> buildUtenteDTOSetFromModelSet(Set<Utente> modelList) {
		return (Set<UtenteDTO>) modelList.stream().map(entity -> UtenteDTO.buildUtenteDTOFromModel(entity))
				.collect(Collectors.toSet());
	}

}
