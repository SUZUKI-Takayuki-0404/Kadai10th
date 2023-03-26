package exercise.kadai10th.service;

import exercise.kadai10th.entity.PrefectureEntity;

import java.util.List;

public interface PrefectureService {

    PrefectureEntity getPrefByCode(String prefCode);

    PrefectureEntity getPrefByName(String prefName);

    List<PrefectureEntity> getAllPrefs();

    PrefectureEntity createPref(String prefCode, String prefName);

    void updatePref(String prefCode, String prefName);

    void deletePref(String prefCode);
}
