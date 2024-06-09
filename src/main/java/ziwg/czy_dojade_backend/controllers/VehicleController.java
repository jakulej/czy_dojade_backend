package ziwg.czy_dojade_backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ziwg.czy_dojade_backend.models.Vehicle;
import ziwg.czy_dojade_backend.services.interfaces.IVehicleService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vehicle")
@CrossOrigin(origins = "*")
@Tag(name = "VehicleController", description = "Endpoints for locating vehicles on the map")
public class VehicleController {

    private final IVehicleService vehicleService;

    @Operation(
            summary = "Get a single vehicle by its id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable String id) {
        return new ResponseEntity<>(vehicleService.findById(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Get all vehicles"
    )
    @GetMapping()
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return new ResponseEntity<>(vehicleService.getAllVehicles(), HttpStatus.OK);
    }

}
