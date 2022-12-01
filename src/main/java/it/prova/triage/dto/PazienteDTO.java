package it.prova.triage.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import it.prova.triage.model.Paziente;
import it.prova.triage.model.StatoPaziente;

public class PazienteDTO {

	private Long id;

	@NotBlank(message = "{nome.notblank}")
	private String nome;

	@NotBlank(message = "{cognome.notblank}")
	private String cognome;

	@NotBlank(message = "{codicefiscale.notblank}")
	private String codiceFiscale;

	private LocalDate dataRegistrazione;

	private StatoPaziente stato;

	private String codiceDottore;

	public PazienteDTO() {
		// TODO Auto-generated constructor stub
	}

	public PazienteDTO(Long id, String nome, String cognome, String codiceFiscale, LocalDate dataRegistrazione,
			StatoPaziente stato) {
		super();
		this.id = id;
		this.nome = nome;
		this.cognome = cognome;
		this.codiceFiscale = codiceFiscale;
		this.dataRegistrazione = dataRegistrazione;
		this.stato = stato;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public LocalDate getDataRegistrazione() {
		return dataRegistrazione;
	}

	public void setDataRegistrazione(LocalDate dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}

	public StatoPaziente getStato() {
		return stato;
	}

	public void setStato(StatoPaziente stato) {
		this.stato = stato;
	}

	public String getCodiceDottore() {
		return codiceDottore;
	}

	public void setCodiceDottore(String codiceDottore) {
		this.codiceDottore = codiceDottore;
	}

	public Paziente buildPazienteModel() {
		return new Paziente(id, nome, cognome, codiceFiscale, dataRegistrazione, stato);
	}

	public static PazienteDTO buildPazienteDTOModel(Paziente pazienteModel) {
		return new PazienteDTO(pazienteModel.getId(), pazienteModel.getNome(), pazienteModel.getCognome(),
				pazienteModel.getCodiceFiscale(), pazienteModel.getDataRegistrazione(), pazienteModel.getStato());
	}

	public static List<PazienteDTO> buildPazienteDTOListFromModelList(List<Paziente> modelList) {
		return modelList.stream().map(entity -> PazienteDTO.buildPazienteDTOModel(entity)).collect(Collectors.toList());

	}

	public static Set<PazienteDTO> buildPazienteDTOSetFromModelSet(Set<Paziente> modelSet) {
		return modelSet.stream().map(entity -> PazienteDTO.buildPazienteDTOModel(entity)).collect(Collectors.toSet());

	}

}
