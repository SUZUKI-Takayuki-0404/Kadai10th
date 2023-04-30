package exercise.kadai10th.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import exercise.kadai10th.entity.AirportEntity;
import exercise.kadai10th.service.AirportService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AirportController.class)
@ExtendWith(MockitoExtension.class)
class AirportControllerTest {

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
    @DisplayName("空港データを取得した場合、レスポンスコード200および空港のjson形式データを返す")
    void getAirportTest1() throws Exception {

        doReturn(new AirportEntity("CTS", "新千歳空港", "01", "北海道"))
                .when(airportService)
                .getAirport("CTS");

        String actualResult = mockMvc
                .perform(get("/airports/codes/CTS"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        String expectedResult = objectMapper.readTree(getJsonFileData("airport1.json")).toString();

        JSONAssert.assertEquals(expectedResult, actualResult, JSONCompareMode.STRICT);
    /*
        指定した空港コードに対応する空港データを取得できた場合 200
        指定した空港コードに対応する空港データが存在しなかった場合 404
    */
    }

    @Test
    @Disabled
    void getAirportsInPrefTest1() {

    /*
        指定した都道府県に対応する空港データを取得できた場合 200
        指定した都道府県が存在しなかった場合 404
    */
    }

    @Test
    @Disabled
    void getAllAirportsTest1() {

    /*
        ステータスコード 200
    */
    }

    @Test
    @Disabled
    void createAirportTest1() {

    /*
        指定した空港コードに対応する新規の空港データを追加できた場合 201
        指定した空港コードに対応する新規の都道府県データを追加できなかった場合 409
        指定した都道府県コードに対応する都道府県データが存在しなかった場合 404
    */
    }

    @Test
    @Disabled
    void updateAirportTest1() {

    /*
        指定した空港コードに対応する空港データを更新できた場合 204
        指定した空港コードに対応する空港データを更新できなかった場合 409
        指定した都道府県コードに対応する都道府県データが存在しなかった場合 404
        指定した空港コードに対応する空港データが存在しなかった場合 404
    */
    }

    @Test
    @Disabled
    void deleteAirportTest1() {

    /*
        指定した空港コードに対応する都道府県データを削除できた場合 204
        指定した空港コードに対応する空港データが存在しなかった場合 404
    */
    }
}
