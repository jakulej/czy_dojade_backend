package ziwg.czy_dojade_backend.repositories;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ziwg.czy_dojade_backend.models.RouteStop;

@RepositoryRestResource
@Hidden
public interface RouteStopRepository extends JpaRepository<RouteStop, Long> {

}
