package ziwg.czy_dojade_backend.services.interfaces;

import ziwg.czy_dojade_backend.models.Vehicle;

import java.util.List;

public interface IVehicleService {
    boolean existsById(String id);
    Vehicle findById(String id);
    List<Vehicle> getAllVehicles();
}
