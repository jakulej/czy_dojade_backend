package ziwg.czy_dojade_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ziwg.czy_dojade_backend.models.Route;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface RouteRepository extends JpaRepository<Route, Long>{
    boolean existsById(Long routeId);
    Optional<Route> findById(Long routeId);
    Optional<Route> findByShortName(String routeName);
}
