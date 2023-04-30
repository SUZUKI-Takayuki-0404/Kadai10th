package exercise.kadai10th.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import exercise.kadai10th.entity.PrefectureEntity;
import exercise.kadai10th.requestform.PrefectureRequestForm;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(PrefectureController.class)
@ExtendWith(MockitoExtension.class)
class PrefectureControllerTest {

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
    @DisplayName("都道府県コードに該当する都道府県データを取得した場合、レスポンスコード200および都道府県のjson形式データを返す")
    void getPrefByCodeTest1() throws Exception {
        doReturn(new PrefectureEntity("01", "北海道"))
                .when(prefectureService)
                .getPrefByCode("01");

        String actualResult = mockMvc
                .perform(get("/prefectures/01"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        String expectedResult = objectMapper.readTree(getJsonFileData("prefecture1.json")).toString();

        JSONAssert.assertEquals(expectedResult, actualResult, JSONCompareMode.STRICT);
    /*
        指定した都道府県コードに対応する都道府県データを取得できた場合 200
        指定した都道府県コードに対応する都道府県データが存在しなかった場合 404
    */
    }

    @Test
    @DisplayName("都道府県名に該当する都道府県データを取得した場合、レスポンスコード200および都道府県のjson形式データを返す")
    void getPrefByNameTest1() throws Exception {
        doReturn(new PrefectureEntity("02", "青森県"))
                .when(prefectureService)
                .getPrefByName("青森県");

        String actualResult = mockMvc
                .perform(get("/prefectures/names?prefName=青森県"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        String expectedResult = objectMapper.readTree(getJsonFileData("prefecture2.json")).toString();

        JSONAssert.assertEquals(expectedResult, actualResult, JSONCompareMode.STRICT);
    /*
        指定した都道府県名に対応する都道府県データを取得できた場合 200
        指定した都道府県名に対応する都道府県データが存在しなかった場合 404
    */
    }

    @Test
    @DisplayName("レスポンスコード200および登録されている全ての都道府県のjson形式データを返す")
    void getAllPrefsTest1() throws Exception {
        doReturn(List.of(
                new PrefectureEntity("01", "北海道"),
                new PrefectureEntity("02", "青森県"),
                new PrefectureEntity("03", "岩手県"),
                new PrefectureEntity("04", "宮城県")))
                .when(prefectureService)
                .getAllPrefs();

        String actualResult = mockMvc
                .perform(get("/prefectures"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        String expectedResult = objectMapper.readTree(getJsonFileData("prefecture-all.json")).toString();

        JSONAssert.assertEquals(expectedResult, actualResult, JSONCompareMode.STRICT);
    /*
        ステータスコード 200
    */
    }

    @Test
    @DisplayName("都道府県コードに該当する都道府県データを追加した場合、レスポンスコード201および都道府県のjson形式データを返す")
    void createPrefTest1() throws Exception {
        doReturn(new PrefectureEntity("05", "秋田県"))
                .when(prefectureService)
                .createPref("05", "秋田県");

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

        JSONAssert.assertEquals(expectedResult, actualResult, JSONCompareMode.STRICT);
    /*
        指定した都道府県コードに対応する新規の都道府県データを追加できた場合 201
        指定した都道府県コードに対応する新規の都道府県データを追加できなかった場合 409
    */
    }

    @Test
    @DisplayName("都道府県コードに該当する都道府県データを更新した場合、レスポンスコード204を返す")
    void updatePrefTest1() throws Exception {
        doNothing().when(prefectureService).updatePref("02", "あおもりけん");

        var actualResult = mockMvc
                .perform(patch("/prefectures/02")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new PrefectureRequestForm("02", "あおもりけん")))
                )
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        verify(prefectureService, times(1)).updatePref("02", "あおもりけん");


    /*
        指定した都道府県コードに対応する都道府県データを更新できた場合 204
        指定した都道府県コードに対応する新規の都道府県データを更新できなかった場合 409
        指定した都道府県コードに対応する都道府県データが存在しなかった場合 404
    */
    }

    @Test
    @DisplayName("都道府県コードに該当する都道府県データを削除した場合、レスポンスコード204を返す")
    void deletePrefTest1() throws Exception {
        doNothing().when(prefectureService).deletePref("11");

        var actualResult = mockMvc
                .perform(delete("/prefectures/11"))
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        verify(prefectureService, times(1)).deletePref("11");

    /*
        指定した都道府県コードに対応する都道府県データを削除できた場合 204
        指定した都道府県コードに対応する都道府県データを削除できなかった場合 409
        指定した都道府県コードに対応する都道府県データが存在しなかった場合 404
    */
    }
}
