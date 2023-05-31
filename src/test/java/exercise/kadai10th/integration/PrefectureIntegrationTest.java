package exercise.kadai10th.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import exercise.kadai10th.requestform.PrefectureRequestForm;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Order(11)
@AutoConfigureMockMvc
@SpringBootTest
@DBRider
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

    @Nested
    @DisplayName("Method: getPrefByCode")
    class GetPrefByCodeTest {
        @Test
        @DataSet(value = "datasets/prefectures.yml", transactional = true)
        @DisplayName("Should get a corresponding prefecture by code when exists \n"
                + "都道府県コードに対応する都道府県がある場合はデータ取得できること")
        void getPrefByCodeNormally() throws Exception {
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
        @DisplayName("Should throw NoResourceException when no corresponding prefecture exists \n"
                + "都道府県コードに対応する都道府県が存在しない場合はエラー情報を返すこと")
        @DataSet(value = "datasets/prefectures.yml", transactional = true)
        void throwWhenNoPref() throws Exception {
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
    }

    @Nested
    @DisplayName("Method: getPrefByName")
    class GetPrefByNameTest {
        @Test
        @DataSet(value = "datasets/prefectures.yml", transactional = true)
        @DisplayName("Should get a corresponding prefecture by name when exists \n"
                + "都道府県名に対応する都道府県があればデータ取得できること")
        void getPrefByNameNormally() throws Exception {
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
        @DataSet(value = "datasets/prefectures.yml", transactional = true)
        @DisplayName("Should throw NoResourceException when no corresponding prefecture exists \n"
                + "都道府県名に対応する都道府県が存在しない場合はエラー情報を返すこと")
        void throwWhenNoPref() throws Exception {
            String actualResult = mockMvc
                    .perform(get("/prefectures/names?prefName=東京都"))
                    .andExpect(status().isNotFound())
                    .andReturn()
                    .getResponse()
                    .getContentAsString(StandardCharsets.UTF_8);

            String expectedResult = objectMapper.readTree(getJsonFileData("exception-no-resource-tokyo.json")).toString();

            JSONAssert.assertEquals(expectedResult, actualResult,
                    new CustomComparator(
                            JSONCompareMode.STRICT,
                            new Customization("timestamp", (expected, actual) -> true)
                    ));
        }
    }

    @Nested
    @DisplayName("Method: getAllPrefs")
    class GetAllPrefsTest {
        @Test
        @DataSet(value = "datasets/prefectures.yml", transactional = true)
        @DisplayName("Should get a list of all prefectures when exists \n"
                + "登録済みの都道府県がある場合全て取得できること")
        void getAllPrefsNormally() throws Exception {
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
        @DataSet(value = "datasets/prefecture-empty.yml", transactional = true)
        @DisplayName("Should get a empty list when no prefecture exists \n"
                + "登録済みの都道府県が存在しない場合は空のリストを返すこと")
        void getEmptyListWhenNoPref() throws Exception {
            String actualResult = mockMvc
                    .perform(get("/prefectures"))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString(StandardCharsets.UTF_8);

            String expectedResult = objectMapper.readTree(getJsonFileData("prefecture-empty.json")).toString();
            JSONAssert.assertEquals(expectedResult, actualResult, JSONCompareMode.STRICT);
        }
    }

    @Nested
    @DisplayName("Method: createPref")
    class CreatePrefTest {
        @Test
        @DataSet(value = "datasets/prefectures.yml", transactional = true)
        @ExpectedDataSet(value = "datasets/prefecture-insert.yml", orderBy = "prefCode")
        @DisplayName("Should add a prefecture when its code is unique \n"
                + "都道府県コードが既存のものと重複しない場合、都道府県を追加できること")
        void createPrefNormally() throws Exception {
            String actualResult = mockMvc
                    .perform(post("/prefectures")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(
                                    new PrefectureRequestForm("05", "秋田県"))))
                    .andExpect(status().isCreated())
                    .andExpect(redirectedUrl("http://localhost/prefectures/05"))
                    .andReturn()
                    .getResponse()
                    .getContentAsString(StandardCharsets.UTF_8);

            String expectedResult = objectMapper.readTree(getJsonFileData("prefecture-create.json")).toString();
            System.out.println("actual:  " + actualResult);
            System.out.println("expected:" + expectedResult);
            JSONAssert.assertEquals(expectedResult, actualResult, JSONCompareMode.STRICT);
        }

        @Test
        @DataSet(value = "datasets/prefectures.yml", transactional = true)
        @ExpectedDataSet(value = "datasets/prefectures.yml", orderBy = "prefCode")
        @DisplayName("Should throw DuplicateCodeException when a code already exists \n"
                + "都道府県コードが既存のものと重複する場合はエラー情報を返すこと")
        void throwWhenCodeDuplicates() throws Exception {
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
    }

    @Nested
    @DisplayName("Method: updatePref")
    class UpdatePrefTest {
        @Test
        @DataSet(value = "datasets/prefectures.yml", transactional = true)
        @ExpectedDataSet(value = "datasets/prefecture-update.yml", orderBy = "prefCode")
        @DisplayName("Should rename a prefecture when its code exists and new name differs from current one \n"
                + "都道府県コードに対応する都道府県があり、かつ従前と異なれば、都道府県名を更新できること")
        void updatePrefNormally() throws Exception {
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
        @DataSet(value = "datasets/prefectures.yml", transactional = true)
        @ExpectedDataSet(value = "datasets/prefectures.yml", orderBy = "prefCode")
        @DisplayName("Should throw SameAsCurrentException when no change of the name of existing prefecture \n"
                + "都道府県コードに対応する都道府県はあるが、都道府県名が従前と同等の場合、エラー情報を返すこと")
        void throwWhenNoChange() throws Exception {
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
        @DataSet(value = "datasets/prefectures.yml", transactional = true)
        @ExpectedDataSet(value = "datasets/prefectures.yml", orderBy = "prefCode")
        @DisplayName("Should throw NoResourceException when no corresponding prefecture exists \n"
                + "都道府県コードに対応する都道府県が存在しない場合はエラー情報を返すこと")
        void throwWhenNoPref() throws Exception {
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
    }

    @Nested
    @DisplayName("Method: deletePref")
    class DeletePrefTest {
        @Test
        @DataSet(value = "datasets/prefectures.yml, datasets/airports.yml", transactional = true)
        @ExpectedDataSet(value = "datasets/prefecture-delete.yml, datasets/airports.yml", orderBy = "prefCode")
        @DisplayName("Should delete a corresponding prefecture when it exists and no airport is registered \n"
                + "都道府県コードに対応する都道府県があり、かつその都道府県に空港が存在しない場合、都道府県データを削除できること")
        void deletePrefNormally() throws Exception {
            mockMvc.perform(delete("/prefectures/47"))
                    .andExpect(status().isNoContent())
                    .andReturn()
                    .getResponse()
                    .getContentAsString(StandardCharsets.UTF_8);
        }

        @Test
        @DataSet(value = "datasets/prefectures.yml, datasets/airports.yml", transactional = true)
        @ExpectedDataSet(value = "datasets/prefectures.yml, datasets/airports.yml", orderBy = "prefCode")
        @DisplayName("Should throw CodeInUseException when the prefecture has one or more airports \n"
                + "都道府県コードに対応する都道府県があり、かつその都道府県に空港がある場合、エラー情報を返すこと")
        void throwWhenCodeUsed() throws Exception {
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
        @DataSet(value = "datasets/prefectures.yml", transactional = true)
        @ExpectedDataSet(value = "datasets/prefectures.yml", orderBy = "prefCode")
        @DisplayName("Should throw NoResourceException when no corresponding prefecture exists \n"
                + "都道府県コードに対応する都道府県が存在しない場合はエラー情報を返すこと")
        void throwWhenNoPref() throws Exception {
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

}
