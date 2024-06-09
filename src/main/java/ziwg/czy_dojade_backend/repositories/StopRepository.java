package ziwg.czy_dojade_backend.repositories;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ziwg.czy_dojade_backend.models.Stop;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
@Hidden
public interface StopRepository extends JpaRepository<Stop, Long> {
    boolean existsById(Long id);
    Optional<Stop> findById(Long id);
    List<Stop> findAllByName(String name);
    Optional<Stop> findByCode(String code);

//    @Query("SELECT s FROM Stop s JOIN s.stopTimes st WHERE st.trip.id = :tripId")
//    List<Stop> findAllStopsByTripId(@Param("tripId") String tripId);

}
