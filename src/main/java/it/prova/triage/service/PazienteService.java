package it.prova.triage.service;

import java.util.List;

import it.prova.triage.model.Paziente;

public interface PazienteService {

	// CRUD
	public List<Paziente> listAll();
	public Paziente caricaSingoloElemento(Long id);
	public Paziente aggiorna(Paziente pazienteInstance);
	public void inserisciNuovo(Paziente pazienteInstance);
	public void rimuovi(Paziente pazienteInstance);
	public Paziente findByCodiceFiscale(String string);

}
