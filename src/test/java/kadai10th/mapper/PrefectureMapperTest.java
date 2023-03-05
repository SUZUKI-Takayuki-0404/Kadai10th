package kadai10th.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PrefectureMapperTest {

    @Autowired
    PrefectureMapper prefectureMapper;

    @Test
    @Sql(
            scripts = {"classpath:/sql-annotation/delete-prefectures.sql", "classpath:/sql-annotation/insert-prefectures.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
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
    @Sql(
            scripts = {"classpath:/sql-annotation/delete-prefectures.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Transactional
    @DisplayName("都道府県データが存在しないときは空のListを返すこと")
    void findAllFromPrefsTest2() {
        assertThat(prefectureMapper.findAllFromPrefs()).isEmpty();
    }

}
