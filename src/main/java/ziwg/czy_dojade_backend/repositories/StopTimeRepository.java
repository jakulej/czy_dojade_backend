package ziwg.czy_dojade_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ziwg.czy_dojade_backend.models.StopTime;

import java.util.List;

@RepositoryRestResource
public interface StopTimeRepository extends JpaRepository<StopTime, Long> {
    List<StopTime> findByStopId(long id);

    List<StopTime> findByTripId(long id);

}
