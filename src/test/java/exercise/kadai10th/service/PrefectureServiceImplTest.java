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

    @Nested
    class GetPrefByCodeTest {
        @Test
        @DisplayName("""
                Should get a corresponding prefecture by code when exists
                都道府県コードに対応する都道府県がある場合は取得できること
                """)
        void workNormally() {
            doReturn(Optional.of(new PrefectureEntity("01", "北海道")))
                    .when(prefectureMapper)
                    .selectPrefByCode("01");

            assertThat(prefectureServiceImpl.getPrefByCode("01"))
                    .isEqualTo(new PrefectureEntity("01", "北海道"));

            verify(prefectureMapper, times(1)).selectPrefByCode("01");
        }

        @Test
        @DisplayName("""
                Should throw NoResourceException when no corresponding prefecture exists
                都道府県が無い場合はNoResourceExceptionをスローすること
                """)
        void throwWhenNoPref() {
            doReturn(Optional.empty())
                    .when(prefectureMapper)
                    .selectPrefByCode("11");

            assertThatExceptionOfType(NoResourceException.class)
                    .isThrownBy(() -> prefectureServiceImpl.getPrefByCode("11"));
        }
    }

    @Nested
    class GetPrefByNameTest {
        @Test
        @DisplayName("""
                Should get a corresponding prefecture by name when exists
                都道府県名に対応する都道府県がある場合は取得できること
                """)
        void workNormally() {
            doReturn(Optional.of(new PrefectureEntity("02", "青森県")))
                    .when(prefectureMapper)
                    .selectPrefByName("青森県");

            assertThat(prefectureServiceImpl.getPrefByName("青森県"))
                    .isEqualTo(new PrefectureEntity("02", "青森県"));

            verify(prefectureMapper, times(1)).selectPrefByName("青森県");
        }

        @Test
        @DisplayName("""
                Should throw NoResourceException when no corresponding prefecture exists
                都道府県が無い場合はNoResourceExceptionをスローすること
                """)
        void throwWhenNoPref() {
            doReturn(Optional.empty())
                    .when(prefectureMapper)
                    .selectPrefByName("埼玉県");

            assertThatExceptionOfType(NoResourceException.class)
                    .isThrownBy(() -> prefectureServiceImpl.getPrefByName("埼玉県"));
        }
    }

    @Nested
    class GetAllPrefsTest {
        @Test
        @DisplayName("""
                Should get a list of all prefectures when exist
                登録済みの都道府県がある場合はその全てをListで取得できること
                """)
        void workNormally() {
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
        @DisplayName("""
                Should get a empty list when no prefecture exists
                都道府県が無い場合は空のListを返すこと
                """)
        void returnEmptyWhenNoPref() {
            doReturn(List.of())
                    .when(prefectureMapper)
                    .selectAllPrefs();

            assertThat(prefectureServiceImpl.getAllPrefs()).isEmpty();
        }
    }

    @Nested
    class CreatePrefTest {
        @Test
        @DisplayName("""
                Should add a prefecture when its code is unique
                都道府県コードが既存のものと重複しない場合、都道府県を追加できること
                """)
        void workNormally() {
            doNothing().when(prefectureMapper).insertPref("05", "秋田県");

            assertThat(prefectureServiceImpl.createPref("05", "秋田県"))
                    .isEqualTo(new PrefectureEntity("05", "秋田県"));

            verify(prefectureMapper).insertPref("05", "秋田県");
        }

        @Test
        @DisplayName("""
                Should Should throw DuplicateCodeException when a code already exists
                都道府県コードが既存のものと重複する場合はDuplicateCodeExceptionをスローすること
                """)
        void throwWhenCodeDuplicates() {
            doThrow(new DuplicateKeyException("04")).when(prefectureMapper).insertPref("04", "宮城県");

            assertThatExceptionOfType(DuplicateCodeException.class)
                    .isThrownBy(() -> prefectureServiceImpl.createPref("04", "宮城県"));
        }
    }

    @Nested
    class UpdatePrefTest {
        @Test
        @DisplayName("""
                Should rename a prefecture when its code exists and new name differs from current one
                都道府県コードに対応する都道府県があり、かつ従前と異なる場合は都道府県名を更新できること
                """)
        void workNormally() {
            doReturn(Optional.of(new PrefectureEntity("02", "青森県")))
                    .when(prefectureMapper)
                    .selectPrefByCode("02");
            doReturn(true).when(prefectureMapper).updatePref("02", "あおもりけん");

            prefectureServiceImpl.updatePref("02", "あおもりけん");

            verify(prefectureMapper, times(1)).selectPrefByCode("02");
            verify(prefectureMapper, times(1)).updatePref("02", "あおもりけん");
        }

        @Test
        @DisplayName("""
                Should throw SameAsCurrentException when no change of the name of existing prefecture
                都道府県コードに対応する都道府県はあるが、都道府県名が従前と同等の場合はSameAsCurrentExceptionをスローすること
                """)
        void throwWhenNoChange() {
            doReturn(Optional.of(new PrefectureEntity("02", "青森県")))
                    .when(prefectureMapper)
                    .selectPrefByCode("02");

            assertThatExceptionOfType(SameAsCurrentException.class)
                    .isThrownBy(() -> prefectureServiceImpl.updatePref("02", "青森県"));
        }

        @Test
        @DisplayName("""
                Should throw NoResourceException when no corresponding prefecture exists
                都道府県コードに対応する都道府県が無い場合はNoResourceExceptionをスローすること
                """)
        void throwWhenNoPref() {
            doReturn(Optional.empty())
                    .when(prefectureMapper)
                    .selectPrefByCode("02");

            assertThatExceptionOfType(NoResourceException.class)
                    .isThrownBy(() -> prefectureServiceImpl.updatePref("02", "あおもりけん"));
        }
    }

    @Nested
    class DeletePrefTest {
        @Test
        @DisplayName("""
                Should delete a corresponding prefecture when it exists and no airport is registered
                都道府県コードに対応する都道府県があり、かつその都道府県に空港が存在しない場合、都道府県を削除できること
                """)
        void workNormally() {
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
        @DisplayName("""
                Should throw CodeInUseException when the prefecture has one or more airports
                都道府県コードに対応する都道府県があり、かつその都道府県に空港がある場合、CodeInUseExceptionをスローすること
                """)
        void throwWhenCodeUsed() {
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
        @DisplayName("""
                Should throw NoResourceException when no corresponding prefecture exists
                都道府県コードに対応する都道府県が無い場合はNoResourceExceptionをスローすること
                """)
        void throwWhenNoPref() {
            doReturn(Optional.empty())
                    .when(prefectureMapper)
                    .selectPrefByCode("05");

            assertThatExceptionOfType(NoResourceException.class)
                    .isThrownBy(() -> prefectureServiceImpl.deletePref("05"));
        }
    }

}
