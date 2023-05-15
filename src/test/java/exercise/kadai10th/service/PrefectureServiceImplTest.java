package exercise.kadai10th.service;

import exercise.kadai10th.entity.AirportEntity;
import exercise.kadai10th.entity.PrefectureEntity;
import exercise.kadai10th.exceptionhandler.CodeInUseException;
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
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PrefectureServiceImplTest {

    @InjectMocks
    PrefectureServiceImpl prefectureServiceImpl;

    @Mock
    PrefectureMapper prefectureMapper;

    @Mock
    AirportMapper airportMapper;

    @Test
    @DisplayName("指定の都道府県コードに対応する都道府県のEntityがある場合はそれを返すこと")
    void getPrefByCode() {
        doReturn(Optional.of(new PrefectureEntity("01", "北海道")))
                .when(prefectureMapper)
                .selectPrefByCode("01");

        assertThat(prefectureServiceImpl.getPrefByCode("01"))
                .isEqualTo(new PrefectureEntity("01", "北海道"));

        verify(prefectureMapper, times(1)).selectPrefByCode("01");
    }

    @Test
    @DisplayName("指定の都道府県コードに対応する都道府県のEntityが存在しない場合はNoResourceExceptionをスローすること")
    void getPrefByCodeToThrowException() {
        doReturn(Optional.empty())
                .when(prefectureMapper)
                .selectPrefByCode("11");

        assertThatExceptionOfType(NoResourceException.class)
                .isThrownBy(() -> prefectureServiceImpl.getPrefByCode("11"));
    }

    @Test
    @DisplayName("指定の都道府県名に対応する都道府県のEntityがある場合はそれを返すこと")
    void getPrefByName() {
        doReturn(Optional.of(new PrefectureEntity("02", "青森県")))
                .when(prefectureMapper)
                .selectPrefByName("青森県");

        assertThat(prefectureServiceImpl.getPrefByName("青森県"))
                .isEqualTo(new PrefectureEntity("02", "青森県"));

        verify(prefectureMapper, times(1)).selectPrefByName("青森県");
    }

    @Test
    @DisplayName("指定の都道府県名に対応する都道府県のEntityが無い場合はNoResourceExceptionをスローすること")
    void getPrefByNameToThrowException() {
        doReturn(Optional.empty())
                .when(prefectureMapper)
                .selectPrefByName("埼玉県");

        assertThatExceptionOfType(NoResourceException.class)
                .isThrownBy(() -> prefectureServiceImpl.getPrefByName("埼玉県"));
    }

    @Test
    @DisplayName("都道府県のEntityが存在する場合はその全てをListで返すこと")
    void getAllPrefs() {
        doReturn(List.of(
                new PrefectureEntity("01", "北海道"),
                new PrefectureEntity("02", "青森県"),
                new PrefectureEntity("03", "岩手県"),
                new PrefectureEntity("04", "宮城県")))
                .when(prefectureMapper)
                .selectAllPrefs();

        assertThat(prefectureServiceImpl.getAllPrefs())
                .hasSize(4)
                .extracting("prefCode", "prefName")
                .contains(
                        tuple("01", "北海道"),
                        tuple("02", "青森県"),
                        tuple("03", "岩手県"),
                        tuple("04", "宮城県")
                );

        verify(prefectureMapper, times(1)).selectAllPrefs();
    }

    @Test
    @DisplayName("都道府県のEntityが無い場合は空のListを返すこと")
    void getAllPrefsToReturnEmpty() {
        doReturn(List.of())
                .when(prefectureMapper)
                .selectAllPrefs();

        assertThat(prefectureServiceImpl.getAllPrefs()).isEmpty();
    }

    @Test
    @DisplayName("指定の都道府県コードが既存のものと重複しない場合は併せて指定した都道府県名で新規の都道府県のEntityを追加すること")
    void createPref() {
        doNothing().when(prefectureMapper).insertPref("05", "秋田県");

        assertThat(prefectureServiceImpl.createPref("05", "秋田県"))
                .isEqualTo(new PrefectureEntity("05", "秋田県"));

        verify(prefectureMapper).insertPref("05", "秋田県");
    }

    @Test
    @DisplayName("指定の都道府県コードが既存のものと重複する場合は、DuplicateCodeExceptionをスローすること")
    void createPrefToThrowException() {
        doThrow(new DuplicateKeyException("04")).when(prefectureMapper).insertPref("04", "宮城県");

        assertThatExceptionOfType(DuplicateCodeException.class)
                .isThrownBy(() -> prefectureServiceImpl.createPref("04", "宮城県"));
    }

    @Test
    @DisplayName("指定の都道府県コードに対応する都道府県のEntityがあり、かつ併せて指定した都道府県名が従前と異なる場合は、都道府県名を更新すること")
    void updatePref() {
        doReturn(Optional.of(new PrefectureEntity("02", "青森県")))
                .when(prefectureMapper)
                .selectPrefByCode("02");
        doReturn(true).when(prefectureMapper).updatePref("02", "あおもりけん");

        prefectureServiceImpl.updatePref("02", "あおもりけん");

        verify(prefectureMapper, times(1)).selectPrefByCode("02");
        verify(prefectureMapper, times(1)).updatePref("02", "あおもりけん");
    }

    @Test
    @DisplayName("指定の都道府県コードに対応する都道府県のEntityはあるが、併せて指定した都道府県名が従前と同等の場合は、SameAsCurrentExceptionをスローすること")
    void updatePrefToThrowSameAsCurrentException() {
        doReturn(Optional.of(new PrefectureEntity("02", "青森県")))
                .when(prefectureMapper)
                .selectPrefByCode("02");

        assertThatExceptionOfType(SameAsCurrentException.class)
                .isThrownBy(() -> prefectureServiceImpl.updatePref("02", "青森県"));
    }

    @Test
    @DisplayName("指定の都道府県コードに対応する都道府県のEntityが無い場合は、NoResourceExceptionをスローすること")
    void updatePrefToThrowNoResourceException() {
        doReturn(Optional.empty())
                .when(prefectureMapper)
                .selectPrefByCode("02");

        assertThatExceptionOfType(NoResourceException.class)
                .isThrownBy(() -> prefectureServiceImpl.updatePref("02", "あおもりけん"));
    }

    @Test
    @DisplayName("指定の都道府県コードに対応する都道府県のEntityがあり、かつその都道府県に空港が存在しない場合は、都道府県を削除すること")
    void deletePref() {
        doReturn(Optional.of(new PrefectureEntity("11", "埼玉県")))
                .when(prefectureMapper)
                .selectPrefByCode("11");
        doReturn(List.of())
                .when(airportMapper)
                .selectAirportsByPrefName("埼玉県");

        prefectureServiceImpl.deletePref("11");

        verify(prefectureMapper).selectPrefByCode("11");
        verify(prefectureMapper).deletePref("11");
    }

    @Test
    @DisplayName("指定の都道府県コードに対応する都道府県のEntityがあり、かつその都道府県コードをもつ空港が存在する場合は、CodeInUseExceptionをスローすること")
    void deletePrefToThrowCodeInUseException() {
        doReturn(Optional.of(new PrefectureEntity("01", "北海道")))
                .when(prefectureMapper)
                .selectPrefByCode("01");
        doReturn(List.of(
                new AirportEntity("CTS", "新千歳空港", "01", "北海道"),
                new AirportEntity("HKD", "函館空港", "01", "北海道")))
                .when(airportMapper)
                .selectAirportsByPrefName("北海道");

        assertThatExceptionOfType(CodeInUseException.class)
                .isThrownBy(() -> prefectureServiceImpl.deletePref("01"));
    }

    @Test
    @DisplayName("指定の都道府県コードに対応する都道府県のEntityが無い場合は、NoResourceExceptionをスローすること")
    void deletePrefToThrowNoResourceException() {
        doReturn(Optional.empty())
                .when(prefectureMapper)
                .selectPrefByCode("05");

        assertThatExceptionOfType(NoResourceException.class)
                .isThrownBy(() -> prefectureServiceImpl.deletePref("05"));
    }
}
