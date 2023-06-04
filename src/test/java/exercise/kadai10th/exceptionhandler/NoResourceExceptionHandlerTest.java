package exercise.kadai10th.exceptionhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import exercise.kadai10th.controller.AirportController;
import exercise.kadai10th.service.AirportService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AirportController.class)
@ExtendWith(MockitoExtension.class)
class NoResourceExceptionHandlerTest {

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
    @DisplayName("Should return error information; a corresponding data(prefecture or airport) does not exist \n"
            + "指定の都道府県データまたは空港データが存在しない事をエラー情報として返すこと")
    void handleNoResourceException() throws Exception {
        doThrow(new NoResourceException("WKJ : This code is not found"))
                .when(airportService)
                .getAirportByCode("WKJ");

        String actualResult = mockMvc
                .perform(get("/airports/codes/WKJ"))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        String expectedResult = objectMapper.readTree(getJsonFileData("exception-no-resource-wkj.json")).toString();

        //To exclude timestamp from scope of JSON comparison
        JSONAssert.assertEquals(expectedResult, actualResult,
                new CustomComparator(
                        JSONCompareMode.STRICT,
                        new Customization("timestamp", (expected, actual) -> true)));
    }
}
