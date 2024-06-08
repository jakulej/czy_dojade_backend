package ziwg.czy_dojade_backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ziwg.czy_dojade_backend.models.Route;
import ziwg.czy_dojade_backend.models.Trip;
import ziwg.czy_dojade_backend.services.interfaces.IRouteService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/route")
@CrossOrigin(origins = "*")
@Tag(name = "RouteController", description = "Endpoints for managing routes")
public class RouteController {

    private final IRouteService routeService;

    @Operation(
            summary = "Retrieve a single route by id. Throws NotFoundException if route with given id does not exist."
    )
    @GetMapping("/{id}")
    public ResponseEntity<Route> getById(@PathVariable String id) {
        return new ResponseEntity<>(routeService.getById(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Retrieve all routes"
    )
    @GetMapping
    public ResponseEntity<List<Route>> getAllRoutes() {
        return new ResponseEntity<>(routeService.getAllRoutes(), HttpStatus.OK);
    }

    @Operation(
            summary = "Retrieve all trips for route by passing a route id"
    )
    @GetMapping("/trip/{id}")
    public ResponseEntity<List<Trip>> getAllTripsForRoute(@PathVariable String id) {
        return new ResponseEntity<>(routeService.getTripsForRoute(id), HttpStatus.OK);
    }

}
