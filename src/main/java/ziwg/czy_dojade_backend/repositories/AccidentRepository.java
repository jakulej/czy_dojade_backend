package ziwg.czy_dojade_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ziwg.czy_dojade_backend.models.Accident;

import java.util.Optional;

@RepositoryRestResource
public interface AccidentRepository extends JpaRepository<Accident, Long> {
    boolean existsById(Long accidentId);
    Optional<Accident> findByTripId(Long tripId);
}
