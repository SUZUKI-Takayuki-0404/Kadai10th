package exercise.kadai10th.service;

import exercise.kadai10th.entity.PrefectureEntity;
import exercise.kadai10th.exceptionhandler.CodeInUseException;
import exercise.kadai10th.exceptionhandler.NoResourceException;
import exercise.kadai10th.exceptionhandler.SameAsCurrentException;
import exercise.kadai10th.mapper.AirportMapper;
import exercise.kadai10th.mapper.PrefectureMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrefectureServiceImpl implements PrefectureService {

    @Autowired
    PrefectureMapper prefectureMapper;

    @Autowired
    AirportMapper airportMapper;

    @Override
    public PrefectureEntity getPrefByCode(String prefCode) {
        return prefectureMapper
                .findByCodeFromPrefs(prefCode)
                .orElseThrow(() -> new NoResourceException(prefCode));
    }

    @Override
    public PrefectureEntity getPrefByName(String prefName) {
        return prefectureMapper
                .findByNameFromPrefs(prefName)
                .orElseThrow(() -> new NoResourceException(prefName));
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
            throw new DuplicateKeyException(prefCode + " code duplicated");
        }
        return new PrefectureEntity(prefCode, prefName);
    }

    @Override
    public void updatePref(String prefCode, String prefName) {
        PrefectureEntity prefectureEntity
                = prefectureMapper
                .findByCodeFromPrefs(prefCode)
                .orElseThrow(() -> new NoResourceException(prefCode));

        if (prefectureEntity.getPrefName().equals(prefName)) {
            throw new SameAsCurrentException(prefName);
        } else {
            prefectureMapper.updatePref(prefCode, prefName);
        }
    }

    @Override
    public void deletePref(String prefCode) {
        String prefName
                = prefectureMapper
                .findByCodeFromPrefs(prefCode)
                .orElseThrow(() -> new NoResourceException(prefCode))
                .getPrefName();

        if (airportMapper.findByPrefFromAirports(prefName).isEmpty()) {
            prefectureMapper.deletePref(prefCode);
        } else {
            throw new CodeInUseException(prefCode);
        }
    }
}
