package exercise.kadai10th.service;

import exercise.kadai10th.entity.AirportEntity;
import exercise.kadai10th.entity.PrefectureEntity;
import exercise.kadai10th.exceptionhandler.DuplicateCodeException;
import exercise.kadai10th.exceptionhandler.NoResourceException;
import exercise.kadai10th.exceptionhandler.SameAsCurrentException;
import exercise.kadai10th.mapper.AirportMapper;
import exercise.kadai10th.mapper.PrefectureMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    @Nested
    @DisplayName("Method: getAirport")
    class GetAirportTest {
        @Test
        @DisplayName("Should get a corresponding airport by code when exists \n"
                + "空港コードに対応する空港がある場合は取得できること")
        void workNormally() {
            doReturn(Optional.of(new AirportEntity("CTS", "新千歳空港", "01", "北海道")))
                    .when(airportMapper)
                    .selectAirportByCode("CTS");

            assertThat(airportServiceImpl.getAirportByCode("CTS"))
                    .isEqualTo(new AirportEntity("CTS", "新千歳空港", "01", "北海道"));

            verify(airportMapper, times(1)).selectAirportByCode("CTS");
        }

        @Test
        @DisplayName("Should throw NoResourceException when no corresponding airport exists \n"
                + "空港コードに対応する空港が無い場合はNoResourceExceptionをスローすること")
        void throwWhenNoAirport() {
            doReturn(Optional.empty())
                    .when(airportMapper)
                    .selectAirportByCode("WKJ");

            assertThatExceptionOfType(NoResourceException.class)
                    .isThrownBy(() -> airportServiceImpl.getAirportByCode("WKJ"));
        }
    }

    @Nested
    @DisplayName("Method: getAirportsByPref")
    class GetAirportsByPrefTest {
        @Test
        @DisplayName("Should get a list of airports in a prefecture when exist \n"
                + "その都道府県に空港がある場合は全て取得できること")
        void workNormally() {
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
        @DisplayName("Should get a empty list when no airport exists \n"
                + "都道府県に空港が無い場合は空のリストを返すこと")
        void returnEmptyWhenNoAirport() {
            doReturn(List.of())
                    .when(airportMapper)
                    .selectAirportsByPrefName("埼玉県");

            assertThat(airportServiceImpl.getAirportsByPrefName("埼玉県")).isEmpty();
        }
    }

    @Nested
    @DisplayName("Method: getAllAirports")
    class GetAllAirportsTest {
        @Test
        @DisplayName("Should get a list of all airports when exist \n"
                + "登録済みの空港がある場合は全て取得できること")
        void workNormally() {
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
        @DisplayName("Should get a empty list when no airport exists \n"
                + "登録済みの空港が無い場合は空のリストを返すこと")
        void returnEmptyWhenNoAirport() {
            doReturn(List.of())
                    .when(airportMapper)
                    .selectAllAirports();

            assertThat(airportServiceImpl.getAllAirports()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Method: createAirport")
    class CreateAirportTest {
        @Test
        @DisplayName("Should add an airport when its code is unique and located prefecture exists \n"
                + "空港コードが既存のものと重複せず、かつ所在の都道府県がある場合、空港データを追加できること")
        void workNormally() {
            doReturn(Optional.of(new PrefectureEntity("47", "沖縄県")))
                    .when(prefectureMapper)
                    .selectPrefByCode("47");

            assertThat(airportServiceImpl.createAirport("OKA", "那覇空港", "47"))
                    .isEqualTo(new AirportEntity("OKA", "那覇空港", "47", "沖縄県"));

            verify(prefectureMapper, times(1)).selectPrefByCode("47");
            verify(airportMapper, times(1)).insertAirport("OKA", "那覇空港", "47");
        }

        @Test
        @DisplayName("Should throw DuplicateCodeException when a code already exists \n"
                + "指定の空港コードが既存のものと重複する場合はDuplicateCodeExceptionをスローすること")
        void throwWhenCodeDuplicates() {
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
        @DisplayName("Should throw NoResourceException when no corresponding prefecture exists \n"
                + "都道府県コードに対応する都道府県が無い場合はNoResourceExceptionをスローすること")
        void throwWhenNoPref() {
            doReturn(Optional.empty())
                    .when(prefectureMapper).selectPrefByCode("13");

            assertThatExceptionOfType(NoResourceException.class)
                    .isThrownBy(() -> airportServiceImpl.createAirport("NRT", "成田国際空港", "13"));
        }
    }

    @Nested
    @DisplayName("Method: updateAirport")
    class UpdateAirportTest {
        @Test
        @DisplayName("Should rename an airport when its code exists and new name differs from current one \n"
                + "空港コードに対応する空港があり、かつ空港名が従前とは異なる場合は空港データを更新できること")
        void workNormally() {
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
        @DisplayName("Should throw SameAsCurrentException when no change of the name of existing airport \n"
                + "空港コードに対応する空港はあるが、空港名が従前と同等の場合はSameAsCurrentExceptionをスローすること")
        void throwWhenNoChange() {
            doReturn(Optional.of(new AirportEntity("SDJ", "仙台空港", "04", "宮城県")))
                    .when(airportMapper)
                    .selectAirportByCode("SDJ");

            assertThatExceptionOfType(SameAsCurrentException.class)
                    .isThrownBy(() -> airportServiceImpl.updateAirport("SDJ", "仙台空港", "04"));
        }

        @Test
        @DisplayName("Should throw NoResourceException when no corresponding airport exists \n"
                + "空港コードに対応する空港が無い場合はNoResourceExceptionをスローすること")
        void throwWhenNoAirport() {
            doReturn(Optional.empty())
                    .when(airportMapper)
                    .selectAirportByCode("SDJ");

            assertThatExceptionOfType(NoResourceException.class)
                    .isThrownBy(() -> airportServiceImpl.updateAirport("SDJ", "仙台国際空港", "04"));
        }

        @Test
        @DisplayName("Should throw NoResourceException when no corresponding prefecture exists \n"
                + "都道府県コードに対応する都道府県が無い場合はNoResourceExceptionをスローすること")
        void throwWhenNoPref() {
            doReturn(Optional.of(new AirportEntity("SDJ", "仙台空港", "04", "宮城県")))
                    .when(airportMapper)
                    .selectAirportByCode("SDJ");
            doReturn(Optional.empty())
                    .when(prefectureMapper)
                    .selectPrefByCode("04");

            assertThatExceptionOfType(NoResourceException.class)
                    .isThrownBy(() -> airportServiceImpl.updateAirport("SDJ", "仙台国際空港", "04"));
        }
    }

    @Nested
    @DisplayName("Method: deleteAirport")
    class DeleteAirportTest {//"Should \n"

        @Test
        @DisplayName("Should delete a corresponding airport when exists \n"
                + "指定の空港コードに対応する空港のEntityがある場合は削除すること")
        void workNormally() {
            doReturn(Optional.of(new AirportEntity("SDJ", "仙台空港", "04", "宮城県")))
                    .when(airportMapper)
                    .selectAirportByCode("SDJ");

            airportServiceImpl.deleteAirport("SDJ");

            verify(airportMapper, times(1)).selectAirportByCode("SDJ");
            verify(airportMapper, times(1)).deleteAirport("SDJ");
        }

        @Test
        @DisplayName("Should throw NoResourceException when no corresponding airport exists \n"
                + "空港コードに対応する空港が無い場合はNoResourceExceptionをスローすること")
        void throwWhenNoAirport() {
            doReturn(Optional.empty())
                    .when(airportMapper)
                    .selectAirportByCode("NKM");

            assertThatExceptionOfType(NoResourceException.class)
                    .isThrownBy(() -> airportServiceImpl.deleteAirport("NKM"));
        }
    }

}
