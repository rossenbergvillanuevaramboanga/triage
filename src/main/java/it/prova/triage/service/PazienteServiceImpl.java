package it.prova.triage.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.triage.model.Paziente;
import it.prova.triage.model.StatoPaziente;
import it.prova.triage.repository.PazienteRepository;

@Service
public class PazienteServiceImpl implements PazienteService {
	
	@Autowired
	private PazienteRepository repository;

	@Transactional(readOnly = true)
	public List<Paziente> listAll() {
		return (List<Paziente>) repository.findAll();
	}

	@Transactional(readOnly = true)
	public Paziente caricaSingoloElemento(Long id) {
		// TODO Auto-generated method stub
		return repository.findById(id).orElse(null);
	}

	@Transactional
	public Paziente aggiorna(Paziente pazienteInstance) {
		// TODO Auto-generated method stub
		Paziente pazienteReloaded = repository.findById(pazienteInstance.getId()).orElse(null);
		if(pazienteReloaded == null)
			throw new RuntimeException("Elemento non trovato");
		pazienteReloaded.setNome(pazienteInstance.getNome());
		pazienteReloaded.setCognome(pazienteInstance.getCognome());
		pazienteReloaded.setCodiceFiscale(pazienteInstance.getCodiceFiscale());
		pazienteReloaded.setStato(pazienteInstance.getStato());
		return repository.save(pazienteReloaded);
	}

	@Transactional
	public void inserisciNuovo(Paziente pazienteInstance) {
		// TODO Auto-generated method stub
		pazienteInstance.setDataRegistrazione(LocalDate.now());
		pazienteInstance.setStato(StatoPaziente.IN_ATTESA_VISITA);
		repository.save(pazienteInstance);
		
	}

	@Transactional
	public void rimuovi(Paziente pazienteInstance) {
		// TODO Auto-generated method stub
		repository.delete(pazienteInstance);
	}

	@Transactional(readOnly = true)
	public Paziente findByCodiceFiscale(String string) {
		// TODO Auto-generated method stub
		return repository.findByCodiceFiscale(string).orElse(null);
	}

}
