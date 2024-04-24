package ziwg.czy_dojade_backend.repositories;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ziwg.czy_dojade_backend.models.Accident;

@RepositoryRestResource
@Hidden
public interface AccidentRepository extends JpaRepository<Accident, String> {
    boolean existsById(String id);
//    Optional<Accident> findByTripId(String id);
    Accident findByTripId(String id);
}
