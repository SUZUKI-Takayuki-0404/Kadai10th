package kadai10th.mapper;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import kadai10th.entity.PrefectureEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import static com.github.database.rider.core.api.configuration.Orthography.LOWERCASE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@DBRider
@DBUnit(caseInsensitiveStrategy = LOWERCASE, schema = "airport_database",
        url = "jdbc:mysql://localhost:3307/airport_database",
        user = "user", password = "password")
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PrefectureMapperTest {

    @Autowired
    PrefectureMapper prefectureMapper;

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @Transactional
    @DisplayName("指定の都道府県コードが存在するときは、対応する都道府県EntityをOptionalとして返すこと")
    void findByCodeFromPrefsTest1() {
        assertThat(prefectureMapper.findByCodeFromPrefs("01"))
                .get()
                .usingRecursiveComparison()
                .isEqualTo(new PrefectureEntity("01", "北海道"));
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @Transactional
    @DisplayName("指定の都道府県コードが無いときは、空のOptionalを返すこと")
    void findByCodeFromPrefsTest2() {
        assertThat(prefectureMapper.findByCodeFromPrefs("11")).isEmpty();
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @Transactional
    @DisplayName("指定の都道府県名が存在するときは、対応する都道府県EntityをOptionalとして返すこと")
    void findByNameFromPrefsTest1() {
        assertThat(prefectureMapper.findByNameFromPrefs("青森県"))
                .get()
                .usingRecursiveComparison()
                .isEqualTo(new PrefectureEntity("02", "青森県"));
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @Transactional
    @DisplayName("指定の都道府県名が無いときは、空のOptionalを返すこと")
    void findByNameFromPrefsTest2() {
        assertThat(prefectureMapper.findByNameFromPrefs("埼玉県")).isEmpty();
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @Transactional
    @DisplayName("存在する都道府県データ全てをListとして返すこと")
    void findAllFromPrefsTest1() {
        assertThat(prefectureMapper.findAllFromPrefs())
                .hasSize(4)
                .extracting("prefCode", "prefName")
                .contains(
                        tuple("01", "北海道"),
                        tuple("02", "青森県"),
                        tuple("03", "岩手県"),
                        tuple("04", "宮城県")
                );
    }

    @Test
    @DataSet(value = "datasets/prefecture-empty.yml")
    @Transactional
    @DisplayName("都道府県データが存在しないときは空のListを返すこと")
    void findAllFromPrefsTest2() {
        assertThat(prefectureMapper.findAllFromPrefs()).isEmpty();
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @ExpectedDataSet(value = "datasets/prefecture-insert.yml", orderBy = "prefCode")
    @Transactional
    @DisplayName("指定の都道府県コードが既存のものと重複しない場合は、併せて指定した都道府県名と共にデータ登録すること")
    void insertPrefTest1() {
        prefectureMapper.insertPref("05", "秋田県");
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @Transactional
    @DisplayName("指定の都道府県コードが既存のものと重複する場合は、DuplicateKeyExceptionをスローすること")
    void insertPrefTest2() {
        assertThatExceptionOfType(DuplicateKeyException.class)
                .isThrownBy(() -> {
                    prefectureMapper.insertPref("04", "宮城県");
                });
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @ExpectedDataSet(value = "datasets/prefecture-update.yml", orderBy = "prefCode")
    @Transactional
    @DisplayName("指定の都道府県コードが存在するときは、併せて指定した都道府県名でデータ更新すること")
    void updatePrefTest1() {
        prefectureMapper.updatePref("02", "あおもりけん");
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @ExpectedDataSet(value = "datasets/prefectures.yml", orderBy = "prefCode")
    @Transactional
    @DisplayName("指定の都道府県コードが存在しない場合は何も変更しないこと")
    void updatePrefTest2() {
        prefectureMapper.updatePref("05", "あおもりけん");
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @ExpectedDataSet(value = "datasets/prefecture-delete.yml", orderBy = "prefCode")
    @Transactional
    @DisplayName("指定の都道府県コードが存在する場合は、対応する都道府県データを削除すること")
    void deletePrefTest1() {
        prefectureMapper.deletePref("03");
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @ExpectedDataSet(value = "datasets/prefectures.yml", orderBy = "prefCode")
    @Transactional
    @DisplayName("指定の都道府県コードが存在しない場合は何も変更しないこと")
    void deletePrefTest2() {
        prefectureMapper.deletePref("05");
    }

}
