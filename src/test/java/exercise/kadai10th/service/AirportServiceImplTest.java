package exercise.kadai10th.service;

import exercise.kadai10th.entity.AirportEntity;
import exercise.kadai10th.entity.PrefectureEntity;
import exercise.kadai10th.exceptionhandler.NoResourceException;
import exercise.kadai10th.exceptionhandler.SameAsCurrentException;
import exercise.kadai10th.mapper.AirportMapper;
import exercise.kadai10th.mapper.PrefectureMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class AirportServiceImplTest {

    @InjectMocks
    AirportServiceImpl airportServiceImpl;

    @Mock
    AirportMapper airportMapper;

    @Mock
    PrefectureMapper prefectureMapper;

    @Test
    @DisplayName("指定の空港コードに対応する空港のEntityがある場合はそれを返すこと")
    void getAirportTest1() {
        doReturn(Optional.of(new AirportEntity("CTS", "新千歳空港", "01", "北海道")))
                .when(airportMapper)
                .findByCodeFromAirports("CTS");

        assertThat(airportServiceImpl.getAirport("CTS"))
                .isEqualTo(new AirportEntity("CTS", "新千歳空港", "01", "北海道"));

        verify(airportMapper, times(1)).findByCodeFromAirports("CTS");
    }

    @Test
    @DisplayName("指定の空港コードに対応する空港のEntityが存在しない場合はNoResourceExceptionをスローすること")
    void getAirportTest2() {
        doReturn(Optional.empty())
                .when(airportMapper)
                .findByCodeFromAirports("WKJ");

        assertThatExceptionOfType(NoResourceException.class)
                .isThrownBy(() -> airportServiceImpl.getAirport("WKJ"));
    }

    @Test
    @DisplayName("指定の都道府県名に対応する空港のEntityがある場合はその全てをListで返すこと")
    void getAirportsByPrefTest1() {
        doReturn(List.of(
                new AirportEntity("CTS", "新千歳空港", "01", "北海道"),
                new AirportEntity("HKD", "函館空港", "01", "北海道")))
                .when(airportMapper)
                .findByPrefFromAirports("北海道");

        assertThat(airportServiceImpl.getAirportsByPref("北海道"))
                .hasSize(2)
                .extracting("airportCode", "airportName", "prefCode", "prefName")
                .contains(
                        tuple("CTS", "新千歳空港", "01", "北海道"),
                        tuple("HKD", "函館空港", "01", "北海道")
                );

        verify(airportMapper, times(1)).findByPrefFromAirports("北海道");
    }

    @Test
    @DisplayName("指定の都道府県コードに対応する空港のEntityが無い場合は空のListを返すこと")
    void getAirportsByPrefTest2() {
        doReturn(List.of())
                .when(airportMapper)
                .findByPrefFromAirports("埼玉県");

        assertThat(airportServiceImpl.getAirportsByPref("埼玉県")).isEmpty();
    }

    @Test
    @DisplayName("空港のEntityが存在する場合はその全てをListで返すこと")
    void getAllAirportsTest1() {
        doReturn(List.of(
                new AirportEntity("CTS", "新千歳空港", "01", "北海道"),
                new AirportEntity("HKD", "函館空港", "01", "北海道"),
                new AirportEntity("AOJ", "青森空港", "02", "青森県"),
                new AirportEntity("HNA", "花巻空港", "03", "岩手県"),
                new AirportEntity("SDJ", "仙台空港", "04", "宮城県")))
                .when(airportMapper)
                .findAllFromAirports();

        assertThat(airportServiceImpl.getAllAirports())
                .hasSize(5)
                .extracting("airportCode", "airportName", "prefCode", "prefName")
                .contains(
                        tuple("CTS", "新千歳空港", "01", "北海道"),
                        tuple("HKD", "函館空港", "01", "北海道"),
                        tuple("AOJ", "青森空港", "02", "青森県"),
                        tuple("HNA", "花巻空港", "03", "岩手県"),
                        tuple("SDJ", "仙台空港", "04", "宮城県")
                );

        verify(airportMapper, times(1)).findAllFromAirports();
    }

    @Test
    @DisplayName("空港のEntityが無い場合は空のListを返すこと")
    void getAllAirportsTest2() {
        doReturn(List.of())
                .when(airportMapper)
                .findAllFromAirports();

        assertThat(airportServiceImpl.getAllAirports()).isEmpty();
    }

    @Test
    @DisplayName("指定の空港コードが既存のものと重複せず、かつ指定の都道府県が存在する場合は、新規の空港Entityを追加すること")
    void createAirportTest1() {
        doReturn(Optional.of(new PrefectureEntity("47", "沖縄県")))
                .when(prefectureMapper)
                .findByCodeFromPrefs("47");

        airportServiceImpl.createAirport("OKA", "那覇空港", "47");

        verify(prefectureMapper, times(1)).findByCodeFromPrefs("47");
        verify(airportMapper, times(1)).insertAirport("OKA", "那覇空港", "47");
    }

    @Test
    @DisplayName("指定の空港コードが既存のものと重複する場合はDuplicateKeyExceptionをスローすること")
    void createAirportTest2() {
        doReturn(Optional.of(new PrefectureEntity("03", "岩手県")))
                .when(prefectureMapper)
                .findByCodeFromPrefs("03");
        doThrow(new DuplicateKeyException("HNA"))
                .when(airportMapper)
                .insertAirport("HNA", "花巻空港", "03");

        assertThatExceptionOfType(DuplicateKeyException.class)
                .isThrownBy(() -> airportServiceImpl.createAirport("HNA", "花巻空港", "03"));
    }

    @Test
    @DisplayName("指定の都道府県コードに対応する都道府県のEntityが存在しない場合はNoResourceExceptionをスローすること")
    void createAirportTest3() {
        doReturn(Optional.empty())
                .when(prefectureMapper).findByCodeFromPrefs("13");

        assertThatExceptionOfType(NoResourceException.class)
                .isThrownBy(() -> airportServiceImpl.createAirport("NRT", "成田国際空港", "13"));
    }

    @Test
    @DisplayName("指定の空港コードに対応する空港のEntityがあり、かつ併せて指定した空港名が従前とは異なる（都道府県コードは同等でも可）場合は更新すること")
    void updateAirportTest1() {
        doReturn(Optional.of(new AirportEntity("SDJ", "仙台空港", "04", "宮城県")))
                .when(airportMapper)
                .findByCodeFromAirports("SDJ");
        doReturn(Optional.of(new PrefectureEntity("04", "宮城県")))
                .when(prefectureMapper)
                .findByCodeFromPrefs("04");

        airportServiceImpl.updateAirport("SDJ", "仙台国際空港", "04");

        verify(prefectureMapper, times(1)).findByCodeFromPrefs("04");
        verify(airportMapper, times(1)).findByCodeFromAirports("SDJ");
        verify(airportMapper, times(1)).updateAirport("SDJ", "仙台国際空港", "04");
    }

    @Test
    @DisplayName("指定の空港コードに対応する空港のEntityはあるが、併せて指定した空港名が従前と同等の場合は、SameAsCurrentExceptionをスローすること")
    void updateAirportTest2() {
        doReturn(Optional.of(new AirportEntity("SDJ", "仙台空港", "04", "宮城県")))
                .when(airportMapper)
                .findByCodeFromAirports("SDJ");

        assertThatExceptionOfType(SameAsCurrentException.class)
                .isThrownBy(() -> airportServiceImpl.updateAirport("SDJ", "仙台空港", "04"));
    }

    @Test
    @DisplayName("指定の空港コードに対応する空港のEntityが存在しない場合はNoResourceExceptionをスローすること")
    void updateAirportTest3() {
        doReturn(Optional.empty())
                .when(airportMapper)
                .findByCodeFromAirports("SDJ");

        assertThatExceptionOfType(NoResourceException.class)
                .isThrownBy(() -> airportServiceImpl.updateAirport("SDJ", "仙台国際空港", "04"));
    }

    @Test
    @DisplayName("指定の都道府県コードに対応する都道府県のEntityが無い場合はNoResourceExceptionをスローすること")
    void updateAirportTest4() {
        doReturn(Optional.of(new AirportEntity("SDJ", "仙台空港", "04", "宮城県")))
                .when(airportMapper)
                .findByCodeFromAirports("SDJ");
        doReturn(Optional.empty())
                .when(prefectureMapper)
                .findByCodeFromPrefs("04");

        assertThatExceptionOfType(NoResourceException.class)
                .isThrownBy(() -> airportServiceImpl.updateAirport("SDJ", "仙台国際空港", "04"));
    }

    @Test
    @DisplayName("指定の空港コードに対応する空港のEntityがある場合は削除すること")
    void deleteAirportTest1() {
        doReturn(Optional.of(new AirportEntity("SDJ", "仙台空港", "04", "宮城県")))
                .when(airportMapper)
                .findByCodeFromAirports("SDJ");

        airportServiceImpl.deleteAirport("SDJ");

        verify(airportMapper, times(1)).findByCodeFromAirports("SDJ");
        verify(airportMapper, times(1)).deleteAirport("SDJ");
    }

    @Test
    @DisplayName("指定の空港コードに対応する空港のEntityが無い場合はNoResourceExceptionをスローすること")
    void deleteAirportTest2() {
        doReturn(Optional.empty())
                .when(airportMapper)
                .findByCodeFromAirports("NKM");

        assertThatExceptionOfType(NoResourceException.class)
                .isThrownBy(() -> airportServiceImpl.deleteAirport("NKM"));
    }
}
