package ziwg.czy_dojade_backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ziwg.czy_dojade_backend.models.Stop;
import ziwg.czy_dojade_backend.models.Trip;
import ziwg.czy_dojade_backend.models.Vehicle;
import ziwg.czy_dojade_backend.services.interfaces.ITripService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trip")
@CrossOrigin(origins = "*")
@Tag(name = "TripController", description = "Endpoints for managing trips")
public class TripController {

    private final ITripService tripService;

    @Operation(
            summary = "Retrieve all trips"
    )
    @GetMapping
    public ResponseEntity<List<Trip>> getAllTrips() {
        return new ResponseEntity<>(tripService.getAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "Retrieve a single trip by id. Throws NotFoundException if trip with given id does not exist."
    )
    @GetMapping("/{id}")
    public ResponseEntity<Trip> getById(@PathVariable String id) {
        return new ResponseEntity<>(tripService.getById(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Retrieve all trips with given tripHeadsign"
    )
    @GetMapping("/tripHeadsign")
    public ResponseEntity<List<Trip>> getAllByTripHeadsign(@RequestBody String tripHeadsign) {
        return new ResponseEntity<>(tripService.getAllByTripHeadsign(tripHeadsign), HttpStatus.OK);
    }

    @Operation(
            summary = "Retrieve vehicle for trip with given id. Throws NotFoundException if trip with given id does not exist."
    )
    @GetMapping("/vehicle/{id}")
    public ResponseEntity<Vehicle> getVehicleForTrip(@PathVariable String id) {
        return new ResponseEntity<>(tripService.getVehicleForTrip(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Retrieve all stops related to a trip. Throws NotFoundException if trip with given id does not exist."
    )
    @GetMapping("/stops/{id}")
    public ResponseEntity<List<Stop>> getStopsForTrip(@PathVariable String id) {
        return new ResponseEntity<>(tripService.getAllStopsByTripId(id), HttpStatus.OK);
    }

}
