package exercise.kadai10th.mapper;

import exercise.kadai10th.entity.PrefectureEntity;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

//@TestClassOrder(ClassOrderer.OrderAnnotation.class)
//@Order(11)
@DBRider
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class PrefectureMapperTest {

    @Autowired
    private PrefectureMapper prefectureMapper;

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @DisplayName("指定の都道府県コードが存在するときは、対応する都道府県Entityを返すこと")
    void selectPrefByCode() {
        assertThat(prefectureMapper.selectPrefByCode("01"))
                .get()
                .isEqualTo(new PrefectureEntity("01", "北海道"));
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @DisplayName("指定の都道府県コードが無いときは、空のOptionalを返すこと")
    void selectPrefByCodeToReturnEmpty() {
        assertThat(prefectureMapper.selectPrefByCode("11")).isEmpty();
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @DisplayName("指定の都道府県名が存在するときは、対応する都道府県Entityを返すこと")
    void selectPrefByName() {
        assertThat(prefectureMapper.selectPrefByName("青森県"))
                .get()
                .isEqualTo(new PrefectureEntity("02", "青森県"));
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @DisplayName("指定の都道府県名が無いときは、空のOptionalを返すこと")
    void selectPrefByNameToReturnEmpty() {
        assertThat(prefectureMapper.selectPrefByName("埼玉県")).isEmpty();
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @DisplayName("存在する都道府県データ全てをListとして返すこと")
    void selectAllPrefs() {
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
    @DisplayName("都道府県データが存在しないときは空のListを返すこと")
    void selectAllPrefsToReturnEmpty() {
        assertThat(prefectureMapper.selectAllPrefs()).isEmpty();
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @ExpectedDataSet(value = "datasets/prefecture-insert.yml", orderBy = "prefCode")
    @DisplayName("指定の都道府県コードが既存のものと重複しない場合は、併せて指定した都道府県名と共にデータ登録すること")
    void insertPref() {
        prefectureMapper.insertPref("05", "秋田県");
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @DisplayName("指定の都道府県コードが既存のものと重複する場合は、DuplicateKeyExceptionをスローすること")
    void insertPrefToThrowException() {
        assertThatExceptionOfType(DuplicateKeyException.class)
                .isThrownBy(() -> prefectureMapper.insertPref("04", "宮城県"));
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @ExpectedDataSet(value = "datasets/prefecture-update.yml", orderBy = "prefCode")
    @DisplayName("指定の都道府県コードが存在するときは、併せて指定した都道府県名でデータ更新すること")
    void updatePref() {
        prefectureMapper.updatePref("02", "あおもりけん");
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @ExpectedDataSet(value = "datasets/prefectures.yml", orderBy = "prefCode")
    @DisplayName("指定の都道府県コードが存在しない場合は何も変更しないこと")
    void updatePrefToDoNothing() {
        prefectureMapper.updatePref("06", "やまがたけん");
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @ExpectedDataSet(value = "datasets/prefecture-delete.yml", orderBy = "prefCode")
    @DisplayName("指定の都道府県コードが存在する場合は、対応する都道府県データを削除すること")
    void deletePref() {
        prefectureMapper.deletePref("47");
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @ExpectedDataSet(value = "datasets/prefectures.yml", orderBy = "prefCode")
    @DisplayName("指定の都道府県コードが存在しない場合は何も変更しないこと")
    void deletePrefToDoNothing() {
        prefectureMapper.deletePref("06");
    }

}
