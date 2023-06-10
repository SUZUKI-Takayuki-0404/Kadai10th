package exercise.kadai10th.mapper;

import exercise.kadai10th.entity.AirportEntity;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.tuple;

@DBRider
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class AirportMapperTest {

    @Autowired
    private AirportMapper airportMapper;

    @Nested
    class SelectAirportByCodeTest {
        @Test
        @DataSet(value = "datasets/airports.yml, datasets/prefectures.yml")
        @DisplayName("""
                Should return a corresponding airport by a code when exists
                空港があるときは取得できること
                """)
        void workNormally() {
            assertThat(airportMapper.selectAirportByCode("CTS"))
                    .get()
                    .isEqualTo(new AirportEntity("CTS", "新千歳空港", "01", "北海道"));
        }

        @Test
        @DataSet(value = "datasets/airports.yml, datasets/prefectures.yml")
        @DisplayName("""
                Should return empty when no corresponding airport exists
                空港が無いときは空として返すこと
                """)
        void returnEmptyWhenNoAirport() {
            assertThat(airportMapper.selectAirportByCode("WKJ")).isEmpty();
        }
    }

    @Nested
    class SelectAirportsByPrefNameTest {
        @Test
        @DataSet(value = "datasets/airports.yml, datasets/prefectures.yml")
        @DisplayName("""
                Should return corresponding airports by a prefecture name when exists
                その都道府県に空港があるときは、全て取得できること
                """)
        void workNormally() {
            assertThat(airportMapper.selectAirportsByPrefName("北海道"))
                    .hasSize(2)
                    .extracting("airportCode", "airportName", "prefCode", "prefName")
                    .contains(
                            tuple("CTS", "新千歳空港", "01", "北海道"),
                            tuple("HKD", "函館空港", "01", "北海道")
                    );
        }

        @Test
        @DataSet(value = "datasets/airports.yml, datasets/prefectures.yml")
        @DisplayName("""
                Should return empty when no corresponding airport exists
                空港がないときは空として返すこと
                """)
        void returnEmptyWhenNoAirport() {
            assertThat(airportMapper.selectAirportsByPrefName("埼玉県")).isEmpty();
        }

        @Test
        @DataSet(value = "datasets/airports.yml, datasets/prefectures.yml")
        @DisplayName("""
                Should return empty when prefecture name is incorrect
                都道府県名が誤っているときは空として返すこと
                """)
        void returnEmptyWhenIncorrectPref() {
            assertThat(airportMapper.selectAirportsByPrefName("北海道県")).isEmpty();
        }
    }

    @Nested
    class SelectAllAirportsTest {
        @Test
        @DataSet(value = "datasets/airports.yml, datasets/prefectures.yml")
        @DisplayName("""
                Should return a list of all airports when exist
                登録済みの空港がある場合は全て取得できること
                """)
        void workNormally() {
            assertThat(airportMapper.selectAllAirports())
                    .hasSize(5)
                    .extracting("airportCode", "airportName", "prefCode", "prefName")
                    .contains(
                            tuple("CTS", "新千歳空港", "01", "北海道"),
                            tuple("HKD", "函館空港", "01", "北海道"),
                            tuple("AOJ", "青森空港", "02", "青森県"),
                            tuple("HNA", "花巻空港", "03", "岩手県"),
                            tuple("SDJ", "仙台空港", "04", "宮城県")
                    );
        }

        @Test
        @DataSet(value = "datasets/airport-empty.yml, datasets/prefectures.yml")
        @DisplayName("""
                Should Should return empty when no prefecture exists
                空港がないときは空として返すこと
                """)
        void returnEmptyWhenNoAirport() {
            assertThat(airportMapper.selectAllAirports()).isEmpty();
        }
    }

    @Nested
    class InsertAirportTest {
        @Test
        @DataSet(value = "datasets/airports.yml")
        @ExpectedDataSet(value = "datasets/airport-insert.yml", orderBy = "airportCode")
        @DisplayName("""
                Should add an airport when its code is unique
                空港コードが既存のものと重複しない場合は登録できること
                """)
        void workNormally() {
            airportMapper.insertAirport("OKA", "那覇空港", "47");
        }

        @Test
        @DataSet(value = "datasets/airports.yml")
        @DisplayName("""
                Should throw DuplicateCodeException when a code already exists
                空港コードが既存のものと重複する場合は、DuplicateKeyExceptionをスローすること
                """)
        void throwWhenCodeDuplicates() {
            assertThatExceptionOfType(DuplicateKeyException.class)
                    .isThrownBy(() -> airportMapper.insertAirport("HNA", "花巻空港", "03"));
        }
    }

    @Nested
    class UpdateAirportTest {
        @Test
        @DataSet(value = "datasets/airports.yml")
        @ExpectedDataSet(value = "datasets/airport-update.yml", orderBy = "airportCode")
        @DisplayName("""
                Should rename an airport when exists
                空港がある場合は空港名を更新すること
                """)
        void workNormally() {
            airportMapper.updateAirport("SDJ", "仙台国際空港", "04");
        }

        @Test
        @DataSet(value = "datasets/airports.yml")
        @ExpectedDataSet(value = "datasets/airports.yml", orderBy = "airportCode")
        @DisplayName("""
                Should do nothing when no corresponding airport exists
                空港が無い場合は何もしないこと
                """)
        void doNothingWhenNoAirport() {
            airportMapper.updateAirport("NRT", "新東京国際空港", "13");
        }
    }

    @Nested
    class DeleteAirportTest {
        @Test
        @DataSet(value = "datasets/airports.yml")
        @ExpectedDataSet(value = "datasets/airport-delete.yml", orderBy = "airportCode")
        @DisplayName("""
                Should rename an airport when exists
                空港がある場合は削除できること
                """)
        void workNormally() {
            airportMapper.deleteAirport("SDJ");
        }

        @Test
        @DataSet(value = "datasets/airports.yml")
        @ExpectedDataSet(value = "datasets/airports.yml", orderBy = "airportCode")
        @DisplayName("""
                Should do nothing when no corresponding airport exists
                空港が無い場合は何もしないこと
                """)
        void doNothingWhenNoAirport() {
            airportMapper.deleteAirport("NKM");
        }
    }

}
