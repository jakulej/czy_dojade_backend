package ziwg.czy_dojade_backend.repositories;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ziwg.czy_dojade_backend.models.Vehicle;

import java.util.Optional;

@RepositoryRestResource
@Hidden
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    boolean existsById(String id);
    Optional<Vehicle> findById(String id);

}
