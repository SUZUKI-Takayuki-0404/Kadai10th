package exercise.kadai10th.mapper;

import exercise.kadai10th.entity.PrefectureEntity;
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
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@DBRider
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class PrefectureMapperTest {

    @Autowired
    private PrefectureMapper prefectureMapper;

    @Nested
    @DisplayName("Method: selectPrefByCode")
    class SelectPrefByCodeTest {
        @Test
        @DataSet(value = "datasets/prefectures.yml")
        @DisplayName("Should return a corresponding prefecture by a code when exists \n"
                + "都道府県があるときは取得できること")
        void workNormally() {
            assertThat(prefectureMapper.selectPrefByCode("01"))
                    .get()
                    .isEqualTo(new PrefectureEntity("01", "北海道"));
        }

        @Test
        @DataSet(value = "datasets/prefectures.yml")
        @DisplayName("Should return empty when no corresponding prefecture exists \n"
                + "都道府県が無いときは空として返すこと")
        void returnEmptyWhenNoPref() {
            assertThat(prefectureMapper.selectPrefByCode("11")).isEmpty();
        }
    }

    @Nested
    @DisplayName("Method: selectPrefByName")
    class SelectPrefByNameTest {
        @Test
        @DataSet(value = "datasets/prefectures.yml")
        @DisplayName("Should return a corresponding prefecture by a prefecture name when exists \n"
                + "都道府県があるときは取得できること")
        void workNormally() {
            assertThat(prefectureMapper.selectPrefByName("青森県"))
                    .get()
                    .isEqualTo(new PrefectureEntity("02", "青森県"));
        }

        @Test
        @DataSet(value = "datasets/prefectures.yml")
        @DisplayName("Should return empty when no corresponding prefecture exists \n"
                + "都道府県名が無いときは空として返すこと")
        void returnEmptyWhenNoPref() {
            assertThat(prefectureMapper.selectPrefByName("埼玉県")).isEmpty();
        }
    }

    @Nested
    @DisplayName("Method: selectAllPrefs")
    class SelectAllPrefsTest {
        @Test
        @DataSet(value = "datasets/prefectures.yml")
        @DisplayName("Should return a list of all prefectures when exist \n"
                + "登録済みの都道府県がある場合は全て取得できること")
        void workNormally() {
            assertThat(prefectureMapper.selectAllPrefs())
                    .hasSize(5)
                    .extracting("prefCode", "prefName")
                    .contains(
                            tuple("01", "北海道"),
                            tuple("02", "青森県"),
                            tuple("03", "岩手県"),
                            tuple("04", "宮城県"),
                            tuple("47", "沖縄県")
                    );
        }

        @Test
        @DataSet(value = "datasets/prefecture-empty.yml")
        @DisplayName("Should return empty when no prefecture exists \n"
                + "都道府県データが無いときは空として返すこと")
        void returnEmptyWhenNoPref() {
            assertThat(prefectureMapper.selectAllPrefs()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Method: insertPref")
    class InsertPrefTest {
        @Test
        @DataSet(value = "datasets/prefectures.yml")
        @ExpectedDataSet(value = "datasets/prefecture-insert.yml", orderBy = "prefCode")
        @DisplayName("Should add a prefecture when its code is unique \n"
                + "都道府県コードが既存のものと重複しない場合は登録できること")
        void workNormally() {
            prefectureMapper.insertPref("05", "秋田県");
        }

        @Test
        @DataSet(value = "datasets/prefectures.yml")
        @DisplayName("Should throw DuplicateCodeException when a code already exists \n"
                + "都道府県コードが既存のものと重複する場合は、DuplicateKeyExceptionをスローすること")
        void throwWhenCodeDuplicates() {
            assertThatExceptionOfType(DuplicateKeyException.class)
                    .isThrownBy(() -> prefectureMapper.insertPref("04", "宮城県"));
        }
    }

    @Nested
    @DisplayName("Method: updatePref")
    class UpdatePrefTest {
        @Test
        @DataSet(value = "datasets/prefectures.yml")
        @ExpectedDataSet(value = "datasets/prefecture-update.yml", orderBy = "prefCode")
        @DisplayName("Should rename a prefecture when exists \n"
                + "都道府県があるときは名前を更新できること")
        void workNormally() {
            prefectureMapper.updatePref("02", "あおもりけん");
        }

        @Test
        @DataSet(value = "datasets/prefectures.yml")
        @ExpectedDataSet(value = "datasets/prefectures.yml", orderBy = "prefCode")
        @DisplayName("Should do nothing when no corresponding prefecture exists \n"
                + "都道府県が無い場合は何しないこと")
        void doNothingWhenNoPref() {
            prefectureMapper.updatePref("06", "やまがたけん");
        }
    }

    @Nested
    @DisplayName("Method: deletePref")
    class DeletePrefTest {
        @Test
        @DataSet(value = "datasets/prefectures.yml")
        @ExpectedDataSet(value = "datasets/prefecture-delete.yml", orderBy = "prefCode")
        @DisplayName("Should delete a prefecture when exists \n"
                + "都道府県がある場合は削除できること")
        void workNormally() {
            prefectureMapper.deletePref("47");
        }

        @Test
        @DataSet(value = "datasets/prefectures.yml")
        @ExpectedDataSet(value = "datasets/prefectures.yml", orderBy = "prefCode")
        @DisplayName("Should do nothing when no corresponding prefecture exists \n"
                + "都道府県が無い場合は何もしないこと")
        void doNothingWhenNoPref() {
            prefectureMapper.deletePref("06");
        }
    }
}
