package exercise.kadai10th.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import exercise.kadai10th.requestform.AirportRequestForm;
import org.junit.jupiter.api.DisplayName;
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
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.github.database.rider.core.api.configuration.Orthography.LOWERCASE;
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

    @Test
    @DataSet(value = "datasets/airports.yml, datasets/prefectures.yml", transactional = true)
    @DisplayName("空港コードに対応する空港がある場合、空港コードから空港データを取得できること")
    void getAirport() throws Exception {
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
    @DataSet(value = "datasets/airports.yml, datasets/prefectures.yml", transactional = true)
    @DisplayName("空港コードに対応する空港が存在しない場合、空港データが存在しないことをエラー情報として返すこと")
    void getAirportTest2() throws Exception {
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

    @Test
    @DataSet(value = "datasets/airports.yml, datasets/prefectures.yml", transactional = true)
    @DisplayName("都道府県に空港がある場合、都道府県名から該当する空港データを取得できること")
    void getAirportsInPref() throws Exception {
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
    @DataSet(value = "datasets/airports.yml, datasets/prefectures.yml", transactional = true)
    @DisplayName("都道府県に空港が存在しない場合、空のデータを返すこと")
    void getAirportsInPrefTest2() throws Exception {
        String actualResult = mockMvc
                .perform(get("/airports/prefectures?prefName=埼玉県"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        String expectedResult = objectMapper.readTree(getJsonFileData("airport-saitama-empty.json")).toString();
        JSONAssert.assertEquals(expectedResult, actualResult, JSONCompareMode.STRICT);
    }

    @Test
    @DataSet(value = "datasets/airports.yml, datasets/prefectures.yml", transactional = true)
    @DisplayName("登録済みの空港がある場合、登録済みの全ての空港データを取得できること")
    void getAllAirports() throws Exception {
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
    @DataSet(value = "datasets/airport-empty.yml, datasets/prefectures.yml", transactional = true)
    @DisplayName("登録済みの空港が存在しない場合、空のデータを返すこと")
    void getAllAirportsTest2() throws Exception {
        String actualResult = mockMvc
                .perform(get("/airports"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        String expectedResult = objectMapper.readTree(getJsonFileData("airport-empty.json")).toString();
        JSONAssert.assertEquals(expectedResult, actualResult, JSONCompareMode.STRICT);
    }

    @Test
    @DataSet(value = "datasets/airports.yml, datasets/prefectures.yml", transactional = true)
    @ExpectedDataSet(value = "datasets/airport-insert.yml", orderBy = "airportCode")
    @DisplayName("空港コードが既存のものと重複せず、かつ都道府県がある場合、空港データを追加できること")
    void createAirport() throws Exception {
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
    @DataSet(value = "datasets/airports.yml", transactional = true)
    @ExpectedDataSet(value = "datasets/airports.yml", orderBy = "airportCode")
    @DisplayName("空港コードが既存のものと重複する場合、コードが重複してしまうことをエラー情報として返すこと")
    void createAirportTest2() throws Exception {
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
    @DataSet(value = "datasets/airports.yml", transactional = true)
    @ExpectedDataSet(value = "datasets/airports.yml", orderBy = "airportCode")
    @DisplayName("都道府県コードに対応する都道府県が存在しない場合、都道府県データが存在しないことをエラー情報として返すこと")
    void createAirportTest3() throws Exception {
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

    @Test
    @DataSet(value = "datasets/airports.yml", transactional = true)
    @ExpectedDataSet(value = "datasets/airport-update.yml", orderBy = "airportCode")
    @DisplayName("空港コードに対応する空港があり、かつ空港名が従前とは異なる場合、空港データを更新できること")
    void updateAirport() throws Exception {
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
    @DataSet(value = "datasets/airports.yml", transactional = true)
    @ExpectedDataSet(value = "datasets/airports.yml", orderBy = "airportCode")
    @DisplayName("空港コードに対応する空港はあるが、空港名が従前と同等の場合、データの内容が従前から更新されていないことをエラー情報として返すこと")
    void updateAirportTest2() throws Exception {
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
    @DataSet(value = "datasets/airports.yml", transactional = true)
    @ExpectedDataSet(value = "datasets/airports.yml", orderBy = "airportCode")
    @DisplayName("空港コードに対応する空港が存在しない場合、空港データが存在しないことをエラー情報として返すこと")
    void updateAirportTest3() throws Exception {
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
    @DataSet(value = "datasets/airports.yml, datasets/prefectures.yml", transactional = true)
    @ExpectedDataSet(value = "datasets/airports.yml, datasets/prefectures.yml", orderBy = "airportCode")
    @DisplayName("都道府県コードに対応する都道府県が存在しない場合、都道府県データが存在しないことをエラー情報として返すこと")
    void updateAirportTest4() throws Exception {
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

    @Test
    @DataSet(value = "datasets/airports.yml", transactional = true)
    @ExpectedDataSet(value = "datasets/airport-delete.yml", orderBy = "airportCode")
    @DisplayName("空港コードに対応する空港がある場合、空港データを削除できること")
    void deleteAirport() throws Exception {
        mockMvc.perform(delete("/airports/SDJ"))
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
    }

    @Test
    @DataSet(value = "datasets/airports.yml", transactional = true)
    @ExpectedDataSet(value = "datasets/airports.yml", orderBy = "airportCode")
    @DisplayName("空港コードに対応する空港が存在しない場合、空港データが存在しないことをエラー情報として返すこと")
    void deleteAirportTest2() throws Exception {
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
