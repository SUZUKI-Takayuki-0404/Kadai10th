package exercise.kadai10th.service;

import exercise.kadai10th.entity.PrefectureEntity;
import exercise.kadai10th.exceptionhandler.CodeInUseException;
import exercise.kadai10th.exceptionhandler.NoResourceException;
import exercise.kadai10th.exceptionhandler.SameAsCurrentException;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;

public interface PrefectureService {

    /**
     * 指定の都道府県コードに対応する都道府県データを返す<br>
     * 都道府県データは都道府県コード表から取得する<br>
     * Returns {@code PrefectureEntity} corresponding to {@code prefCode}
     * Entity of the prefecture is searched from the prefecture code table
     *
     * @param prefCode the code which is unique value assigned to each prefecture
     * @return PrefectureEntity the entity consisting of the code and the name of prefecture corresponding to the code
     * @throws NoResourceException when the prefecture corresponding to the code does not exist
     */
    PrefectureEntity getPrefByCode(String prefCode);

    /**
     * 指定の都道府県名に対応する都道府県データを返す<br>
     * 都道府県データは都道府県コード表から取得する<br>
     * Returns [@code PrefectureEntity] corresponding to [@code prefName]
     * Entity of the prefecture is searched from the prefecture code table
     *
     * @param prefName the name of prefecture
     * @return PrefectureEntity the entity consisting of the code and the name of prefecture corresponding to the name
     * @throws NoResourceException when the prefecture corresponding to the name does not exist
     */
    PrefectureEntity getPrefByName(String prefName);

    /**
     * 都道府県データ全てをListとして返す<br>
     * 都道府県データは都道府県コード表から取得する<br>
     * Returns {@code List} of all prefecture data in the prefecture table
     * List of prefectures are got from the prefecture code table
     *
     * @return List<PrefectureEntity> the list of PrefectureEntity
     */
    List<PrefectureEntity> getAllPrefs();

    /**
     * 新規の都道府県コードで都道府県データを登録する<br>
     * Registers a new prefecture with a new code into the prefecture code table
     *
     * @param prefCode new code which is unique value assigned to the prefecture
     * @param prefName new name of prefecture
     * @return PrefectureEntity the entity consisting of the code and the name of the prefecture
     * @throws DuplicateKeyException when the code has already exists in the prefecture code table
     */
    PrefectureEntity createPref(String prefCode, String prefName);

    /**
     * 指定の都道府県コードに対応する都道府県データを更新する<br>
     * Updates a name of the prefecture corresponding to the code existing in the prefecture code table
     *
     * @param prefCode the code which is unique value assigned to each prefecture
     * @param prefName new name of prefecture
     * @throws NoResourceException    when the prefecture corresponding to the code does not exist
     * @throws SameAsCurrentException when new name equals to current one
     */
    void updatePref(String prefCode, String prefName);

    /**
     * 指定の都道府県コードに対応する都道府県データを削除する<br>
     * Deletes the prefecture from the prefecture code table
     * on condition that the code of the prefecture does not exist in the airport table
     *
     * @param prefCode the code which is unique value assigned to each prefecture
     * @throws NoResourceException when the prefecture corresponding to the code does not exist
     * @throws CodeInUseException  when the prefecture exists in the airport table
     */
    void deletePref(String prefCode);
}
