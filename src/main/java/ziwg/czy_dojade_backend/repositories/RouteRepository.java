package ziwg.czy_dojade_backend.repositories;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ziwg.czy_dojade_backend.models.Route;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
@Hidden
public interface RouteRepository extends JpaRepository<Route, String>{
    boolean existsById(String id);
    Optional<Route> findById(String id);
    Optional<Route> findByShortName(String routeName);
    List<Route> findByRouteTypeId(long id); // jak to działa? jak to jest użyte w tym DataImportService, bo nie rozumiem tam tego do końca? Nie powinno być przypadkiem: findAllByRouteTypeId?
}
