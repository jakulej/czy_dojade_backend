package ziwg.czy_dojade_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ziwg.czy_dojade_backend.models.Trip;

import java.util.List;

@RepositoryRestResource
public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByRouteId(long id);
}
