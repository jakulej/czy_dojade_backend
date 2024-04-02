package ziwg.czy_dojade_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ziwg.czy_dojade_backend.models.Route;

import java.util.List;

@RepositoryRestResource
public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findByRouteTypeId(long id);
}
