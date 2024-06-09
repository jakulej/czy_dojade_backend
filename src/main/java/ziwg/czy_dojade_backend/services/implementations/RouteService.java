package ziwg.czy_dojade_backend.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ziwg.czy_dojade_backend.exceptions.NotFoundException;
import ziwg.czy_dojade_backend.models.Route;
import ziwg.czy_dojade_backend.models.Trip;
import ziwg.czy_dojade_backend.repositories.RouteRepository;
import ziwg.czy_dojade_backend.services.interfaces.IRouteService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteService implements IRouteService {

    private final RouteRepository routeRepository;

    @Override
    public boolean existsById(String id) {
        return routeRepository.existsById(id);
    }

    @Override
    public Route getById(String id) {
        return routeRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Route with id " + id + " not found"));
    }

    @Override
    public List<Route> getAllRoutes() {
        return routeRepository
                .findAll();
    }

    @Override
    public List<Trip> getTripsForRoute(String routeId) {
        Route route = routeRepository
                .findById(routeId)
                .orElseThrow(() -> new NotFoundException("Route with id " + routeId + " not found"));
        return route.getTrips();
    }
}
