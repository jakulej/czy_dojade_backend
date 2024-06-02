package ziwg.czy_dojade_backend.repositories;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ziwg.czy_dojade_backend.models.RouteType;
import ziwg.czy_dojade_backend.models.Trip;
import ziwg.czy_dojade_backend.models.TripDestination;

import java.util.List;

@RepositoryRestResource
@Hidden
public interface TripDestinationRepository extends JpaRepository<TripDestination, Long> {

    List<TripDestination> findById(String id);
}