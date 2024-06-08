package ziwg.czy_dojade_backend.services.interfaces;

import ziwg.czy_dojade_backend.models.Stop;
import ziwg.czy_dojade_backend.models.StopTime;

import java.util.List;

public interface IStopService {
    boolean existsById(long id);
    Stop findById(long id);
    List<Stop> findAllByName(String name);
    Stop findByCode(String code);
    List<Stop> getAllStops();
    List<StopTime> getTimetableForStop(long stopId);
}
