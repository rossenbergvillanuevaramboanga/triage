package it.prova.triage.service;

import java.util.List;

import it.prova.triage.model.Utente;

public interface UtenteService {

	// CRUD
	public List<Utente> listAllUtenti();
	public Utente caricaSingoloUtente(Long id);
	public Utente caricaSingoloUtenteConRuoli(Long id);
	public Utente aggiorna(Utente utenteInstance);
	public void inserisciNuovo(Utente utenteInstance);
	public void rimuovi(Utente utenteInstance);
	public List<Utente> findByExample(Utente buildUtenteModel);

}
