package exercise.kadai10th.service;

import exercise.kadai10th.entity.AirportEntity;
import exercise.kadai10th.exceptionhandler.NoResourceException;
import exercise.kadai10th.exceptionhandler.SameAsCurrentException;
import exercise.kadai10th.mapper.AirportMapper;
import exercise.kadai10th.mapper.PrefectureMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AirportServiceImpl implements AirportService {

    @Autowired
    AirportMapper airportMapper;

    @Autowired
    PrefectureMapper prefectureMapper;

    @Override
    public AirportEntity getAirport(String airportCode) {
        return airportMapper
                .findByCodeFromAirports(airportCode)
                .orElseThrow(() -> new NoResourceException(airportCode));
    }

    @Override
    public List<AirportEntity> getAirportsByPref(String prefName) {
        return airportMapper.findByPrefFromAirports(prefName);
    }

    @Override
    public List<AirportEntity> getAllAirports() {
        return airportMapper.findAllFromAirports();
    }

    @Override
    public AirportEntity createAirport(String airportCode, String airportName, String prefCode) {
        String prefName
                = prefectureMapper
                .findByCodeFromPrefs(prefCode)
                .orElseThrow(() -> new NoResourceException(prefCode))
                .getPrefName();

        try {
            airportMapper.insertAirport(airportCode, airportName, prefCode);
        } catch (DuplicateKeyException e) {
            throw new DuplicateKeyException(airportCode + " code duplicated");
        }
        return new AirportEntity(airportCode, airportName, prefCode, prefName);
    }

    @Override
    public void updateAirport(String airportCode, String airportName, String prefCode) {
        Optional<AirportEntity> optionalAirportEntity = airportMapper.findByCodeFromAirports(airportCode);

        if (optionalAirportEntity.isEmpty() || prefectureMapper.findByCodeFromPrefs(prefCode).isEmpty()) {
            throw new NoResourceException(prefCode);
        }
        if (optionalAirportEntity.get().getAirportName().equals(airportName)) {
            throw new SameAsCurrentException(airportName);
        }
        airportMapper.updateAirport(airportCode, airportName, prefCode);
    }

    @Override
    public void deleteAirport(String airportCode) {
        if (airportMapper.findByCodeFromAirports(airportCode).isPresent()) {
            airportMapper.deleteAirport(airportCode);
        } else {
            throw new NoResourceException(airportCode);
        }
    }
}
