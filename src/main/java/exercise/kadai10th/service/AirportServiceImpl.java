package exercise.kadai10th.service;

import exercise.kadai10th.entity.AirportEntity;
import exercise.kadai10th.exceptionhandler.DuplicateCodeException;
import exercise.kadai10th.exceptionhandler.NoResourceException;
import exercise.kadai10th.exceptionhandler.SameAsCurrentException;
import exercise.kadai10th.mapper.AirportMapper;
import exercise.kadai10th.mapper.PrefectureMapper;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AirportServiceImpl implements AirportService {

    private final AirportMapper airportMapper;

    private final PrefectureMapper prefectureMapper;

    @Override
    public AirportEntity getAirportByCode(String airportCode) {
        return airportMapper
                .selectAirportByCode(airportCode)
                .orElseThrow(() -> new NoResourceException(airportCode + " : This code is not found"));
    }

    @Override
    public List<AirportEntity> getAirportsByPrefName(String prefName) {
        return airportMapper.selectAirportsByPrefName(prefName);
    }

    @Override
    public List<AirportEntity> getAllAirports() {
        return airportMapper.selectAllAirports();
    }

    @Override
    public AirportEntity createAirport(String airportCode, String airportName, String prefCode) {
        String prefName = prefectureMapper
                .selectPrefByCode(prefCode)
                .orElseThrow(() -> new NoResourceException(prefCode + " : This code is not found"))
                .getPrefName();

        try {
            airportMapper.insertAirport(airportCode, airportName, prefCode);
        } catch (DuplicateKeyException e) {
            throw new DuplicateCodeException(airportCode + " : This code will be duplicated", e);
        }
        return new AirportEntity(airportCode, airportName, prefCode, prefName);
    }

    @Override
    public void updateAirport(String airportCode, String airportName, String prefCode) {
        String airportCurrentName = airportMapper
                .selectAirportByCode(airportCode)
                .orElseThrow(() -> new NoResourceException(airportCode + " : This code is not found"))
                .getAirportName();

        if (airportCurrentName.equals(airportName)) {
            throw new SameAsCurrentException(airportName + " : Current name will be nothing updated");
        }

        if (prefectureMapper.selectPrefByCode(prefCode).isEmpty()) {
            throw new NoResourceException(prefCode + " : This code is not found");
        }

        airportMapper.updateAirport(airportCode, airportName, prefCode);
    }

    @Override
    public void deleteAirport(String airportCode) {
        if (airportMapper.selectAirportByCode(airportCode).isPresent()) {
            airportMapper.deleteAirport(airportCode);
        } else {
            throw new NoResourceException(airportCode + " : This code is not found");
        }
    }
}
