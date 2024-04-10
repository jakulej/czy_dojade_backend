package ziwg.czy_dojade_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ziwg.czy_dojade_backend.models.Trip;

import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {
    boolean existsById(Long tripId);
    Optional<Trip> findById(Long tripId);
    List<Trip> findAllByTripHeadsign(String tripHeadsign);
}
