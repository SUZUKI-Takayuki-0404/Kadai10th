package exercise.kadai10th.service;

import exercise.kadai10th.entity.AirportEntity;
import exercise.kadai10th.entity.PrefectureEntity;
import exercise.kadai10th.exceptionhandler.DuplicateCodeException;
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
    void getAirport() {
        doReturn(Optional.of(new AirportEntity("CTS", "新千歳空港", "01", "北海道")))
                .when(airportMapper)
                .selectAirportByCode("CTS");

        assertThat(airportServiceImpl.getAirportByCode("CTS"))
                .isEqualTo(new AirportEntity("CTS", "新千歳空港", "01", "北海道"));

        verify(airportMapper, times(1)).selectAirportByCode("CTS");
    }

    @Test
    @DisplayName("指定の空港コードに対応する空港のEntityが存在しない場合はNoResourceExceptionをスローすること")
    void getAirportToThrowException() {
        doReturn(Optional.empty())
                .when(airportMapper)
                .selectAirportByCode("WKJ");

        assertThatExceptionOfType(NoResourceException.class)
                .isThrownBy(() -> airportServiceImpl.getAirportByCode("WKJ"));
    }

    @Test
    @DisplayName("指定の都道府県名に対応する空港のEntityがある場合はその全てをListで返すこと")
    void getAirportsByPref() {
        doReturn(List.of(
                new AirportEntity("CTS", "新千歳空港", "01", "北海道"),
                new AirportEntity("HKD", "函館空港", "01", "北海道")))
                .when(airportMapper)
                .selectAirportsByPrefName("北海道");

        assertThat(airportServiceImpl.getAirportsByPrefName("北海道"))
                .hasSize(2)
                .extracting("airportCode", "airportName", "prefCode", "prefName")
                .contains(
                        tuple("CTS", "新千歳空港", "01", "北海道"),
                        tuple("HKD", "函館空港", "01", "北海道")
                );

        verify(airportMapper, times(1)).selectAirportsByPrefName("北海道");
    }

    @Test
    @DisplayName("指定の都道府県コードに対応する空港のEntityが無い場合は空のListを返すこと")
    void getAirportsByPrefToReturnEmpty() {
        doReturn(List.of())
                .when(airportMapper)
                .selectAirportsByPrefName("埼玉県");

        assertThat(airportServiceImpl.getAirportsByPrefName("埼玉県")).isEmpty();
    }

    @Test
    @DisplayName("空港のEntityが存在する場合はその全てをListで返すこと")
    void getAllAirports() {
        doReturn(List.of(
                new AirportEntity("CTS", "新千歳空港", "01", "北海道"),
                new AirportEntity("HKD", "函館空港", "01", "北海道"),
                new AirportEntity("AOJ", "青森空港", "02", "青森県"),
                new AirportEntity("HNA", "花巻空港", "03", "岩手県"),
                new AirportEntity("SDJ", "仙台空港", "04", "宮城県")))
                .when(airportMapper)
                .selectAllAirports();

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

        verify(airportMapper, times(1)).selectAllAirports();
    }

    @Test
    @DisplayName("空港のEntityが無い場合は空のListを返すこと")
    void getAllAirportsToReturnEmpty() {
        doReturn(List.of())
                .when(airportMapper)
                .selectAllAirports();

        assertThat(airportServiceImpl.getAllAirports()).isEmpty();
    }

    @Test
    @DisplayName("指定の空港コードが既存のものと重複せず、かつ指定の都道府県が存在する場合は、新規の空港Entityを追加すること")
    void createAirport() {
        doReturn(Optional.of(new PrefectureEntity("47", "沖縄県")))
                .when(prefectureMapper)
                .selectPrefByCode("47");

        assertThat(airportServiceImpl.createAirport("OKA", "那覇空港", "47"))
                .isEqualTo(new AirportEntity("OKA", "那覇空港", "47", "沖縄県"));

        verify(prefectureMapper, times(1)).selectPrefByCode("47");
        verify(airportMapper, times(1)).insertAirport("OKA", "那覇空港", "47");
    }

    @Test
    @DisplayName("指定の空港コードが既存のものと重複する場合はDuplicateCodeExceptionをスローすること")
    void createAirportToThrowDuplicateCodeException() {
        doReturn(Optional.of(new PrefectureEntity("03", "岩手県")))
                .when(prefectureMapper)
                .selectPrefByCode("03");
        doThrow(new DuplicateKeyException("HNA" + " : This code will be duplicated"))
                .when(airportMapper)
                .insertAirport("HNA", "花巻空港", "03");

        assertThatExceptionOfType(DuplicateCodeException.class)
                .isThrownBy(() -> airportServiceImpl.createAirport("HNA", "花巻空港", "03"));
    }

    @Test
    @DisplayName("指定の都道府県コードに対応する都道府県のEntityが存在しない場合はNoResourceExceptionをスローすること")
    void createAirportToThrowNoResourceException() {
        doReturn(Optional.empty())
                .when(prefectureMapper).selectPrefByCode("13");

        assertThatExceptionOfType(NoResourceException.class)
                .isThrownBy(() -> airportServiceImpl.createAirport("NRT", "成田国際空港", "13"));
    }

    @Test
    @DisplayName("指定の空港コードに対応する空港のEntityがあり、かつ併せて指定した空港名が従前とは異なる（都道府県コードは同等でも可）場合は更新すること")
    void updateAirport() {
        doReturn(Optional.of(new AirportEntity("SDJ", "仙台空港", "04", "宮城県")))
                .when(airportMapper)
                .selectAirportByCode("SDJ");
        doReturn(Optional.of(new PrefectureEntity("04", "宮城県")))
                .when(prefectureMapper)
                .selectPrefByCode("04");

        airportServiceImpl.updateAirport("SDJ", "仙台国際空港", "04");

        verify(prefectureMapper, times(1)).selectPrefByCode("04");
        verify(airportMapper, times(1)).selectAirportByCode("SDJ");
        verify(airportMapper, times(1)).updateAirport("SDJ", "仙台国際空港", "04");
    }

    @Test
    @DisplayName("指定の空港コードに対応する空港のEntityはあるが、併せて指定した空港名が従前と同等の場合は、SameAsCurrentExceptionをスローすること")
    void updateAirportToThrowSameAsCurrentException() {
        doReturn(Optional.of(new AirportEntity("SDJ", "仙台空港", "04", "宮城県")))
                .when(airportMapper)
                .selectAirportByCode("SDJ");

        assertThatExceptionOfType(SameAsCurrentException.class)
                .isThrownBy(() -> airportServiceImpl.updateAirport("SDJ", "仙台空港", "04"));
    }

    @Test
    @DisplayName("指定の空港コードに対応する空港のEntityが存在しない場合はNoResourceExceptionをスローすること")
    void updateAirportToThrowNoResourceExceptionIfAirportNotExists() {
        doReturn(Optional.empty())
                .when(airportMapper)
                .selectAirportByCode("SDJ");

        assertThatExceptionOfType(NoResourceException.class)
                .isThrownBy(() -> airportServiceImpl.updateAirport("SDJ", "仙台国際空港", "04"));
    }

    @Test
    @DisplayName("指定の都道府県コードに対応する都道府県のEntityが無い場合はNoResourceExceptionをスローすること")
    void updateAirportToThrowNoResourceExceptionIfPrefNotExists() {
        doReturn(Optional.of(new AirportEntity("SDJ", "仙台空港", "04", "宮城県")))
                .when(airportMapper)
                .selectAirportByCode("SDJ");
        doReturn(Optional.empty())
                .when(prefectureMapper)
                .selectPrefByCode("04");

        assertThatExceptionOfType(NoResourceException.class)
                .isThrownBy(() -> airportServiceImpl.updateAirport("SDJ", "仙台国際空港", "04"));
    }

    @Test
    @DisplayName("指定の空港コードに対応する空港のEntityがある場合は削除すること")
    void deleteAirport() {
        doReturn(Optional.of(new AirportEntity("SDJ", "仙台空港", "04", "宮城県")))
                .when(airportMapper)
                .selectAirportByCode("SDJ");

        airportServiceImpl.deleteAirport("SDJ");

        verify(airportMapper, times(1)).selectAirportByCode("SDJ");
        verify(airportMapper, times(1)).deleteAirport("SDJ");
    }

    @Test
    @DisplayName("指定の空港コードに対応する空港のEntityが無い場合はNoResourceExceptionをスローすること")
    void deleteAirportToThrowException() {
        doReturn(Optional.empty())
                .when(airportMapper)
                .selectAirportByCode("NKM");

        assertThatExceptionOfType(NoResourceException.class)
                .isThrownBy(() -> airportServiceImpl.deleteAirport("NKM"));
    }
}
