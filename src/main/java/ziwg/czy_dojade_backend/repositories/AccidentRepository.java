package ziwg.czy_dojade_backend.repositories;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ziwg.czy_dojade_backend.models.Accident;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource
@Hidden
public interface AccidentRepository extends JpaRepository<Accident, String> {
    boolean existsById(String id);
    Accident findByTripId(String id);

    @Query("SELECT a FROM Accident a WHERE a.timeOfAccident >= :time")
    List<Accident> findAllByTimeOfAccidentAfter(@Param("time") LocalDateTime time);

    List<Accident> findAllByTripId(String id);
    List<Accident> findAllByIsVerified(boolean verified);
}
