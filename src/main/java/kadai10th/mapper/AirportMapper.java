package kadai10th.mapper;

import kadai10th.entity.AirportEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AirportMapper {

    @Select("SELECT airports.*, prefectures.prefName FROM airports INNER JOIN prefectures ON airports.prefCode = prefectures.prefCode WHERE airportCode = #{airportCode}")
    Optional<AirportEntity> findByCodeFromAirports(String airportCode);

    @Select("SELECT airports.*, prefectures.prefName FROM airports INNER JOIN prefectures ON airports.prefCode = prefectures.prefCode WHERE prefCode = #{prefCode}")
    List<AirportEntity> findByPrefFromAirports(String airportCode, String airportName, String prefCode);

    @Select("SELECT airports.*, prefectures.prefName FROM airports INNER JOIN prefectures ON airports.prefCode = prefectures.prefCode")
    List<AirportEntity> findAllFromAirports();

    @Insert("INSERT INTO airports (airportCode, airportName, prefCode) VALUES (#{airportCode}, #{airportName}, #{prefCode})")
    void insertAirport(String airportCode, String airportName, String prefCode);

    @Update("UPDATE airports SET airportName = #{airportName}, prefCode = #{prefCode} WHERE airportCode = #{airportCode}")
    boolean updateAirport(String airportCode, String airportName, String prefCode);

    @Delete("DELETE FROM airports WHERE airportCode = #{airportCode}")
    boolean deleteAirport(String airportCode);

}
