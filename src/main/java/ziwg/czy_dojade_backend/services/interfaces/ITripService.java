package ziwg.czy_dojade_backend.services.interfaces;

import ziwg.czy_dojade_backend.models.Stop;
import ziwg.czy_dojade_backend.models.Trip;
import ziwg.czy_dojade_backend.models.Vehicle;

import java.util.List;

public interface ITripService {
    boolean existsById(String id);
    List<Trip> getAll();
    Trip getById(String id);
    List<Trip> getAllByTripHeadsign(String tripHeadsign);
    Vehicle getVehicleForTrip(String id);
    //List<Trip> getRouteForTrip(String id);
    List<Stop> getAllStopsByTripId(String tripId);
}
