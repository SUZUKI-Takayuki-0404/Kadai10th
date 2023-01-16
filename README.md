# 第10回課題

## アプリケーション概要

- 以下データテーブルに対するCRUD処理すべてを備えたREST API
- JUnitによるテストコード実装
- GitHub Actionsによる自動単体テスト実装

### データテーブル

#### 空港コード表

[空港コード](sql/table-airport.sql)

<img src=https://user-images.githubusercontent.com/113277395/210732765-8d3d43f6-994f-41b9-b948-8c5e09cfddac.PNG width="30%">

#### 都道府県コード表

[都道府県コード](sql/table-Pref.sql)

<img src="https://user-images.githubusercontent.com/113277395/210732796-9837f61f-de6c-4700-8222-7cc238f8e04f.PNG" width="30%">

## API仕様書

[API仕様書](http://htmlpreview.github.io/?https://github.com/SUZUKI-Takayuki-0404/Kadai10th/blob/main/Kadai10api.html)

- [Apiaryエディタ](https://docs.oracle.com/cd/E91812_01/tools/apiary-editor/index.html)を使用し、API Blueprintベースで作成
- 上記で作成した仕様書を[aglio](https://github.com/danielgtaylor/aglio)にてhtml変換
- GitHub上のhtmlファイル表示には[GitHub & BitBucket HTML Preview](https://htmlpreview.github.io/)を使用

## 起動/API動作確認プロセス

### 起動プロセス

git clone コマンドにて各自PCにダウンロードし実行

### API動作確認プロセス

コマンド一覧表

| 処理 | コマンド | ステータスコード |
|--|--|--|
| GET | curl -X | 200 |
| POST | curl -X | 201 |
| PATCH | curl -X | 200 |
| DELETE | curl -X | 204 |

## テスト対象メソッド一覧

### Mapper

#### 都道府県テーブル

|Type|Name (Arguments)|Function|SQL|Note|
|--|--|--|--|--|
|Optional\<PrefEntity\>|`findByCodeFromPrefs (String prefCode)`|指定した都道府県コードに対応する都道府県データを取得|`SELECT * FROM prefectures WHERE prefCode = #{prefCode}`||
|Optional\<PrefEntity\>|`findByNameFromPrefs (String prefName)`|指定した都道府県名に対応する都道府県データを取得|`SELECT * FROM prefectures WHERE prefName = #{prefName}`||
|List\<PrefEntity\>|`findAllFromPrefs ()`|全ての都道府県データを取得|`SELECT * FROM prefectures`|
|void|`createOfPref (String prefCode, String prefName)`|既存と重複しない都道府県データを挿入|`INSERT INTO prefectures (prefCode, prefName) VALUES (#{prefCode}, #{prefName})`||
|boolean|`updateOfPref (String prefCode, String prefName)`|指定した都道府県コードに対応する都道府県データを更新|`UPDATE prefectures SET prefName = #{prefName} WHERE prefCode = #{prefCode}`||
|boolean|`deleteOfPref (String prefCode)`|指定した都道府県コードに対応する都道府県データを削除|`DELETE FROM prefectures WHERE prefCode = #{prefCode}`||

#### 空港テーブル

|Type|Name (Arguments)|Function|SQL|Note|
|--|--|--|--|--|
|Optional\<AirportEntity\>|`findByCodeFromAirports (String airportCode)`|空港データと都道府県データとを都道府県コードで結合し、指定した空港コードに該当するデータを取得|`SELECT airports.*, prefectures.prefName FROM airports INNER JOIN prefectures ON airports.prefCode = prefectures.prefCode WHERE airportCode = #{airportCode} `||
|List\<AirportEntity\>|`findByPrefFromAirports (String airportCode, String airportName, String prefCode)`|空港データと都道府県データとを都道府県コードで結合し、指定した都道府県名に該当するデータを取得|`SELECT airports.*, prefectures.prefName FROM airports INNER JOIN prefectures ON airports.prefCode = prefectures.prefCode WHERE prefCode = #{prefCode}`||
|List\<AirportEntity\>|`findAllFromAirports (airportCode)`|空港データと都道府県データとを都道府県コードで結合し、全データを取得|`SELECT airports.*, prefectures.prefName FROM airports INNER JOIN prefectures ON airports.prefCode = prefectures.prefCode`||
|void|`createOfAirport (String airportCode, String airportName, String prefCode)`|既存と重複しない空港データを挿入|`INSERT INTO airports (airportCode, airportName, prefCode) VALUES (#{airportCode}, #{airportName}, #{prefCode})`||
|boolean|`updateOfAirport (String airportCode, String airportName, String prefCode)`|指定した空港コードに対応する空港データを更新|`UPDATE airports SET airportName = #{airportName}, prefCode = #{prefCode} WHERE airportCode = #{airportCode}`||
|boolean|`deleteOfAirport (String airportCode)`|指定した空港コードに対応する空港データを削除|`DELETE FROM airports WHERE airportCode = #{airportCode}`||


### Service

|Type|Name (Arguments)|Function|Note|
|--|--|--|--|
|PrefEntity|`getPrefByCode (String prefCode)`|都道府県コードに対応する都道府県エンティティを返す||
|PrefEntity|`getPrefByName (String prefName)`|都道府県名に対応する都道府県エンティティを返す||
|List\<PrefEntity\>|`getAllPrefs ()`|都道府県エンティティ全てをリストとして返す||
|PrefEntity|`createPref (String prefCode, String prefName)`|既存と重複しない都道府県エンティティを追加登録する||
|PrefEntity|`updatePref (String prefCode, String prefName)`|指定した都道府県コードに対応する都道府県エンティティのうち都道府県コード以外を書き換える||
|void|`deletePref (String prefCode)`|指定した都道府県コードに対応する都道府県エンティティを削除する||
|AirportEntity|`getAirport (String airportCode)`|空港コードに対応する空港エンティティを返す||
|List\<AirportEntity\>|`getAirportsByPref (String prefCode)`|都道府県コード/都道府県名に対応する空港エンティティ全てをリストとして返す||
|List\<AirportEntity\>|`getAllAirports ()`|空港エンティティ全てをリストとして返す||
|AirportEntity|`createAirport (String airportCode, String airportName, String prefCode)`|既存と重複しない空港エンティティを追加登録する||
|AirportEntity|`updateAirport (String airportCode, String airportName, String prefCode)`|指定した空港コードに対応する空港エンティティのうち空港コード以外を書き換える||
|void|`deleteAirport (String airportCode)`|指定した空港コードに対応する空港エンティティを削除する||

### Controller

#### Prefectures リソース

|Request|Name|Function|Note|
|--|--|--|--|
|GET|`getPrefByCode`|指定した既存の都道府県コードに対応する都道府県データを返す||
|GET|`getPrefByName`|指定した既存の都道府県名に対応する都道府県データを返す||
|GET|`getAllPrefs`|既存の都道府県コードと対応する都道府県データを全て返す||
|POST|`createPref`|既存とは重複しない都道府県コードとその都道府県名をデータとして追加||
|PATCH|`updatePref`|指定した既存の都道府県コードに対応する都道府県名を書き換え||
|DELETE|`deletePref`|指定した既存の都道府県コードに対応する都道府県データ削除|要：削除対象の都道府県コードを付与されている空港も削除|

#### Airports リソース

|Request|Name|Function|Note|
|--|--|--|--|
|GET|`getAirport`|指定した既存の空港コードに対応する空港名、都道府県コード、都道府県名を返す|要：都道府県コードから都道府県名を取得|
|GET|`getAirportsInPref`|指定した既存の都道府県に存在する空港コード、空港名を全て返す|要：都道府県コードから都道府県名を取得|
|GET|`getAllAirports`|全ての既存の空港コードとこれに対応する空港名、都道府県コード、都道府県名を返す|要：都道府県コードから都道府県名に変換|
|POST|`createAirport`|既存とは重複しない任意の空港コードで空港を新規追加|要：既存の都道府県コード/都道府県名から都道府県コードに変換|
|PATCH|`updateAirport`|指定した既存の空港コードに対応する空港名、都道府県コードを書き換え|
|DELETE|`deleteAirport`|指定した既存の空港コードに対応する空港名と所在都道府県を削除|

