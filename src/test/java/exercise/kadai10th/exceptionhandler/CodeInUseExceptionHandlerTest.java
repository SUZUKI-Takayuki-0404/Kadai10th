package exercise.kadai10th.exceptionhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import exercise.kadai10th.controller.PrefectureController;
import exercise.kadai10th.service.PrefectureService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PrefectureController.class)
@ExtendWith(MockitoExtension.class)
class CodeInUseExceptionHandlerTest {

    @InjectMocks
    private PrefectureController prefectureController;

    @MockBean
    PrefectureService prefectureService;

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
    @DisplayName("CodeInUseExceptionがスローされた場合、" +
            "ステータスコード409および都道府県データが空港データ内で使用中であることを示すレスポンスを返す")
    void handleCodeInUseExceptionTest1() throws Exception {
        doThrow(new CodeInUseException("11 : This code is in use"))
                .when(prefectureService)
                .deletePref("11");

        String actualResult = mockMvc
                .perform(delete("/prefectures/11"))
                .andExpect(status().isConflict())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertThat(actualResult).contains("11 : This code is in use");
        System.out.println(actualResult);
//        String expectedResult = objectMapper.readTree(getJsonFileData("exception-code-in-use.json")).toString();
//        JSONAssert.assertEquals(expectedResult, actualResult, JSONCompareMode.STRICT);
    }
}
