package exercise.kadai10th.exceptionhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import exercise.kadai10th.controller.PrefectureController;
import exercise.kadai10th.requestform.PrefectureRequestForm;
import exercise.kadai10th.service.PrefectureService;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PrefectureController.class)
@ExtendWith(MockitoExtension.class)
class DuplicateCodeExceptionHandlerTest {

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
    @DisplayName("""
            Should return error information; the code(primary key) is already used
            コードが重複してしまう事をエラー情報として返すこと
            """)
    void handleDuplicateCodeException() throws Exception {
        doThrow(new DuplicateCodeException("04 : This code will be duplicated", new Throwable()))
                .when(prefectureService)
                .createPref("04", "宮城県");

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

        //To exclude timestamp from scope of JSON comparison
        JSONAssert.assertEquals(expectedResult, actualResult,
                new CustomComparator(
                        JSONCompareMode.STRICT,
                        new Customization("timestamp", (expected, actual) -> true)));
    }
}
