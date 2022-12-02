package it.prova.triage.web.api;

import java.util.List;
import java.util.function.Function;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import it.prova.triage.dto.DottoreRequestDTO;
import it.prova.triage.dto.DottoreResponseDTO;
import it.prova.triage.dto.PazienteDTO;
import it.prova.triage.model.Paziente;
import it.prova.triage.model.StatoPaziente;
import it.prova.triage.service.PazienteService;
import it.prova.triage.web.api.exception.DottoreNotAvailbleException;
import it.prova.triage.web.api.exception.DottoreNotFoundException;
import it.prova.triage.web.api.exception.ErrorConnectionException;
import it.prova.triage.web.api.exception.IdNotNullForInsertException;
import it.prova.triage.web.api.exception.PazienteDeleteException;
import it.prova.triage.web.api.exception.PazienteNotFoundException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/paziente")
public class PazienteController {

	@Autowired
	private PazienteService pazienteService;

	@Autowired
	private WebClient webClient;

	@GetMapping
	public List<PazienteDTO> getAll() {
		return PazienteDTO.buildPazienteDTOListFromModelList(pazienteService.listAll());
	}

	@GetMapping("/{id}")
	public PazienteDTO findById(@PathVariable(value = "id", required = true) Long id) {

		Paziente pazienteModel = pazienteService.caricaSingoloElemento(id);

		PazienteDTO result = PazienteDTO.buildPazienteDTOModel(pazienteModel);

		try {
			DottoreResponseDTO dottoreResponseDTO = webClient.get().uri("/find/" + pazienteModel.getCodiceFiscale())
					.retrieve().bodyToMono(DottoreResponseDTO.class).block();

			if (dottoreResponseDTO != null && dottoreResponseDTO.getCodiceFiscalePaziente() != null
					&& dottoreResponseDTO.getCodiceFiscalePaziente().equals(result.getCodiceFiscale())) {

				result.setCodiceDottore(dottoreResponseDTO.getCodiceDottore());
			}

		} catch (Exception e) {
			// TODO: handle exception
			return result;
		}

		return result;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PazienteDTO createNew(@Valid @RequestBody PazienteDTO pazienteInput) {

		if (pazienteInput.getId() != null)
			throw new IdNotNullForInsertException("Non è ammesso fornire un id per la creazione");

		Paziente paziente = pazienteInput.buildPazienteModel();
		pazienteService.inserisciNuovo(paziente);

		return PazienteDTO.buildPazienteDTOModel(paziente);
	}

	@PutMapping("/{id}")
	public PazienteDTO update(@Valid @RequestBody PazienteDTO pazienteInput, @PathVariable(required = true) Long id) {
		Paziente paziente = pazienteService.caricaSingoloElemento(id);

		if (paziente == null)
			throw new PazienteNotFoundException("Paziente not found con id: " + id);

		pazienteInput.setId(id);
		Paziente pazienteAggiornato = pazienteService.aggiorna(pazienteInput.buildPazienteModel());
		return PazienteDTO.buildPazienteDTOModel(pazienteAggiornato);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable(required = true) Long id) {
		Paziente paziente = pazienteService.caricaSingoloElemento(id);

		if (paziente == null)
			throw new PazienteNotFoundException("Utente not found con id: " + id);

		if (paziente.getStato() != null && paziente.getStato() != StatoPaziente.DIMESSO)
			throw new PazienteDeleteException("Utente not found con id: " + id);

		pazienteService.rimuovi(paziente);
	}

	// TODO Implementare AssegnaPaziente, Ricovera e Dimetti
	@PostMapping("/assegnaPaziente")
	public DottoreResponseDTO assegnaPaziente(@Valid @RequestBody PazienteDTO pazienteInput) {

		// Verifico se il paziente esiste
		Paziente paziente = pazienteService.findByCodiceFiscale(pazienteInput.getCodiceFiscale());
		if (paziente == null)
			throw new PazienteNotFoundException(
					"Paziente not found con Codice Fiscale: " + pazienteInput.getCodiceFiscale());

		// Verifica se il dottore prescelto è disponibile contattando l'API esterna
		ResponseEntity<DottoreResponseDTO> response = webClient.get()
				.uri("/verifica/" + pazienteInput.getCodiceDottore())
				.retrieve()
				.onStatus(HttpStatus::is4xxClientError, responseEntity -> {
					throw new DottoreNotFoundException("Dottore not found");
				})
				.toEntity(DottoreResponseDTO.class)
				.block();

		// Gestione della risposta
//		if (response.getStatusCode() != HttpStatus.OK) {
//			if (response.getStatusCode() == HttpStatus.NOT_FOUND)
//				throw new DottoreNotFoundException("Dottore non trovato");
//			if (response.getStatusCode() == HttpStatus.FORBIDDEN)
//				throw new DottoreNotAvailbleException("Dottore non disponibile");
//			else
//				throw new ErrorConnectionException("Errore nella connessione");
//		}

		// Assegnazione del Dottore al paziente
		response = webClient.post().uri("/impostaInVisita/")
				.body(Mono.just(
						new DottoreRequestDTO(pazienteInput.getCodiceDottore(), pazienteInput.getCodiceFiscale())),
						DottoreRequestDTO.class)
				.retrieve()
				.onStatus(HttpStatus::is4xxClientError, responseEntity -> {
					throw new DottoreNotFoundException("Dottore not found");
				})
				.toEntity(DottoreResponseDTO.class).block();

//		// Gestione della risposta
//		if (response.getStatusCode() != HttpStatus.OK) {
//			if (response.getStatusCode() == HttpStatus.NOT_FOUND)
//				throw new DottoreNotFoundException("Dottore non trovato");
//			if (response.getStatusCode() == HttpStatus.FORBIDDEN)
//				throw new DottoreNotAvailbleException("Dottore non disponibile");
//			else
//				throw new ErrorConnectionException("Errore nella connessione");
//		}

		// Update del paziente
		paziente.setStato(StatoPaziente.IN_VISITA);
		pazienteService.aggiorna(paziente);

		return response.getBody();
	}

	@PostMapping("/ricovera/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void ricovera(@PathVariable(required = true) Long id) {

		Paziente paziente = pazienteService.caricaSingoloElemento(id);
		if (paziente == null)
			throw new PazienteNotFoundException("Paziente not found con id: " + id);

		// Verifica sullo stato del paziente
		if (!paziente.getStato().equals(StatoPaziente.IN_VISITA))
			throw new PazienteNotFoundException("Paziente not IN VISITA con" + id);

		ResponseEntity<DottoreResponseDTO> response = webClient.post().uri("/terminaVisita/")
				.body(Mono.just(new DottoreRequestDTO(paziente.getCodiceFiscale())), DottoreRequestDTO.class).retrieve()
				.onStatus(HttpStatus::is4xxClientError, responseEntity -> {
					throw new DottoreNotFoundException("Dottore not found");
				})
				.toEntity(DottoreResponseDTO.class).block();

//		// Gestione della risposta
//		if (response.getStatusCode() != HttpStatus.OK) {
//			if (response.getStatusCode() == HttpStatus.NOT_FOUND)
//				throw new DottoreNotFoundException("Dottore non trovato");
//			throw new ErrorConnectionException("Errore nella connessione");
//		}

		// Update del paziente
		paziente.setStato(StatoPaziente.RICOVERATO);
		pazienteService.aggiorna(paziente);

	}

	@PostMapping("/dimetti/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void dimetti(@PathVariable(required = true) Long id) {

		Paziente paziente = pazienteService.caricaSingoloElemento(id);
		if (paziente == null)
			throw new PazienteNotFoundException("Paziente not found con id: " + id);

		PazienteDTO pazienteDT0 = this.findById(id);

		// Verifica sullo stato del paziente
		if (paziente.getStato().equals(StatoPaziente.DIMESSO))
			throw new PazienteNotFoundException("Paziente già dimesso con id: " + id);

		if (pazienteDT0.getCodiceDottore() != null) {

			// Verifica se il dottore prescelto è disponibile contattando l'API esterna
			ResponseEntity<DottoreResponseDTO> response = webClient.post().uri("/terminaVisita/")
					.body(Mono.just(new DottoreRequestDTO(paziente.getCodiceFiscale())), DottoreRequestDTO.class)
					.retrieve()
					.onStatus(HttpStatus::is4xxClientError, responseEntity -> {
						throw new DottoreNotFoundException("Dottore not found");
					})
					.toEntity(DottoreResponseDTO.class).block();

			// Gestione della risposta
//			if (response.getStatusCode() != HttpStatus.OK) {
//				if (response.getStatusCode() == HttpStatus.NOT_FOUND)
//					throw new DottoreNotFoundException("Il paziente non ha il dottore non trovato");
//				throw new ErrorConnectionException("Errore nella connessione");
//			}
		}

		// Update del paziente
		paziente.setStato(StatoPaziente.DIMESSO);
		pazienteService.aggiorna(paziente);

	}

}
