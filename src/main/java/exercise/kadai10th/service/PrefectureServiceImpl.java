package exercise.kadai10th.service;

import exercise.kadai10th.entity.PrefectureEntity;
import exercise.kadai10th.exceptionhandler.CodeInUseException;
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
public class PrefectureServiceImpl implements PrefectureService {

    private final PrefectureMapper prefectureMapper;

    private final AirportMapper airportMapper;

    @Override
    public PrefectureEntity getPrefByCode(String prefCode) {
        return prefectureMapper
                .findByCodeFromPrefs(prefCode)
                .orElseThrow(() -> new NoResourceException(prefCode + " : This code is not found"));
    }

    @Override
    public PrefectureEntity getPrefByName(String prefName) {
        return prefectureMapper
                .findByNameFromPrefs(prefName)
                .orElseThrow(() -> new NoResourceException(prefName + " : This name is not found"));
    }

    @Override
    public List<PrefectureEntity> getAllPrefs() {
        return prefectureMapper.findAllFromPrefs();
    }

    @Override
    public PrefectureEntity createPref(String prefCode, String prefName) {
        try {
            prefectureMapper.insertPref(prefCode, prefName);
        } catch (DuplicateKeyException e) {
            throw new DuplicateCodeException(prefCode + " : This code will be duplicated");
        }
        return new PrefectureEntity(prefCode, prefName);
    }

    @Override
    public void updatePref(String prefCode, String prefName) {
        PrefectureEntity prefectureEntity = prefectureMapper
                .findByCodeFromPrefs(prefCode)
                .orElseThrow(() -> new NoResourceException(prefCode + " : This code is not found"));

        if (prefectureEntity.getPrefName().equals(prefName)) {
            throw new SameAsCurrentException(prefName + " : Current name will be nothing updated");
        } else {
            prefectureMapper.updatePref(prefCode, prefName);
        }
    }

    @Override
    public void deletePref(String prefCode) {
        String prefName = prefectureMapper
                .findByCodeFromPrefs(prefCode)
                .orElseThrow(() -> new NoResourceException(prefCode + " : This code is not found"))
                .getPrefName();

        if (airportMapper.findByPrefFromAirports(prefName).isEmpty()) {
            prefectureMapper.deletePref(prefCode);
        } else {
            throw new CodeInUseException(prefCode + " : This code is in use");
        }
    }
}
