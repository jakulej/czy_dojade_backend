package ziwg.czy_dojade_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ziwg.czy_dojade_backend.models.Accident;

import java.util.List;

@RepositoryRestResource
public interface AccidentRepository extends JpaRepository<Accident, Long> {
    boolean existsByAccidentId(Long accidentId);
    List<Accident> findAllByTrip_Id(Long routeId);
}
