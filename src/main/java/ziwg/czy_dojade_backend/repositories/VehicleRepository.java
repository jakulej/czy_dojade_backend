package ziwg.czy_dojade_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ziwg.czy_dojade_backend.models.Vehicle;

@RepositoryRestResource
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
