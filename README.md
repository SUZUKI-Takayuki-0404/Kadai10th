# 第10回課題　（最終課題は本課題の成果物ベースで作成予定）
## 1. アプリケーション概要

- 以下データテーブルに対するCRUD処理すべてを備えたREST API
- JUnitによるテストコード実装
- GitHub Actionsによる自動単体テスト実装
- プロジェクト構成図
 (ここに付図)


## 2. 取扱データテーブル
#### 都道府県コード表

[都道府県コード](https://github.com/SUZUKI-Takayuki-0404/Kadai10th/blob/main/sql/table-prefecture.sql)

<img src="https://user-images.githubusercontent.com/113277395/213171032-961dad83-fe6e-4194-b49f-9e14ac52e30c.PNG" width="20%">

#### 空港コード表

[空港コード](https://github.com/SUZUKI-Takayuki-0404/Kadai10th/blob/main/sql/table-airport.sql)

<img src=https://user-images.githubusercontent.com/113277395/213170879-3c85d6f2-6976-4c95-8854-6d79c9d3faf7.PNG width="20%">


## 3. API動作確認プロセス
### 事前準備

git clone コマンドにて各自PCにダウンロードし実行
`git clone git@github.com:SUZUKI-Takayuki-0404/Kadai10th.git`


### API仕様書

[API仕様書リンク](http://htmlpreview.github.io/?https://github.com/SUZUKI-Takayuki-0404/Kadai10th/blob/main/Kadai10api.html)

- [Apiaryエディタ](https://docs.oracle.com/cd/E91812_01/tools/apiary-editor/index.html)を使用し、API Blueprintにて作成
- 上記で作成した仕様書を[aglio](https://github.com/danielgtaylor/aglio)にてhtml変換
- GitHub上のhtmlファイル表示には[GitHub & BitBucket HTML Preview](https://htmlpreview.github.io/)を使用


### curlコマンド一覧
#### Prefectures

| Request | curlコマンド |
|--|--|
| GET | `curl 'http://localhost:8080/prefectures/?prefCode=01'` |
| GET | `curl 'http://localhost:8080/prefectures/?prefName=北海道'` |
| GET | `curl 'http://localhost:8080/prefectures'` |
| POST | `curl -XPOST -H "Content-type: application/json" -d '{"prefCode": "11","prefName": "さいたま県"}' 'http://localhost:8080/prefectures'` |
| PATCH | `curl -XPATCH -H "Content-type: application/json" -d '{"prefCode": "11","prefName": "埼玉県"}' 'http://localhost:8080/prefectures'` |
| DELETE | `curl -XDELETE 'http://localhost:8080/prefectures/11'` |

#### Airports

| Request | curlコマンド |
|--|--|
| GET | `curl 'http://localhost:8080/airports/?airportCode=HND'` |
| GET | `curl 'http://localhost:8080/airports/?prefCode=27'` |
| GET | `curl 'http://localhost:8080/airports'` |
| POST | `curl -XPOST -H "Content-type: application/json" -d '{ "airportCode": "MYE", "airportName": "三宅島", "prefCode": "13" }' 'http://localhost:8080/airports'` |
| PATCH | `curl -XPATCH -H "Content-type: application/json" -d '{"airportCode": "NKM", "airportName": "名古屋", "prefCode": "23"}' 'http://localhost:8080/airports/NKM'` |
| DELETE | `curl -XDELETE 'http://localhost:8080/airports/NKM'` |


## 4. 各クラスの実装メソッド/Testメソッド確認事項一覧
### Mapperクラス
#### Prefectures

|Method<br>`type name(arguments)`|Function<br>`実行SQLコマンド`|Testメソッド<br>確認事項|
|--|--|--|
|`Optional<PrefEntity>`<br>`findByCodeFromPrefs(String prefCode)`|指定した都道府県コードに対応する都道府県データを取得<br>`SELECT * FROM prefectures`<br>`WHERE prefCode = #{prefCode}`||
|`Optional<PrefEntity>`<br>`findByNameFromPrefs(String prefName)`|指定した都道府県名に対応する都道府県データを取得<br>`SELECT * FROM prefectures`<br>`WHERE prefName = #{prefName}`||
|`List<PrefEntity> findAllFromPrefs()`|全ての都道府県データを取得<br>`SELECT * FROM prefectures`||
|`void createOfPref`<br>`(String prefCode, String prefName)`|既存と重複しない都道府県データを挿入<br>`INSERT INTO prefectures (prefCode, prefName)`<br>`VALUES (#{prefCode}, #{prefName})`||
|`boolean updateOfPref`<br>`(String prefCode, String prefName)`|指定した都道府県コードに対応する都道府県データを更新<br>`UPDATE prefectures `<br>`SET prefName = #{prefName}`<br>`WHERE prefCode = #{prefCode}`||
|`boolean deleteOfPref`<br>`(String prefCode)`|指定した都道府県コードに対応する都道府県データを削除<br>`DELETE FROM prefectures`<br>`WHERE prefCode = #{prefCode}`||


#### Airports

|Method<br>`type name(arguments)`|Function<br>`事項SQLコマンド`|Testメソッド<br>確認事項|
|--|--|--|
|`Optional<AirportEntity>`<br>`findByCodeFromAirports(String airportCode)`|空港データと都道府県データとを都道府県コードで結合し、指定した空港コードに該当するデータを取得<br>`SELECT airports.*, prefectures.prefName`<br>`FROM airports INNER JOIN prefectures`<br>`ON airports.prefCode = prefectures.prefCode`<br>`WHERE airportCode = #{airportCode} `||
|`List<AirportEntity> findByPrefFromAirports`<br>`(String airportCode, String airportName, String prefCode)`|空港データと都道府県データとを都道府県コードで結合し、指定した都道府県名に該当するデータを取得<br>`SELECT airports.*, prefectures.prefName`<br>`FROM airports INNER JOIN prefectures`<br>`ON airports.prefCode = prefectures.prefCode `<br>`WHERE prefCode = #{prefCode}`||
|`List<AirportEntity> findAllFromAirports(airportCode)`|空港データと都道府県データとを都道府県コードで結合し、全データを取得<br>`SELECT airports.*, prefectures.prefName `<br>`FROM airports INNER JOIN prefectures`<br>`ON airports.prefCode = prefectures.prefCode`||
|`void createOfAirport`<br>`(String airportCode, String airportName, String prefCode)`|既存と重複しない空港データを挿入<br>`INSERT INTO airports (airportCode, airportName, prefCode)`<br>`VALUES (#{airportCode}, #{airportName}, #{prefCode})`||
|`boolean updateOfAirport`<br>`(String airportCode, String airportName, String prefCode)`|指定した空港コードに対応する空港データを更新<br>`UPDATE airports `<br>`SET airportName = #{airportName}, prefCode = #{prefCode} `<br>`WHERE airportCode = #{airportCode}`||
|`boolean deleteOfAirport`<br>`(String airportCode)`|指定した空港コードに対応する空港データを削除<br>`DELETE FROM airports `<br>`WHERE airportCode = #{airportCode}`||


### Serviceクラス
#### Prefectures

|Method<br>`type name(arguments)`|Function|Testメソッド<br>確認事項|
|--|--|--|
|`PrefEntity getPrefByCode(String prefCode)`|指定の都道府県コードに対応する都道府県データを返す|<ul><li>指定の都道府県コードに対応する都道府県がある場合はそれを返す</li><li>指定の都道府県コードに対応する都道府県が存在しない場合はNoResourceExceptionをスローする</li></ul>|
|`PrefEntity getPrefByName(String prefName)`|指定の都道府県名に対応する都道府県データを返す|<ul><li>指定の都道府県名に対応する都道府県がある場合はそれを返す</li><li>指定の都道府県名に対応する都道府県が無い場合はNoResourceExceptionをスローする</li></ul>|
|`List<PrefEntity> getAllPrefs()`|都道府県データ全てをListとして返す|<ul><li>都道府県が存在する場合はその全てをListで返す</li><li>都道府県が無い場合は空のListを返す</li></ul>|
|`PrefEntity createPref`<br>`(String prefCode, String prefName)`|新規の都道府県コードで都道府県データを登録する|<ul><li>指定の都道府県コードが既存のものと重複しない場合は併せて指定した都道府県名で新規の都道府県を追加する</li><li>指定の都道府県コードが既存のものと重複する場合はCodeDuplicatedExceptionをスローする</li></ul>|
|`PrefEntity updatePref`<br>`(String prefCode, String prefName)`|指定の都道府県コードに対応する都道府県データを更新する|<ul><li>指定の都道府県コードに対応する都道府県のEntityがある場合は都道府県名を併せて指定したものに書き換える</li><li>指定の都道府県コードに対応する都道府県のEntityはあるが、併せて指定した都道府県名が従前と同等の場合はSameAsCurrentExceptionをスローする</li><li>指定の都道府県コードに対応する都道府県のEntityが無い場合はNoResourceExceptionをスローする</li></ul>|
|`void deletePref`<br>`(String prefCode)`|指定の都道府県コードに対応する都道府県データを削除する|<ul><li>指定の都道府県コードに対応する都道府県のEntityがある場合は削除する</li><li>指定の都道府県コードに対応する都道府県のEntityが無い場合はNoResourceExceptionをスローする</li></ul>|


#### Airportsクラス

|Method<br>`type name(arguments)`|Function|Testメソッド<br>確認事項|
|--|--|--|
|`AirportEntity`<br>`getAirport(String airportCode)`|指定の空港コードに対応する空港データを返す|<ul><li>指定の空港コードに対応する空港のEntityがある場合はそれを返す</li><li>指定の空港コードに対応する空港のEntityが存在しない場合はNoResourceExceptionをスローする</li></ul>|
|`List<AirportEntity>`<br>`getAirportsByPref(String prefCode)`|指定の都道府県名に対応する空港データ全てをListとして返す|<ul><li>指定の都道府県コードに対応する都道府県のEntityがある場合はその全てをListで返す</li><li>指定の都道府県コードに対応する都道府県のEntityが無い場合は空のListを返す</li></ul>|
|`List<AirportEntity>`<br>`getAllAirports()`|空港データ全てをListとして返す|<ul><li>空港のEntityが存在する場合はその全てをListで返す</li><li>空港のEntityが無い場合は空のListを返す</li></ul>|
|`AirportEntity createAirport`<br>`(String airportCode, String airportName, String prefCode)`|新規の空港データを登録する|<ul><li>指定の空港コードが既存のものと重複しない場合は併せて指定した空港名と都道府県で新規の空港のEntityを追加する</li><li>指定の都道府県コードが既存のものと重複する場合はCodeDuplicatedExceptionをスローする</li></ul>|
|`AirportEntity updateAirport`<br>`(String airportCode, String airportName, String prefCode)`|指定の空港コードに対応する空港データを更新する|<ul><li>指定の空港コードに対応する空港のEntityがある場合は空港名と都道府県コードを併せて指定したものに書き換える</li><li>指定の空港コードに対応する空港のEntityはあるが、併せて指定した空港名が従前と同等の場合はSameAsCurrentExceptionをスローする</li><li>指定の空港コードに対応する空港のEntityが無い場合はNoResourceExceptionをスローする</li></ul>|
|`void deleteAirport`<br>`(String airportCode)`|指定の空港コードに対応する空港データを削除する|<ul><li>指定の都道府県コードに対応する都道府県のEntityがある場合は削除する</li><li>指定の空港コードに対応する空港のEntityが無い場合はNoResourceExceptionをスローする</li></ul>|


### Controller
#### Prefectures

|Method<br>`type name(arguments)`|Function|Testメソッド<br>確認事項|
|--|--|--|
|`ResponseEntity<PrefResponse>`<br>`getPrefByCode(String prefCode)`|指定した既存の都道府県コードに対応する都道府県データを返す||
|`ResponseEntity<PrefResponse>`<br>`getPrefByName(String prefName)`|指定した既存の都道府県名に対応する都道府県データを返す||
|`ResponseEntity<PrefResponse>`<br>`getAllPrefs()`|既存の都道府県コードと対応する都道府県データを全て返す||
|`ResponseEntity<PrefResponse>`<br>`createPref(PrefForm prefForm)`|新規の都道府県コードとその都道府県名をデータとして追加||
|`ResponseEntity<PrefResponse>`<br>`updatePref(PrefForm prefForm)`|指定した既存の都道府県コードに対応する都道府県名を書き換え||
|`ResponseEntity<Void>`<br>`deletePref(String prefCode)`|指定した既存の都道府県コードに対応する都道府県データ削除<br>　要：削除対象の都道府県コードを付与されている空港も削除||


#### Airports

|Method<br>`type name(arguments)`|Function|Testメソッド<br>確認事項|
|--|--|--|
|`ResponseEntity<AirportResponse>`<br>`getAirport(String airportCode)`|指定した既存の空港コードに対応する空港データを返す<br>　要：都道府県コードから都道府県名を取得||
|`ResponseEntity<AirportResponse>`<br>`getAirportsInPref(String prefCode)`|指定した既存の都道府県に存在する空港データを全て返す<br>　要：都道府県コードから都道府県名を取得||
|`ResponseEntity<AirportResponse>`<br>`getAllAirports()`|全ての既存の空港データを返す<br>　要：都道府県コードから都道府県名に変換||
|`ResponseEntity<AirportResponse>`<br>`createAirport(AirportfForm airportForm)`|新規の空港コードで空港データを新規追加||
|`ResponseEntity<AirportResponse>`<br>`updateAirport(AirportfForm airportForm)`|指定の空港コードに対応する空港名、都道府県コードを書き換え||
|`ResponseEntity<Void>`<br>`deleteAirport(String airportCode)`|指定の空港コードに対応する空港データを削除||
