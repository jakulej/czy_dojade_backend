package ziwg.czy_dojade_backend.repositories;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ziwg.czy_dojade_backend.models.Trip;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
@Hidden
public interface TripRepository extends JpaRepository<Trip, String> {
    boolean existsById(String id);
    Optional<Trip> findById(String id);
    List<Trip> findAllByTripHeadsign(String tripHeadsign);

    List<Trip> findByRouteId(String id);
    List<Trip> findByVehicleId(long id);
}
