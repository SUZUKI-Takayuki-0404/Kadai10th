package exercise.kadai10th.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import exercise.kadai10th.controller.PrefectureController;
import exercise.kadai10th.entity.PrefectureEntity;
import exercise.kadai10th.mapper.PrefectureMapper;
import exercise.kadai10th.requestform.AirportRequestForm;
import exercise.kadai10th.requestform.PrefectureRequestForm;
import exercise.kadai10th.service.PrefectureService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.github.database.rider.core.api.configuration.Orthography.LOWERCASE;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@DBRider
@DBUnit(caseInsensitiveStrategy = LOWERCASE, schema = "airport_database",
        url = "jdbc:mysql://localhost:3307/airport_database",
        user = "user", password = "password")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PrefectureIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ResourceLoader resourceLoader;

    private final String BASE_PATH = "json-datasets/";

    private String getJsonFileData(String fileName) throws IOException {
        var jsonResult = resourceLoader.getResource("classpath:" + BASE_PATH + fileName);
        return StreamUtils.copyToString(jsonResult.getInputStream(), StandardCharsets.UTF_8);
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @Transactional
    @DisplayName("都道府県コードに対応する都道府県がある場合、都道府県コードから都道府県データを取得できること")
    void getPrefByCode() throws Exception {
        String actualResult = mockMvc
                .perform(get("/prefectures/01"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        String expectedResult = objectMapper.readTree(getJsonFileData("prefecture-01.json")).toString();
        JSONAssert.assertEquals(expectedResult, actualResult, JSONCompareMode.STRICT);
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @Transactional
    @DisplayName("都道府県コードに対応する都道府県が存在しない場合、都道府県データが存在しないことをエラー情報として返すこと")
    void getPrefByCodeTest2() throws Exception {
        String actualResult = mockMvc
                .perform(get("/prefectures/13"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        String expectedResult = objectMapper.readTree(getJsonFileData("exception-no-resource-13.json")).toString();

        JSONAssert.assertEquals(expectedResult, actualResult,
                new CustomComparator(
                        JSONCompareMode.STRICT,
                        new Customization("path", (expected, actual) -> true),
                        new Customization("timestamp", (expected, actual) -> true)
                ));
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @Transactional
    @DisplayName("都道府県名に対応する都道府県がある場合、都道府県名から都道府県データを取得できること")
    void getPrefByName() throws Exception {
        String actualResult = mockMvc
                .perform(get("/prefectures/names?prefName=青森県"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        String expectedResult = objectMapper.readTree(getJsonFileData("prefecture-aomori.json")).toString();
        JSONAssert.assertEquals(expectedResult, actualResult, JSONCompareMode.STRICT);
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @Transactional
    @DisplayName("都道府県名に対応する都道府県が存在しない場合、都道府県データが存在しないことをエラー情報として返すこと")
    void getPrefByNameTest2() throws Exception {
        String actualResult = mockMvc
                .perform(get("/prefectures/names?prefName=東京都"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        String expectedResult = objectMapper.readTree(getJsonFileData("exception-no-resource-13.json")).toString();

        JSONAssert.assertEquals(expectedResult, actualResult,
                new CustomComparator(
                        JSONCompareMode.STRICT,
                        new Customization("path", (expected, actual) -> true),
                        new Customization("timestamp", (expected, actual) -> true)
                ));
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @Transactional
    @DisplayName("登録済みの都道府県がある場合、登録済みの全ての都道府県データを取得できること")
    void getAllPrefs() throws Exception {
        String actualResult = mockMvc
                .perform(get("/prefectures"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        String expectedResult = objectMapper.readTree(getJsonFileData("prefecture-all.json")).toString();
        JSONAssert.assertEquals(expectedResult, actualResult, JSONCompareMode.STRICT);
    }

    @Test
    @DataSet(value = "datasets/prefecture-empty.yml")
    @Transactional
    @DisplayName("登録済みの都道府県が存在しない場合、空のデータを返すこと")
    void getAllPrefsTest2() throws Exception {
        String actualResult = mockMvc
                .perform(get("/prefectures"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        String expectedResult = objectMapper.readTree(getJsonFileData("prefecture-empty.json")).toString();
        JSONAssert.assertEquals(expectedResult, actualResult, JSONCompareMode.STRICT);
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @ExpectedDataSet(value = "datasets/prefecture-insert.yml", orderBy = "prefCode")
    @Transactional
    @DisplayName("都道府県コードが既存のものと重複しない場合、都道府県データを追加できること")
    void createPref() throws Exception {
        String actualResult = mockMvc
                .perform(post("/prefectures")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new PrefectureRequestForm("06", "山形県"))))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl("http://localhost/prefectures/05"))
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        String expectedResult = objectMapper.readTree(getJsonFileData("prefecture-create.json")).toString();
        JSONAssert.assertEquals(expectedResult, actualResult, JSONCompareMode.STRICT);
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @ExpectedDataSet(value = "datasets/prefectures.yml", orderBy = "prefCode")
    @Transactional
    @DisplayName("都道府県コードが既存のものと重複する場合、コードが重複してしまうことをエラー情報として返すこと")
    void createPrefTest2() throws Exception {
        String actualResult = mockMvc
                .perform(post("/prefectures")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new PrefectureRequestForm("04", "宮城県"))))
                .andExpect(status().isConflict())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        String expectedResult = objectMapper.readTree(getJsonFileData("exception-duplicate-code-04.json")).toString();

        JSONAssert.assertEquals(expectedResult, actualResult,
                new CustomComparator(
                        JSONCompareMode.STRICT,
                        new Customization("timestamp", (expected, actual) -> true)
                ));
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @ExpectedDataSet(value = "datasets/prefecture-update.yml", orderBy = "prefCode")
    @Transactional
    @DisplayName("都道府県コードに対応する都道府県があり、かつ都道府県名が従前と異なる場合、都道府県データを更新できること")
    void updatePref() throws Exception {
        mockMvc.perform(patch("/prefectures/02")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new PrefectureRequestForm("02", "あおもりけん"))))
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @ExpectedDataSet(value = "datasets/prefectures.yml", orderBy = "prefCode")
    @Transactional
    @DisplayName("都道府県コードに対応する都道府県はあるが、都道府県名が従前と同等の場合、データの内容が従前から更新されていないことをエラー情報として返すこと")
    void updatePrefTest2() throws Exception {
        String actualResult = mockMvc
                .perform(patch("/prefectures/02")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new PrefectureRequestForm("02", "青森県"))))
                .andExpect(status().isConflict())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        String expectedResult = objectMapper.readTree(getJsonFileData("exception-same-as-current-02.json")).toString();

        JSONAssert.assertEquals(expectedResult, actualResult,
                new CustomComparator(
                        JSONCompareMode.STRICT,
                        new Customization("timestamp", (expected, actual) -> true)
                ));
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @ExpectedDataSet(value = "datasets/prefectures.yml", orderBy = "prefCode")
    @Transactional
    @DisplayName("都道府県コードに対応する都道府県が存在しない場合、都道府県データが存在しないことをエラー情報として返すこと")
    void updatePrefTest3() throws Exception {
        String actualResult = mockMvc
                .perform(patch("/prefectures/13")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new PrefectureRequestForm("13", "とうきょうと"))))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        String expectedResult = objectMapper.readTree(getJsonFileData("exception-no-resource-13.json")).toString();

        JSONAssert.assertEquals(expectedResult, actualResult,
                new CustomComparator(
                        JSONCompareMode.STRICT,
                        new Customization("path", (expected, actual) -> true),
                        new Customization("timestamp", (expected, actual) -> true)
                ));
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml, datasets/airports.yml")
    @ExpectedDataSet(value = "datasets/prefecture-delete.yml, datasets/airports.yml", orderBy = "prefCode")
    @Transactional
    @DisplayName("都道府県コードに対応する都道府県があり、かつその都道府県に空港が存在しない場合、都道府県データを削除できること")
    void deletePref() throws Exception {
        mockMvc.perform(delete("/prefectures/05"))
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml, datasets/airport-empty.yml")
    @ExpectedDataSet(value = "datasets/prefectures.yml, datasets/airport-empty.yml", orderBy = "prefCode")
    @Transactional
    @DisplayName("都道府県コードに対応する都道府県があり、かつその都道府県に空港がある場合、都道府県データが空港データ内で使用中であることをエラー情報として返すこと")
    void deletePrefTest2() throws Exception {
        String actualResult = mockMvc
                .perform(delete("/prefectures/01"))
                .andExpect(status().isConflict())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        String expectedResult = objectMapper.readTree(getJsonFileData("exception-code-in-use.json")).toString();

        JSONAssert.assertEquals(expectedResult, actualResult,
                new CustomComparator(
                        JSONCompareMode.STRICT,
                        new Customization("timestamp", (expected, actual) -> true)
                ));
    }

    @Test
    @DataSet(value = "datasets/prefectures.yml")
    @ExpectedDataSet(value = "datasets/prefectures.yml", orderBy = "prefCode")
    @Transactional
    @DisplayName("都道府県コードに対応する都道府県が存在しない場合、都道府県データが存在しないことをエラー情報として返すこと")
    void deletePrefTest3() throws Exception {
        String actualResult = mockMvc
                .perform(delete("/prefectures/13"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        String expectedResult = objectMapper.readTree(getJsonFileData("exception-no-resource-13.json")).toString();

        JSONAssert.assertEquals(expectedResult, actualResult,
                new CustomComparator(
                        JSONCompareMode.STRICT,
                        new Customization("path", (expected, actual) -> true),
                        new Customization("timestamp", (expected, actual) -> true)
                ));
    }

}
