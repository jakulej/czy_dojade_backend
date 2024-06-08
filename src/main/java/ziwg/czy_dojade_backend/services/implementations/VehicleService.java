package ziwg.czy_dojade_backend.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ziwg.czy_dojade_backend.exceptions.NotFoundException;
import ziwg.czy_dojade_backend.models.Vehicle;
import ziwg.czy_dojade_backend.repositories.VehicleRepository;
import ziwg.czy_dojade_backend.services.interfaces.IVehicleService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService implements IVehicleService {

    private final VehicleRepository vehicleRepository;

    @Override
    public boolean existsById(String id) {
        return vehicleRepository.existsById(id);
    }

    @Override
    public Vehicle findById(String id) {
        return vehicleRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Vehicle with id " + id + " not found"));
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }
}
