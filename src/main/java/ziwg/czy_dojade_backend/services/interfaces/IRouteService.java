package ziwg.czy_dojade_backend.services.interfaces;

import ziwg.czy_dojade_backend.models.Route;
import ziwg.czy_dojade_backend.models.Trip;

import java.util.List;

public interface IRouteService {
    boolean existsById(String id);
    Route getById(String id);
    List<Route> getAllRoutes();
    List<Trip> getTripsForRoute(String routeId);
}
