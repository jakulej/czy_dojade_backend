package ziwg.czy_dojade_backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ziwg.czy_dojade_backend.models.Accident;
import ziwg.czy_dojade_backend.services.interfaces.IAccidentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accident")
@CrossOrigin(origins = "*")
@Tag(name = "AccidentController", description = "Endpoints for managing accidents")
public class AccidentController {

    private final IAccidentService accidentService;

    @Operation(
            summary = "Retrieve a single accident by id. " +
                    "Throws NotFoundException if accident with given id does not exist."
    )
    @GetMapping("/{id}")
    public ResponseEntity<Accident> getById(@PathVariable String id) {
        return new ResponseEntity<>(accidentService.getById(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Retrieve all accidents"
    )
    @GetMapping
    public ResponseEntity<List<Accident>> getAllAccidents() {
        return new ResponseEntity<>(accidentService.getAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "Retrieve all accidents by trip id"
    )
    @GetMapping("/trip/{id}")
    public ResponseEntity<List<Accident>> getAllByTripId(@PathVariable String id) {
        return new ResponseEntity<>(accidentService.getAllByTripId(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Retrieve all verified or non-verified accidents",
            description = "Pass a boolean value to path as a parameter. If the value is true," +
                    " returns all verified accidents, otherwise returns all non-verified accidents."
    )
    @GetMapping("/verified/{verified}")
    public ResponseEntity<List<Accident>> getAllByIsVerified(@PathVariable boolean verified) {
        return new ResponseEntity<>(accidentService.getAllByIsVerified(verified), HttpStatus.OK);
    }

}
