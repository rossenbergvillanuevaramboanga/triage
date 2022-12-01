package it.prova.triage;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.prova.triage.model.Ruolo;
import it.prova.triage.model.StatoUtente;
import it.prova.triage.model.Utente;
import it.prova.triage.service.RuoloService;
import it.prova.triage.service.UtenteService;

@SpringBootApplication
public class TriageApplication implements CommandLineRunner {

	@Autowired
	private RuoloService ruoloServiceInstance;
	@Autowired
	private UtenteService utenteServiceInstance;

	public static void main(String[] args) {
		SpringApplication.run(TriageApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// RUOLO

		if (ruoloServiceInstance.cercaPerDescrizioneECodice("Administrator", Ruolo.ROLE_ADMIN) == null) {
			ruoloServiceInstance.inserisciNuovo(new Ruolo("Administrator", Ruolo.ROLE_ADMIN));
		}

		if (ruoloServiceInstance.cercaPerDescrizioneECodice("SubOperator", Ruolo.ROLE_SUB_OPERATOR) == null) {
			ruoloServiceInstance.inserisciNuovo(new Ruolo("SubOperator", Ruolo.ROLE_SUB_OPERATOR));
		}

		// UTENTE
		if (utenteServiceInstance.findByUsername("admin") == null) {
			Utente admin = new Utente("admin", "admin", "Mario", "Rossi", LocalDate.now());
			admin.getRuoli().add(ruoloServiceInstance.cercaPerDescrizioneECodice("Administrator", Ruolo.ROLE_ADMIN));
			utenteServiceInstance.inserisciNuovo(admin);
			// Attivazione
			admin.setStato(StatoUtente.ATTIVO);
			utenteServiceInstance.aggiorna(admin);
		}
		
		if (utenteServiceInstance.findByUsername("suboperator") == null) {
			Utente subOperator = new Utente("suboperator", "suboperator", "Luca", "Verdi", LocalDate.now());
			subOperator.getRuoli().add(ruoloServiceInstance.cercaPerDescrizioneECodice("SubOperator", Ruolo.ROLE_ADMIN));
			utenteServiceInstance.inserisciNuovo(subOperator);
			// Attivazione
			subOperator.setStato(StatoUtente.ATTIVO);
			utenteServiceInstance.aggiorna(subOperator);
		}
	}

}
