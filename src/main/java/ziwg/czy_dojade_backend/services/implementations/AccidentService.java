package ziwg.czy_dojade_backend.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ziwg.czy_dojade_backend.exceptions.NotFoundException;
import ziwg.czy_dojade_backend.models.Accident;
import ziwg.czy_dojade_backend.repositories.AccidentRepository;
import ziwg.czy_dojade_backend.services.interfaces.IAccidentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccidentService implements IAccidentService {

    private final AccidentRepository accidentRepository;

    @Override
    public boolean existsById(String id) {
        return accidentRepository.existsById(id);
    }

    @Override
    public Accident getById(String id) {
        return accidentRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Accident with id " + id + " not found"));
    }

    @Override
    public List<Accident> getAll() {
        return accidentRepository.findAll();
    }

    @Override
    public List<Accident> getAllByTripId(String id) {
        return accidentRepository.findAllByTripId(id);
    }



    @Override
    public List<Accident> getAllByIsVerified(boolean verified) {
        return accidentRepository.findAllByIsVerified(verified);
    }
}
