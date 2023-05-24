package exercise.kadai10th.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import exercise.kadai10th.entity.AirportEntity;
import exercise.kadai10th.requestform.AirportRequestForm;
import exercise.kadai10th.service.AirportService;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
    @DisplayName("空港コードから空港データを取得できること")
    void getAirportByCode() throws Exception {
        doReturn(new AirportEntity("CTS", "新千歳空港", "01", "北海道"))
                .when(airportService)
                .getAirportByCode("CTS");

        String actualResult = mockMvc
                .perform(get("/airports/codes/CTS"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        String expectedResult = objectMapper.readTree(getJsonFileData("airport-cts.json")).toString();
        JSONAssert.assertEquals(expectedResult, actualResult, JSONCompareMode.STRICT);
    }

    @Test
    @DisplayName("都道府県名から該当する空港データを取得できること")
    void getAirportsByPrefName() throws Exception {
        doReturn(List.of(
                new AirportEntity("CTS", "新千歳空港", "01", "北海道"),
                new AirportEntity("HKD", "函館空港", "01", "北海道")))
                .when(airportService)
                .getAirportsByPrefName("北海道");

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
    @DisplayName("登録済みの全ての空港データを取得できること")
    void getAllAirports() throws Exception {
        doReturn(List.of(
                new AirportEntity("CTS", "新千歳空港", "01", "北海道"),
                new AirportEntity("HKD", "函館空港", "01", "北海道"),
                new AirportEntity("AOJ", "青森空港", "02", "青森県"),
                new AirportEntity("HNA", "花巻空港", "03", "岩手県"),
                new AirportEntity("SDJ", "仙台空港", "04", "宮城県")))
                .when(airportService)
                .getAllAirports();

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
    @DisplayName("空港データを追加できること")
    void createAirport() throws Exception {
        doReturn(new AirportEntity("OKA", "那覇空港", "47", "沖縄県"))
                .when(airportService)
                .createAirport("OKA", "那覇空港", "47");

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
        JSONAssert.assertEquals(expectedResult, actualResult, JSONCompareMode.STRICT);
    }

    @Test
    @DisplayName("空港データを更新できること")
    void updateAirport() throws Exception {
        doNothing().when(airportService).updateAirport("SDJ", "仙台国際空港", "04");

        mockMvc.perform(patch("/airports/SDJ")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new AirportRequestForm("SDJ", "仙台国際空港", "04"))))
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        verify(airportService, times(1)).updateAirport("SDJ", "仙台国際空港", "04");
    }

    @Test
    @DisplayName("空港データを削除できること")
    void deleteAirport() throws Exception {
        doNothing().when(airportService).deleteAirport("SDJ");

        mockMvc.perform(delete("/airports/SDJ"))
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        verify(airportService, times(1)).deleteAirport("SDJ");
    }
}
