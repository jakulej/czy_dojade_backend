package ziwg.czy_dojade_backend.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ziwg.czy_dojade_backend.exceptions.NotFoundException;
import ziwg.czy_dojade_backend.models.Stop;
import ziwg.czy_dojade_backend.models.StopTime;
import ziwg.czy_dojade_backend.repositories.StopRepository;
import ziwg.czy_dojade_backend.repositories.StopTimeRepository;
import ziwg.czy_dojade_backend.services.interfaces.IStopService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StopService implements IStopService {

    private final StopRepository stopRepository;
    private final StopTimeRepository stopTimeRepository;


    @Override
    public boolean existsById(long id) {
        return stopRepository.existsById(id);
    }

    @Override
    public Stop findById(long id) {
        return stopRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Stop with id " + id + " not found"));
    }

    @Override
    public List<Stop> findAllByName(String name) {
        return stopRepository
                .findAllByName(name);
    }

    @Override
    public Stop findByCode(String code) {
        return stopRepository
                .findByCode(code)
                .orElseThrow(() -> new NotFoundException("Stop with code " + code + " not found"));
    }

    @Override
    public List<Stop> getAllStops() {
        return stopRepository.findAll();
    }

    @Override
    public List<StopTime> getTimetableForStop(long stopId) {
        return stopTimeRepository.findAllByStopId(stopId);
    }

}
