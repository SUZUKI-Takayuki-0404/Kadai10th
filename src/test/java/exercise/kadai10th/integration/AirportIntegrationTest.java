package exercise.kadai10th.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.spring.api.DBRider;
import exercise.kadai10th.controller.AirportController;
import exercise.kadai10th.entity.AirportEntity;
import exercise.kadai10th.requestform.AirportRequestForm;
import exercise.kadai10th.service.AirportService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
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
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AirportIntegrationTest {
/*
    @InjectMocks
    private AirportController airportController;

    @MockBean
    private AirportService airportService;

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
    @DisplayName("空港コードに対応する空港がある場合、空港コードから空港データを取得できること")
    void getAirportTest1() throws Exception {

    }

    @Test
    @DisplayName("空港コードに対応する空港が存在しない場合、空港データが存在しないことをエラー情報として返すこと")
    void getAirportTest2() throws Exception {

    }

    @Test
    @DisplayName("都道府県に空港がある場合、都道府県名から該当する空港データを取得できること")
    void getAirportsInPrefTest1() throws Exception {

    }

    @Test
    @DisplayName("都道府県に空港が存在しない場合、空のデータを返すこと")
    void getAirportsInPrefTest2() throws Exception {

    }

    @Test
    @DisplayName("登録済みの空港がある場合、登録済みの全ての空港データを取得できること")
    void getAllAirportsTest1() throws Exception {

    }

    @Test
    @DisplayName("登録済みの空港が存在しない場合、空のデータを返すこと")
    void getAllAirportsTest2() throws Exception {

    }

    @Test
    @DisplayName("空港コードが既存のものと重複せず、かつ都道府県がある場合、空港データを追加できること")
    void createAirportTest1() throws Exception {

    }

    @Test
    @DisplayName("都道府県コードに対応する都道府県が存在しない場合、都道府県データが存在しないことをエラー情報として返すこと")
    void createAirportTest2() throws Exception {

    }

    @Test
    @DisplayName("空港コードが既存のものと重複する場合、コードが重複してしまうことをエラー情報として返すこと")
    void createAirportTest3() throws Exception {

    }

    @Test
    @DisplayName("空港コードに対応する空港があり、かつ空港名が従前とは異なる場合、空港データを更新できること")
    void updateAirportTest1() throws Exception {

    }

    @Test
    @DisplayName("空港コードに対応する空港はあるが、空港名が従前と同等の場合、データの内容が従前から更新されていないことをエラー情報として返すこと")
    void updateAirportTest2() throws Exception {

    }

    @Test
    @DisplayName("空港コードに対応する空港が存在しない場合、空港データが存在しないことをエラー情報として返すこと")
    void updateAirportTest3() throws Exception {

    }

    @Test
    @DisplayName("都道府県コードに対応する都道府県が存在しない場合、都道府県データが存在しないことをエラー情報として返すこと")
    void updateAirportTest4() throws Exception {

    }

    @Test
    @DisplayName("空港コードに対応する空港がある場合、空港データを削除できること")
    void deleteAirportTest1() throws Exception {

    }

    @Test
    @DisplayName("空港コードに対応する空港が存在しない場合、空港データが存在しないことをエラー情報として返すこと")
    void deleteAirportTest2() throws Exception {

    }
*/
}
