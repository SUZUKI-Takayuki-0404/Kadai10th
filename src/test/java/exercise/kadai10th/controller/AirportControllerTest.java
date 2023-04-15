package exercise.kadai10th.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AirportControllerTest {

    @Test
    @Disabled
    void getAirportTest1() {

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
