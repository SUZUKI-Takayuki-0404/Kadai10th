package exercise.kadai10th.service;

import exercise.kadai10th.entity.AirportEntity;
import exercise.kadai10th.mapper.AirportMapper;

import java.util.List;

public interface AirportService {
    
    AirportEntity getAirport(String airportCode);

    List<AirportEntity> getAirportsByPref(String prefCode);

    List<AirportEntity> getAllAirports();

    AirportEntity createAirport(String airportCode, String airportName, String prefCode);

    void updateAirport(String airportCode, String airportName, String prefCode);

    void deleteAirport(String airportCode);
}
