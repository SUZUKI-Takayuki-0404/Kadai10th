package kadai10th.mapper;

import kadai10th.entity.AirportEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AirportMapper {

    @SelectProvider(type = SqlProvider.class, method = "findByCodeFromAirports")
    Optional<AirportEntity> findByCodeFromAirports(String airportCode);

    @SelectProvider(type = SqlProvider.class, method = "findByPrefFromAirports")
    List<AirportEntity> findByPrefFromAirports(String prefName);

    @SelectProvider(type = SqlProvider.class, method = "findAllFromAirports")
    List<AirportEntity> findAllFromAirports();

    @Insert("INSERT INTO airports (airportCode, airportName, prefCode) "
            + "VALUES (#{airportCode}, #{airportName}, #{prefCode})")
    void insertAirport(String airportCode, String airportName, String prefCode);

    @Update("UPDATE airports SET airportName = #{airportName}, prefCode = #{prefCode} "
            + "WHERE airportCode = #{airportCode}")
    boolean updateAirport(String airportCode, String airportName, String prefCode);

    @Delete("DELETE FROM airports WHERE airportCode = #{airportCode}")
    boolean deleteAirport(String airportCode);

    //Inner class because of inseparability
    class SqlProvider {
        public String findByCodeFromAirports(String airportCode) {
            return new SQL()
                    .SELECT("airports.*", "prefectures.prefName")
                    .FROM("airports")
                    .INNER_JOIN("prefectures on airports.prefCode = prefectures.prefCode")
                    .WHERE("airportCode = #{airportCode}")
                    .toString();
        }

        public String findByPrefFromAirports(String prefName) {
            return new SQL()
                    .SELECT("airports.*, prefectures.prefName")
                    .FROM("airports")
                    .INNER_JOIN("prefectures on airports.prefCode = prefectures.prefCode")
                    .WHERE("prefName = #{prefName}")
                    .toString();
        }

        public String findAllFromAirports() {
            return new SQL()
                    .SELECT("airports.*, prefectures.prefName")
                    .FROM("airports")
                    .INNER_JOIN("prefectures on airports.prefCode = prefectures.prefCode")
                    .toString();
        }
    }
}
