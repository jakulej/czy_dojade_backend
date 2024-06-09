package ziwg.czy_dojade_backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ziwg.czy_dojade_backend.models.Stop;
import ziwg.czy_dojade_backend.models.StopTime;
import ziwg.czy_dojade_backend.services.interfaces.IStopService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stop")
@CrossOrigin(origins = "*")
@Tag(name = "StopController", description = "Endpoints for interacting with stops")
public class StopController {

    private final IStopService stopService;

    @Operation(
            summary = "Retrieve a single stop by its id"
    )
    @GetMapping("/id/{id}")
    public ResponseEntity<Stop> getStopById(@PathVariable long id) {
        return new ResponseEntity<>(stopService.findById(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Retrieve stops by their name. Pass the name as a request body"
    )
    @GetMapping("/name")
    public ResponseEntity<List<Stop>> getStopByName(@RequestBody String name) {
        return new ResponseEntity<>(stopService.findAllByName(name), HttpStatus.OK);
    }

    @Operation(
            summary = "Retrieve a single stop by its code"
    )
    @GetMapping("/code/{code}")
    public ResponseEntity<Stop> getStopByCode(@PathVariable String code) {
        return new ResponseEntity<>(stopService.findByCode(code), HttpStatus.OK);
    }

    @Operation(
            summary = "Retrieve all stops"
    )
    @GetMapping
    public ResponseEntity<List<Stop>> getAllStops() {
        return new ResponseEntity<>(stopService.getAllStops(), HttpStatus.OK);
    }

    @Operation(
            summary = "Retrieve timetable for a stop (all related StopTime entities)"
    )
    @GetMapping("/timetable/{id}")
    public ResponseEntity<List<StopTime>> getTimetableForStop(@PathVariable long id) {
        return new ResponseEntity<>(stopService.getTimetableForStop(id), HttpStatus.OK);
    }


}
