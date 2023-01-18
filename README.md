# 第10回課題

## アプリケーション概要

- 以下データテーブルに対するCRUD処理すべてを備えたREST API
- JUnitによるテストコード実装
- GitHub Actionsによる自動単体テスト実装

### データテーブル

#### 空港コード表

[空港コード](https://github.com/SUZUKI-Takayuki-0404/Kadai10th/blob/main/sql/table-airport.sql)

<img src=https://user-images.githubusercontent.com/113277395/213170879-3c85d6f2-6976-4c95-8854-6d79c9d3faf7.PNG width="30%">

#### 都道府県コード表

[都道府県コード](https://github.com/SUZUKI-Takayuki-0404/Kadai10th/blob/main/sql/table-prefecture.sql)

<img src="https://user-images.githubusercontent.com/113277395/213171032-961dad83-fe6e-4194-b49f-9e14ac52e30c.PNG" width="20%">

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

#### Prefectures

|Type|Method Name (Arguments)|Function|SQL|Test|Note|
|--|--|--|--|--|--|
|Optional\<PrefEntity\>|`findByCodeFromPrefs (String prefCode)`|指定した都道府県コードに対応する都道府県データを取得|`SELECT * FROM prefectures WHERE prefCode = #{prefCode}`||
|Optional\<PrefEntity\>|`findByNameFromPrefs (String prefName)`|指定した都道府県名に対応する都道府県データを取得|`SELECT * FROM prefectures WHERE prefName = #{prefName}`||
|List\<PrefEntity\>|`findAllFromPrefs ()`|全ての都道府県データを取得|`SELECT * FROM prefectures`|
|void|`createOfPref (String prefCode, String prefName)`|既存と重複しない都道府県データを挿入|`INSERT INTO prefectures (prefCode, prefName) VALUES (#{prefCode}, #{prefName})`||
|boolean|`updateOfPref (String prefCode, String prefName)`|指定した都道府県コードに対応する都道府県データを更新|`UPDATE prefectures SET prefName = #{prefName} WHERE prefCode = #{prefCode}`||
|boolean|`deleteOfPref (String prefCode)`|指定した都道府県コードに対応する都道府県データを削除|`DELETE FROM prefectures WHERE prefCode = #{prefCode}`||

#### Airports

|Type|Method Name (Arguments)|Function|SQL|Test|Note|
|--|--|--|--|--|--|
|Optional\<AirportEntity\>|`findByCodeFromAirports (String airportCode)`|空港データと都道府県データとを都道府県コードで結合し、指定した空港コードに該当するデータを取得|`SELECT airports.*, prefectures.prefName FROM airports INNER JOIN prefectures ON airports.prefCode = prefectures.prefCode WHERE airportCode = #{airportCode} `||
|List\<AirportEntity\>|`findByPrefFromAirports (String airportCode, String airportName, String prefCode)`|空港データと都道府県データとを都道府県コードで結合し、指定した都道府県名に該当するデータを取得|`SELECT airports.*, prefectures.prefName FROM airports INNER JOIN prefectures ON airports.prefCode = prefectures.prefCode WHERE prefCode = #{prefCode}`||
|List\<AirportEntity\>|`findAllFromAirports (airportCode)`|空港データと都道府県データとを都道府県コードで結合し、全データを取得|`SELECT airports.*, prefectures.prefName FROM airports INNER JOIN prefectures ON airports.prefCode = prefectures.prefCode`||
|void|`createOfAirport (String airportCode, String airportName, String prefCode)`|既存と重複しない空港データを挿入|`INSERT INTO airports (airportCode, airportName, prefCode) VALUES (#{airportCode}, #{airportName}, #{prefCode})`||
|boolean|`updateOfAirport (String airportCode, String airportName, String prefCode)`|指定した空港コードに対応する空港データを更新|`UPDATE airports SET airportName = #{airportName}, prefCode = #{prefCode} WHERE airportCode = #{airportCode}`||
|boolean|`deleteOfAirport (String airportCode)`|指定した空港コードに対応する空港データを削除|`DELETE FROM airports WHERE airportCode = #{airportCode}`||


### Service

|Type|Method Name (Arguments)|Function|Test Methods|
|--|--|--|--|
|PrefEntity|`getPrefByCode (String prefCode)`|指定の都道府県コードに対応する都道府県データを返す|<ul><li>指定の都道府県コードに対応する都道府県がある場合はそれを返す</li><li>指定の都道府県コードに対応する都道府県が存在しない場合はそれを表す例外を返す</li></ul>|
|PrefEntity|`getPrefByName (String prefName)`|指定の都道府県名に対応する都道府県データを返す|<ul><li>指定の都道府県名に対応する都道府県がある場合はそれを返す</li><li>指定の都道府県名に対応する都道府県が無い場合はそれを表す例外を返す</li></ul>|
|List\<PrefEntity\>|`getAllPrefs ()`|都道府県データ全てをListとして返す|<ul><li>都道府県が存在する場合はその全てをListで返す</li><li>都道府県が無い場合は空のListを返す</li></ul>|
|PrefEntity|`createPref (String prefCode, String prefName)`|新規の都道府県コードで都道府県データを登録する|<ul><li>指定の都道府県コードが既存のものと重複しない場合は併せて指定した都道府県名で新規の都道府県を追加する</li><li>指定の都道府県コードが既存のものと重複する場合はそれを表す例外を返す</li></ul>|
|PrefEntity|`updatePref (String prefCode, String prefName)`|指定の都道府県コードに対応する都道府県データを更新する|<ul><li>指定の都道府県コードに対応する都道府県のEntityがある場合は都道府県名を併せて指定したものに書き換える</li><li>指定の都道府県コードに対応する都道府県のEntityはあるが、併せて指定した都道府県名が従前と同等の場合はそれを表す例外を返す</li><li>指定の都道府県コードに対応する都道府県のEntityが無い場合はそれを表す例外を返す</li></ul>|
|void|`deletePref (String prefCode)`|指定の都道府県コードに対応する都道府県データを削除する|<ul><li>指定の都道府県コードに対応する都道府県のEntityがある場合は削除する</li><li>指定の都道府県コードに対応する都道府県のEntityが無い場合はそれを表す例外を返す</li></ul>|
|AirportEntity|`getAirport (String airportCode)`|指定の空港コードに対応する空港データを返す|<ul><li>指定の空港コードに対応する空港のEntityがある場合はそれを返す</li><li>指定の空港コードに対応する空港のEntityが存在しない場合はそれを表す例外を返す</li></ul>|
|List\<AirportEntity\>|`getAirportsByPref (String prefCode)`|指定の都道府県名に対応する空港データ全てをListとして返す|<ul><li>指定の都道府県コードに対応する都道府県のEntityがある場合はその全てをListで返す</li><li>指定の都道府県コードに対応する都道府県のEntityが無い場合は空のListを返す</li></ul>|
|List\<AirportEntity\>|`getAllAirports ()`|空港データ全てをListとして返す|<ul><li>空港のEntityが存在する場合はその全てをListで返す</li><li>空港のEntityが無い場合は空のListを返す</li></ul>|
|AirportEntity|`createAirport (String airportCode, String airportName, String prefCode)`|新規の空港データを登録する|<ul><li>指定の空港コードが既存のものと重複しない場合は併せて指定した空港名と都道府県で新規の空港のEntityを追加する</li><li>指定の都道府県コードが既存のものと重複する場合はそれを表す例外を返す</li></ul>|
|AirportEntity|`updateAirport (String airportCode, String airportName, String prefCode)`|指定の空港コードに対応する空港データを更新する|<ul><li>・指定の空港コードに対応する空港のEntityがある場合は空港名と都道府県コードを併せて指定したものに書き換える</li><li>指定の空港コードに対応する空港のEntityはあるが、併せて指定した空港名が従前と同等の場合はそれを表す例外を返す</li><li>指定の空港コードに対応する空港のEntityが無い場合はそれを表す例外を返す</li></ul>|
|void|`deleteAirport (String airportCode)`|指定の空港コードに対応する空港データを削除する|<ul><li>指定の都道府県コードに対応する都道府県のEntityがある場合は削除する</li><li>指定の空港コードに対応する空港のEntityが無い場合はそれを表す例外を返す</li></ul>|

### Controller

#### Prefectures

|Request|Method Name (Arguments)|Function|Note|
|--|--|--|--|
|GET|`getPrefByCode (String prefCode)`|指定した既存の都道府県コードに対応する都道府県データを返す||
|GET|`getPrefByName (String prefName)`|指定した既存の都道府県名に対応する都道府県データを返す||
|GET|`getAllPrefs ()`|既存の都道府県コードと対応する都道府県データを全て返す||
|POST|`createPref (PrefForm prefForm)`|既存とは重複しない都道府県コードとその都道府県名をデータとして追加|`PrefForm (String prefCode, String prefName)`|
|PATCH|`updatePref (PrefForm prefForm)`|指定した既存の都道府県コードに対応する都道府県名を書き換え|`PrefForm (String prefCode, String prefName)`|
|DELETE|`deletePref (String prefCode)`|指定した既存の都道府県コードに対応する都道府県データ削除|要：削除対象の都道府県コードを付与されている空港も削除|

#### Airports

|Request|Method Name (Arguments)|Function|Note|
|--|--|--|--|
|GET|`getAirport (String airportCode)`|指定した既存の空港コードに対応する空港データを返す|要：都道府県コードから都道府県名を取得|
|GET|`getAirportsInPref (String prefCode)`|指定した既存の都道府県に存在する空港データを全て返す|要：都道府県コードから都道府県名を取得|
|GET|`getAllAirports ()`|全ての既存の空港データを返す|要：都道府県コードから都道府県名に変換|
|POST|`createAirport (AirportfForm airportForm)`|既存とは重複しない任意の空港コードで空港データを新規追加|・要：既存の都道府県コード/都道府県名から都道府県コードに変換 <br>・`AirportfForm (String airportCode, String airportName, String prefCode)`|
|PATCH|`updateAirport`|指定した既存の空港コードに対応する空港名、都道府県コードを書き換え|`AirportfForm (String airportCode, String airportName, String prefCode)`|
|DELETE|`deleteAirport (String airportCode)`|指定した既存の空港コードに対応する空港データを削除||

