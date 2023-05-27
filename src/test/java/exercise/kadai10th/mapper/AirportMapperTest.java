package exercise.kadai10th.mapper;

import exercise.kadai10th.entity.AirportEntity;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DuplicateKeyException;

import static com.github.database.rider.core.api.configuration.Orthography.LOWERCASE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.tuple;

@DBRider
@DBUnit(caseInsensitiveStrategy = LOWERCASE, schema = "airport_database",
        url = "jdbc:mysql://localhost:3307/airport_database",
        user = "user", password = "password")
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AirportMapperTest {

    @Autowired
    AirportMapper airportMapper;

    @Test
    @DataSet(value = "datasets/airports.yml, datasets/prefectures.yml", transactional = true)
    @DisplayName("指定の空港コードが存在するときは、対応する空港EntityをOptionalとして返すこと")
    void selectAirportByCode() {
        assertThat(airportMapper.selectAirportByCode("CTS"))
                .get()
                .isEqualTo(new AirportEntity("CTS", "新千歳空港", "01", "北海道"));
    }

    @Test
    @DataSet(value = "datasets/airports.yml, datasets/prefectures.yml", transactional = true)
    @DisplayName("指定の空港コードが無いときは、空のOptionalを返すこと")
    void selectAirportByCodeToReturnEmpty() {
        assertThat(airportMapper.selectAirportByCode("WKJ")).isEmpty();
    }

    @Test
    @DataSet(value = "datasets/airports.yml, datasets/prefectures.yml", transactional = true)
    @DisplayName("指定の都道府県に空港が存在するときは、対応する空港Entity全てをListとして返すこと")
    void selectAirportsByPrefName() {
        assertThat(airportMapper.selectAirportsByPrefName("北海道"))
                .hasSize(2)
                .extracting("airportCode", "airportName", "prefCode", "prefName")
                .contains(
                        tuple("CTS", "新千歳空港", "01", "北海道"),
                        tuple("HKD", "函館空港", "01", "北海道")
                );
    }

    @Test
    @DataSet(value = "datasets/airports.yml, datasets/prefectures.yml", transactional = true)
    @DisplayName("指定の都道府県に空港が無いときは、空のListを返すこと")
    void selectAirportsByPrefNameToReturnEmptyIfNotExists() {
        assertThat(airportMapper.selectAirportsByPrefName("埼玉県")).isEmpty();
    }

    @Test
    @DataSet(value = "datasets/airports.yml, datasets/prefectures.yml", transactional = true)
    @DisplayName("誤った都道府県名を指定したときは、空のListを返すこと")
    void selectAirportsByPrefNameToReturnEmptyIfIncorrect() {
        assertThat(airportMapper.selectAirportsByPrefName("北海道県")).isEmpty();
    }

    @Test
    @DataSet(value = "datasets/airports.yml, datasets/prefectures.yml", transactional = true)
    @DisplayName("存在する全ての空港EntityをListとして返すこと")
    void selectAllAirports() {
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
    @DataSet(value = "datasets/airport-empty.yml, datasets/prefectures.yml", transactional = true)
    @DisplayName("空港が存在しないときは空のListを返すこと")
    void selectAllAirportsToReturnEmpty() {
        assertThat(airportMapper.selectAllAirports()).isEmpty();
    }

    @Test
    @DataSet(value = "datasets/airports.yml", transactional = true)
    @ExpectedDataSet(value = "datasets/airport-insert.yml", orderBy = "airportCode")
    @DisplayName("指定の空港コードが既存のものと重複しない場合は、併せて指定した空港名および都道府県コードと共にデータ登録すること")
    void insertAirport() {
        airportMapper.insertAirport("OKA", "那覇空港", "47");
    }

    @Test
    @DataSet(value = "datasets/airports.yml", transactional = true)
    @DisplayName("指定の空港コードが既存のものと重複する場合は、DuplicateKeyExceptionをスローすること")
    void insertAirportToThrowException() {
        assertThatExceptionOfType(DuplicateKeyException.class)
                .isThrownBy(() -> airportMapper.insertAirport("HNA", "花巻空港", "03"));
    }

    @Test
    @DataSet(value = "datasets/airports.yml", transactional = true)
    @ExpectedDataSet(value = "datasets/airport-update.yml", orderBy = "airportCode")
    @DisplayName("指定の空港コードが存在する場合は、併せて指定した空港名および都道府県コードでデータ更新すること")
    void updateAirport() {
        airportMapper.updateAirport("SDJ", "仙台国際空港", "04");
    }

    @Test
    @DataSet(value = "datasets/airports.yml", transactional = true)
    @ExpectedDataSet(value = "datasets/airports.yml", orderBy = "airportCode")
    @DisplayName("指定の空港コードが存在しない場合は何も変更しないこと")
    void updateAirportToDoNothing() {
        airportMapper.updateAirport("NRT", "新東京国際空港", "13");
    }

    @Test
    @DataSet(value = "datasets/airports.yml", transactional = true)
    @ExpectedDataSet(value = "datasets/airport-delete.yml", orderBy = "airportCode")
    @DisplayName("指定の空港コードが存在する場合は、対応する空港データを削除すること")
    void deleteAirport() {
        airportMapper.deleteAirport("SDJ");
    }

    @Test
    @DataSet(value = "datasets/airports.yml", transactional = true)
    @ExpectedDataSet(value = "datasets/airports.yml", orderBy = "airportCode")
    @DisplayName("指定の空港コードが存在しない場合は何も変更しないこと")
    void deleteAirportToDoNothing() {
        airportMapper.deleteAirport("NKM");
    }
}
