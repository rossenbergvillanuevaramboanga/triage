package it.prova.triage.service;

import java.util.List;

import it.prova.triage.model.Ruolo;

public interface RuoloService {

	// CRUD

	public List<Ruolo> listAll();

	public Ruolo caricaSingoloElemento(Long id);

	public void aggiorna(Ruolo ruoloInstance);

	public void inserisciNuovo(Ruolo ruoloInstance);

	public void rimuovi(Ruolo ruoloInstance);
	
	// FIND
	
	public Ruolo cercaPerDescrizioneECodice(String string, String roleAdmin);


}
