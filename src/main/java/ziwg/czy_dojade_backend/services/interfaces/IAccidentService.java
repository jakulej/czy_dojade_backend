package ziwg.czy_dojade_backend.services.interfaces;

import ziwg.czy_dojade_backend.models.Accident;

import java.util.List;

public interface IAccidentService {
    boolean existsById(String id);
    Accident getById(String id);
    List<Accident> getAll();
    List<Accident> getAllByTripId(String id);
    List<Accident> getAllByIsVerified(boolean verified);
}
