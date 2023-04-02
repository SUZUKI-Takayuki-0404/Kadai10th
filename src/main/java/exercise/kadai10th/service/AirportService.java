package exercise.kadai10th.service;

import exercise.kadai10th.entity.AirportEntity;
import exercise.kadai10th.exceptionhandler.NoResourceException;
import exercise.kadai10th.exceptionhandler.SameAsCurrentException;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;

public interface AirportService {

    /**
     * 指定の空港コードに対応する空港データを返す<br>
     * 空港データは空港コード表と都道府県データを結合して取得する<br>
     * Returns {@code AirportEntity} corresponding to {@code airportCode}
     * Entity of the airport is searched from the airport code table inner joined with the prefecture code table
     *
     * @param airportCode the code which is unique value assigned to each airport
     * @return AirportEntity the entity consisting of the code, the name, located prefecture code, and prefecture name
     * @throws NoResourceException when the airport corresponding to the code does not exist
     */
    AirportEntity getAirport(String airportCode);

    /**
     * 指定の都道府県名に対応する空港データ全てをListとして返す<br>
     * 空港データは空港コード表と都道府県データを結合して取得する<br>
     * Returns {@code List} of the airport data corresponding to located prefecture
     * List of airports are got from the airport code table inner joined with the prefecture code table
     *
     * @param prefName the name of prefecture listed in the prefecture table
     * @return List<AirportEntity> the list of AirportEntity
     */
    List<AirportEntity> getAirportsByPref(String prefName);

    /**
     * 空港データ全てをListとして返す<br>
     * 空港データは空港コード表と都道府県データを結合して取得する<br>
     * Returns {@code List} of all airport data
     * List of airports are got from the airport code table inner joined with the prefecture code table
     *
     * @return List<PrefectureEntity> the list of AirportEntity
     */
    List<AirportEntity> getAllAirports();

    /**
     * 新規の空港データを登録する<br>
     * Registers a new airport with a new code into the airport code table
     *
     * @param airportCode new code which is unique value assigned to the airport
     * @param airportName new name of airport
     * @param prefCode    the name of prefecture existing in the prefecture code table
     * @return AirportEntity the entity consisting of the code, the name, located prefecture code, and prefecture name
     * @throws DuplicateKeyException when the code has already exists in the airport code table
     * @throws NoResourceException   when the prefecture corresponding to the code does not exist
     */
    AirportEntity createAirport(String airportCode, String airportName, String prefCode);

    /**
     * 指定の空港コードに対応する空港データを更新する<br>
     * Updates a name of the airport with or without the code of located prefecture corresponding to
     * the code existing in the airport code table
     *
     * @param airportCode new code which is unique value assigned to each airport
     * @param airportName new name of airport
     * @param prefCode    the name of prefecture existing in the prefecture code table
     * @throws NoResourceException    when the airport or the prefecture corresponding to the code does not exist
     * @throws SameAsCurrentException when new name equals to current one
     */
    void updateAirport(String airportCode, String airportName, String prefCode);

    /**
     * 指定の空港コードに対応する空港データを削除する<br>
     * Deletes the airport from the airport code table
     *
     * @param airportCode the code which is unique value assigned to each airport
     * @throws NoResourceException when the airport corresponding to the code does not exist
     */
    void deleteAirport(String airportCode);
}
