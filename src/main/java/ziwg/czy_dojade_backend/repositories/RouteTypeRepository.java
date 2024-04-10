package ziwg.czy_dojade_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ziwg.czy_dojade_backend.models.RouteType;

@RepositoryRestResource
public interface RouteTypeRepository extends JpaRepository<RouteType, Long>{
}
