package it.prova.triage.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.prova.triage.model.Utente;

public interface UtenteRepository extends CrudRepository<Utente, Long> {
	
	@Query("from Utente u left join fetch u.ruoli where u.id = ?1")
	Optional<Utente> findByIdConEager(Long id);
	
	@EntityGraph(attributePaths = { "ruoli" })
	Optional<Utente> findByUsername(String username);

}
