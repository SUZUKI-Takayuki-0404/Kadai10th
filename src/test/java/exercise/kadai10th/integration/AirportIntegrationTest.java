package exercise.kadai10th.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import exercise.kadai10th.requestform.AirportRequestForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@DBRider
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class AirportIntegrationTest {

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
    class GetAirportTest {
        @Test
        @DataSet(value = "datasets/airports.yml, datasets/prefectures.yml")
        @DisplayName("""
                Should get a corresponding airport by code when exists
                空港コードに対応する空港がある場合は取得できること
                """)
        void workNormally() throws Exception {
            String actualResult = mockMvc
                    .perform(MockMvcRequestBuilders.get("/airports/codes/CTS"))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString(StandardCharsets.UTF_8);

            String expectedResult = objectMapper.readTree(getJsonFileData("airport-cts.json")).toString();
            JSONAssert.assertEquals(expectedResult, actualResult, JSONCompareMode.STRICT);
        }

        @Test
        @DataSet(value = "datasets/airports.yml, datasets/prefectures.yml")
        @DisplayName("""
                Should return error information when no corresponding airport exists
                空港コードに対応する空港が無い場合はエラー情報を返すこと
                """)
        void returnWhenNoAirport() throws Exception {
            String actualResult = mockMvc
                    .perform(get("/airports/codes/WKJ"))
                    .andExpect(status().isNotFound())
                    .andReturn()
                    .getResponse()
                    .getContentAsString(StandardCharsets.UTF_8);

            String expectedResult = objectMapper.readTree(getJsonFileData("exception-no-resource-wkj.json")).toString();

            JSONAssert.assertEquals(expectedResult, actualResult,
                    new CustomComparator(
                            JSONCompareMode.STRICT,
                            new Customization("path", (expected, actual) -> true),
                            new Customization("timestamp", (expected, actual) -> true)
                    ));
        }
    }

    @Nested
    class GetAirportsInPrefTest {
        @Test
        @DataSet(value = "datasets/airports.yml, datasets/prefectures.yml")
        @DisplayName("""
                Should get a list of airports in a prefecture when exist
                その都道府県に空港がある場合は全て取得できること
                """)
        void workNormally() throws Exception {
            String actualResult = mockMvc
                    .perform(get("/airports/prefectures?prefName=北海道"))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString(StandardCharsets.UTF_8);

            String expectedResult = objectMapper.readTree(getJsonFileData("airport-hokkaido.json")).toString();
            JSONAssert.assertEquals(expectedResult, actualResult, JSONCompareMode.STRICT);
        }

        @Test
        @DataSet(value = "datasets/airports.yml, datasets/prefectures.yml")
        @DisplayName("""
                Should get a empty list when no airport exists
                都道府県に空港が無い場合は空のリストを返すこと
                """)
        void returnEmptyWhenNoAirport() throws Exception {
            String actualResult = mockMvc
                    .perform(get("/airports/prefectures?prefName=埼玉県"))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString(StandardCharsets.UTF_8);

            String expectedResult = objectMapper.readTree(getJsonFileData("airport-saitama-empty.json")).toString();
            JSONAssert.assertEquals(expectedResult, actualResult, JSONCompareMode.STRICT);
        }
    }

    @Nested
    class GetAllAirportsTest {
        @Test
        @DataSet(value = "datasets/airports.yml, datasets/prefectures.yml")
        @DisplayName("""
                Should get a list of all airports when exist
                登録済みの空港がある場合は全て取得できること
                """)
        void workNormally() throws Exception {
            String actualResult = mockMvc
                    .perform(get("/airports"))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString(StandardCharsets.UTF_8);

            String expectedResult = objectMapper.readTree(getJsonFileData("airport-all.json")).toString();
            JSONAssert.assertEquals(expectedResult, actualResult, JSONCompareMode.STRICT);
        }

        @Test
        @DataSet(value = "datasets/airport-empty.yml, datasets/prefectures.yml")
        @DisplayName("""
                Should get a empty list when no airport exists
                登録済みの空港が無い場合は空のリストを返すこと
                """)
        void getEmptyWhenNoAirport() throws Exception {
            String actualResult = mockMvc
                    .perform(get("/airports"))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString(StandardCharsets.UTF_8);

            String expectedResult = objectMapper.readTree(getJsonFileData("airport-empty.json")).toString();
            JSONAssert.assertEquals(expectedResult, actualResult, JSONCompareMode.STRICT);
        }
    }

    @Nested
    class CreateAirportTest {
        @Test
        @DataSet(value = "datasets/airports.yml, datasets/prefectures.yml")
        @ExpectedDataSet(value = "datasets/airport-insert.yml", orderBy = "airportCode")
        @DisplayName("""
                Should add an airport when its code is unique and located prefecture exists
                空港コードが既存のものと重複せず、かつ所在の都道府県がある場合、空港データを追加できること
                """)
        void workNormally() throws Exception {
            String actualResult = mockMvc
                    .perform(post("/airports")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(
                                    new AirportRequestForm("OKA", "那覇空港", "47"))))
                    .andExpect(status().isCreated())
                    .andExpect(redirectedUrl("http://localhost/airports/OKA"))
                    .andReturn()
                    .getResponse()
                    .getContentAsString(StandardCharsets.UTF_8);

            String expectedResult = objectMapper.readTree(getJsonFileData("airport-create.json")).toString();
            System.out.println("actual:  " + actualResult);
            System.out.println("expected:" + expectedResult);
            JSONAssert.assertEquals(expectedResult, actualResult, JSONCompareMode.STRICT);
        }

        @Test
        @DataSet(value = "datasets/airports.yml, datasets/prefectures.yml")
        @ExpectedDataSet(value = "datasets/airports.yml", orderBy = "airportCode")
        @DisplayName("""
                Should return error information when a code already exists
                空港コードが既存のものと重複する場合はエラー情報を返すこと
                """)
        void returnWhenCodeDuplicates() throws Exception {
            String actualResult = mockMvc
                    .perform(post("/airports")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(
                                    new AirportRequestForm("HNA", "花巻空港", "03"))))
                    .andExpect(status().isConflict())
                    .andReturn()
                    .getResponse()
                    .getContentAsString(StandardCharsets.UTF_8);

            String expectedResult = objectMapper.readTree(getJsonFileData("exception-duplicate-code-hna.json")).toString();

            JSONAssert.assertEquals(expectedResult, actualResult,
                    new CustomComparator(
                            JSONCompareMode.STRICT,
                            new Customization("timestamp", (expected, actual) -> true)
                    ));
        }

        @Test
        @DataSet(value = "datasets/airports.yml, datasets/prefectures.yml")
        @ExpectedDataSet(value = "datasets/airports.yml", orderBy = "airportCode")
        @DisplayName("""
                Should return error information when no corresponding prefecture exists
                都道府県コードに対応する都道府県が無い場合はエラー情報を返すこと
                """)
        void returnWhenNoPref() throws Exception {
            String actualResult = mockMvc
                    .perform(post("/airports")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(
                                    new AirportRequestForm("NRT", "成田国際空港", "13"))))
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
    class UpdateAirportTest {
        @Test
        @DataSet(value = "datasets/airports.yml, datasets/prefectures.yml")
        @ExpectedDataSet(value = "datasets/airport-update.yml", orderBy = "airportCode")
        @DisplayName("""
                Should rename an airport when its code exists and new name differs from current one
                空港コードに対応する空港があり、かつ空港名が従前とは異なる場合は空港データを更新できること
                """)
        void workNormally() throws Exception {
            mockMvc.perform(patch("/airports/SDJ")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(
                                    new AirportRequestForm("SDJ", "仙台国際空港", "04"))))
                    .andExpect(status().isNoContent())
                    .andReturn()
                    .getResponse()
                    .getContentAsString(StandardCharsets.UTF_8);
        }

        @Test
        @DataSet(value = "datasets/airports.yml")
        @ExpectedDataSet(value = "datasets/airports.yml", orderBy = "airportCode")
        @DisplayName("""
                Should return error information when no change of the name of existing airport
                空港コードに対応する空港はあるが、空港名が従前と同等の場合はエラー情報を返すこと
                """)
        void returnWhenNoChange() throws Exception {
            String actualResult = mockMvc
                    .perform(patch("/airports/SDJ")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(
                                    new AirportRequestForm("SDJ", "仙台空港", "04"))))
                    .andExpect(status().isConflict())
                    .andReturn()
                    .getResponse()
                    .getContentAsString(StandardCharsets.UTF_8);

            String expectedResult = objectMapper.readTree(getJsonFileData("exception-same-as-current-sdj.json")).toString();

            JSONAssert.assertEquals(expectedResult, actualResult,
                    new CustomComparator(
                            JSONCompareMode.STRICT,
                            new Customization("timestamp", (expected, actual) -> true)
                    ));
        }

        @Test
        @DataSet(value = "datasets/airports.yml")
        @ExpectedDataSet(value = "datasets/airports.yml", orderBy = "airportCode")
        @DisplayName("""
                Should return error information when no corresponding airport exists
                空港コードに対応する空港が無い場合はエラー情報を返すこと
                """)
        void returnWhenNoAirport() throws Exception {
            String actualResult = mockMvc
                    .perform(patch("/airports/WKJ")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(
                                    new AirportRequestForm("WKJ", "稚内空港", "01"))))
                    .andExpect(status().isNotFound())
                    .andReturn()
                    .getResponse()
                    .getContentAsString(StandardCharsets.UTF_8);

            String expectedResult = objectMapper.readTree(getJsonFileData("exception-no-resource-wkj.json")).toString();

            JSONAssert.assertEquals(expectedResult, actualResult,
                    new CustomComparator(
                            JSONCompareMode.STRICT,
                            new Customization("path", (expected, actual) -> true),
                            new Customization("timestamp", (expected, actual) -> true)
                    ));
        }

        @Test
        @DataSet(value = "datasets/airports.yml, datasets/prefectures.yml")
        @ExpectedDataSet(value = "datasets/airports.yml, datasets/prefectures.yml", orderBy = "airportCode")
        @DisplayName("""
                Should return error information when no corresponding prefecture exists
                都道府県コードに対応する都道府県が無い場合はエラー情報を返すこと
                """)
        void returnWhenNoPref() throws Exception {
            String actualResult = mockMvc
                    .perform(patch("/airports/SDJ")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(
                                    new AirportRequestForm("SDJ", "仙台国際空港", "13"))))
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
    class DeleteAirportTest {
        @Test
        @DataSet(value = "datasets/airports.yml")
        @ExpectedDataSet(value = "datasets/airport-delete.yml", orderBy = "airportCode")
        @DisplayName("""
                Should delete a corresponding airport when exists
                空港コードに対応する空港がある場合、空港データを削除できること
                """)
        void workNormally() throws Exception {
            mockMvc.perform(delete("/airports/SDJ"))
                    .andExpect(status().isNoContent())
                    .andReturn()
                    .getResponse()
                    .getContentAsString(StandardCharsets.UTF_8);
        }

        @Test
        @DataSet(value = "datasets/airports.yml")
        @ExpectedDataSet(value = "datasets/airports.yml", orderBy = "airportCode")
        @DisplayName("""
                Should return error information when no corresponding airport exists
                空港コードに対応する空港が無い場合はエラー情報を返すこと
                """)
        void returnWhenNoAirport() throws Exception {
            String actualResult = mockMvc
                    .perform(delete("/airports/WKJ"))
                    .andExpect(status().isNotFound())
                    .andReturn()
                    .getResponse()
                    .getContentAsString(StandardCharsets.UTF_8);

            String expectedResult = objectMapper.readTree(getJsonFileData("exception-no-resource-wkj.json")).toString();

            JSONAssert.assertEquals(expectedResult, actualResult,
                    new CustomComparator(
                            JSONCompareMode.STRICT,
                            new Customization("path", (expected, actual) -> true),
                            new Customization("timestamp", (expected, actual) -> true)
                    ));
        }
    }

}
