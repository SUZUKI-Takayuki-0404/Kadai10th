package exercise.kadai10th.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import exercise.kadai10th.entity.PrefectureEntity;
import exercise.kadai10th.response.PrefectureResponse;
import exercise.kadai10th.service.AirportService;
import exercise.kadai10th.service.PrefectureService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.skyscreamer.jsonassert.JSONAssert;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class PrefectureControllerTest {

    @InjectMocks
    private PrefectureController prefectureController;

    @Mock
    PrefectureService prefectureService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private ResourceLoader resourceLoader;

    private final String BASE_PATH = "json-datasets/";

    private String getJsonFileData(String fileName) throws IOException {
        var jsonResult = resourceLoader.getResource("classpath:" + BASE_PATH + fileName);
        return StreamUtils.copyToString(jsonResult.getInputStream(), StandardCharsets.UTF_8);
    }

    @Test
//    @Disabled
    @DisplayName("指定した都道府県コードに対応する都道府県データを取得できた場合、"
            + "取得データjson形式で返し、かつステータスコード200を返す")
    void getPrefByCodeTest1() throws Exception {

        doReturn(new PrefectureEntity("01", "北海道"))
                .when(prefectureService)
                .getPrefByCode("01");

        String expectedResponse = objectMapper.readTree(getJsonFileData("prefectures1.json")).toString();
        String actualResponse = prefectureController.getPrefByCode("01").toString();

        JSONAssert.assertEquals(expectedResponse, actualResponse, JSONCompareMode.LENIENT);
    /*
        指定した都道府県コードに対応する都道府県データを取得できた場合 200
        指定した都道府県コードに対応する都道府県データが存在しなかった場合 404
    */
    }

    @Test
    @Disabled
    void getPrefByNameTest1() {

    /*
        指定した都道府県名に対応する都道府県データを取得できた場合 200
        指定した都道府県名に対応する都道府県データが存在しなかった場合 404
    */
    }

    @Test
    @Disabled
    void getAllPrefsTest1() {

    /*
        ステータスコード 200
    */
    }

    @Test
    @Disabled
    void createPrefTest1() {

    /*
        指定した都道府県コードに対応する新規の都道府県データを追加できた場合 201
        指定した都道府県コードに対応する新規の都道府県データを追加できなかった場合 409
    */
    }

    @Test
    @Disabled
    void updatePrefTest1() {

    /*
        指定した都道府県コードに対応する都道府県データを更新できた場合 204
        指定した都道府県コードに対応する新規の都道府県データを更新できなかった場合 409
        指定した都道府県コードに対応する都道府県データが存在しなかった場合 404
    */
    }

    @Test
    @Disabled
    void deletePrefTest1() {

    /*
        指定した都道府県コードに対応する都道府県データを削除できた場合 204
        指定した都道府県コードに対応する都道府県データを削除できなかった場合 409
        指定した都道府県コードに対応する都道府県データが存在しなかった場合 404
    */
    }
}
