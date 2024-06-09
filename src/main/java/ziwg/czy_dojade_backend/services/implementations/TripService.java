package ziwg.czy_dojade_backend.services.implementations;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ziwg.czy_dojade_backend.exceptions.NotFoundException;
import ziwg.czy_dojade_backend.models.Stop;
import ziwg.czy_dojade_backend.models.StopTime;
import ziwg.czy_dojade_backend.models.Trip;
import ziwg.czy_dojade_backend.models.Vehicle;
import ziwg.czy_dojade_backend.repositories.TripRepository;
import ziwg.czy_dojade_backend.services.interfaces.ITripService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TripService implements ITripService {

    private final TripRepository tripRepository;

    @Override
    public boolean existsById(String id) {
        return tripRepository.existsById(id);
    }

    @Override
    public List<Trip> getAll() {
        return tripRepository.findAll();
    }

    @Override
    public Trip getById(String id) throws NotFoundException{
        return tripRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Trip with id " + id + " not found"));
    }

    @Override
    public List<Trip> getAllByTripHeadsign(String tripHeadsign) {
        return tripRepository.findAllByTripHeadsign(tripHeadsign);
    }

    @Override
    public Vehicle getVehicleForTrip(String id) throws NotFoundException{
        Trip trip = tripRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Vehicle for a trip with id " + id + " not found"));
        return trip.getVehicle();
    }

    public List<Stop> getAllStopsByTripId(String tripId) {
        Trip trip = tripRepository
                .findById(tripId)
                .orElseThrow(() -> new NotFoundException("Trip with id " + tripId + " not found"));

        Set<Stop> stops = new HashSet<>();
        for (StopTime stopTime : trip.getStopTimes()) {
            stops.add(stopTime.getStop());
        }
        return new ArrayList<>(stops);
    }
}
