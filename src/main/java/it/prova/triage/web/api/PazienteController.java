package it.prova.triage.web.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import it.prova.triage.dto.DottoreResponseDTO;
import it.prova.triage.dto.PazienteDTO;
import it.prova.triage.model.Paziente;
import it.prova.triage.model.StatoPaziente;
import it.prova.triage.service.PazienteService;
import it.prova.triage.web.api.exception.IdNotNullForInsertException;
import it.prova.triage.web.api.exception.PazienteDeleteException;
import it.prova.triage.web.api.exception.PazienteNotFoundException;

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

		DottoreResponseDTO dottoreResponseDTO = webClient.get().uri("/" + pazienteModel.getCodiceFiscale()).retrieve()
				.bodyToMono(DottoreResponseDTO.class).block();

		PazienteDTO result = PazienteDTO.buildPazienteDTOModel(pazienteModel);

		if (dottoreResponseDTO != null && dottoreResponseDTO.getCodiceFiscalePaziente() != null
				&& dottoreResponseDTO.getCodiceFiscalePaziente().equals(result.getCodiceFiscale())) {
			result.setCodiceDottore(dottoreResponseDTO.getCodiceDottore());

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
			throw new PazienteNotFoundException("Utente not found con id: " + id);

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
	
	//TODO Implementare AssegnaPaziente, Ricovera e Dimetti

}
