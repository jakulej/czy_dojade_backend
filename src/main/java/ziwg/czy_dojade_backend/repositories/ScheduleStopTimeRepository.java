package ziwg.czy_dojade_backend.repositories;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ziwg.czy_dojade_backend.models.ScheduleStopTime;
import ziwg.czy_dojade_backend.models.Stop;
import ziwg.czy_dojade_backend.models.Trip;

import java.util.List;

@RepositoryRestResource
@Hidden
public interface ScheduleStopTimeRepository extends JpaRepository<ScheduleStopTime, Long> {
    List<ScheduleStopTime> findAllByStopId(long id);
}
