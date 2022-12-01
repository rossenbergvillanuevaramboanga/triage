package it.prova.triage.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import it.prova.triage.dto.DottoreResponseDTO;
import it.prova.triage.dto.PazienteDTO;
import it.prova.triage.model.Paziente;
import it.prova.triage.service.PazienteService;

@RestController
@RequestMapping("/api/paziente")
public class PazienteController {

	@Autowired
	private PazienteService pazienteService;

	@Autowired
	private WebClient webClient;

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

//	@PostMapping
//	public PazienteDTO createPaziente(@Valid @RequestBody PazienteDTO pazienteInput) {
//
//		if (pazienteInput.getId() != null)
//			throw new IdNotNullForInsertException("Non è ammesso fornire un id per la creazione");
//		
//		Paziente paziente = pazienteInput.buildPazienteModel();
//		pazienteService.inserisciNuovo(paziente);
//
//		return PazienteDTO.buildPazienteDTOModel(paziente);
//	}
//	
//	@PostMapping("/search")
//	public List<UtenteDTO> search(@RequestBody UtenteDTO example) {
//		return UtenteDTO.buildUtenteDTOListFromModelList(utenteService.findByExample(example.buildUtenteModel(true)));
//	}
//	
//	@PutMapping("/{id}")
//	public UtenteDTO update(@Valid @RequestBody UtenteDTO utenteInput, @PathVariable(required = true) Long id) {
//		Utente utente = utenteService.caricaSingoloUtente(id);
//
//		if (utente == null)
//			throw new UtenteNotFoundException("Utente not found con id: " + id);
//
//		utenteInput.setId(id);
//		Utente utenteAggiornato = utenteService.aggiorna(utenteInput.buildUtenteModel(true));
//		return UtenteDTO.buildUtenteDTOFromModel(utenteAggiornato);
//	}
//	
//	@DeleteMapping("/{id}")
//	@ResponseStatus(HttpStatus.OK)
//	public void delete(@PathVariable(required = true) Long id) {
//		Utente utente = utenteService.caricaSingoloUtente(id);
//
//		if (utente == null)
//			throw new UtenteNotFoundException("Utente not found con id: " + id);
//		utenteService.rimuovi(utente);
//	}

}
