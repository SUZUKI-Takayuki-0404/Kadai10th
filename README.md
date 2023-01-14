# 第10回課題（本課題の成果物は最終課題に使用）

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

- findByCodeFromPrefs|指定した都道府県コードに対応する都道府県データを取得
- findByNameFromPrefs|指定した都道府県名に対応する都道府県データを取得
- findAllFromPrefs|全ての都道府県データを取得
- createOfPref|既存と重複しない都道府県データを挿入
- updateOfPref|指定した都道府県コードに対応する都道府県データを更新
- deleteOfPref|指定した都道府県コードに対応する都道府県データを削除

#### 空港テーブル insert/update/delete

- createOfAirport|既存と重複しない空港データを挿入
- updateOfAirport|指定した空港コードに対応する空港データを更新
- deleteOfAirport|指定した空港コードに対応する空港データを削除

#### 空港コード表 and 都道府県コード表 select inner join

- findByCodeFromAirports|空港データと都道府県データとを都道府県コードで結合し、指定した空港コードに該当するデータを取得
- findAllByNameFromAirports|空港データと都道府県データとを都道府県コードで結合し、指定した都道府県名に該当するデータを取得
- findAllFromAirports|空港データと都道府県データとを都道府県コードで結合し、全データを取得


### Service

|Type|Name(Arguments)|Function|Note|
|--|--|--|--|
|PrefEntity|getPrefByCode|都道府県コードに対応する都道府県エンティティを返す||
|PrefEntity|getPrefByName|都道府県名に対応する都道府県エンティティを返す||
|List<PrefEntity>|getAllPrefs|都道府県エンティティ全てをリストとして返す||
|PrefEntity|createPref|既存と重複しない都道府県エンティティを追加登録する||
|PrefEntity|updatePref|指定した都道府県コードに対応する都道府県エンティティのうち都道府県コード以外を書き換える||
|void|deletePref|指定した都道府県コードに対応する都道府県エンティティを削除する||
|AirportEntity|getAirport|空港コードに対応する空港エンティティを返す||
|List<AirportEntity>|getAirportsByPref|都道府県コード/都道府県名に対応する空港エンティティ全てをリストとして返す||
|List<AirportEntity>|getAllAirports|空港エンティティ全てをリストとして返す||
|AirportEntity|createAirport|既存と重複しない空港エンティティを追加登録する||
|AirportEntity|updateAirport|指定した空港コードに対応する空港エンティティのうち空港コード以外を書き換える||
|void|deleteAirport|指定した空港コードに対応する空港エンティティを削除する||

### Controller

#### Prefectures

|Request|Type|Name|Function|Note|
|--|--|--|--|--|
|GET||getPrefByCode|指定した既存の都道府県コードに対応する都道府県データを返す||
|GET||getPrefByName|指定した既存の都道府県名に対応する都道府県データを返す||
|GET||getAllPrefs|既存の都道府県コードと対応する都道府県データを全て返す||
|POST||createPref|既存とは重複しない都道府県コードとその都道府県名をデータとして追加||
|PATCH||updatePref|指定した既存の都道府県コードに対応する都道府県名を書き換え||
|DELETE||deletePref|指定した既存の都道府県コードに対応する都道府県データ削除|要：削除対象の都道府県コードを付与されている空港も削除|

#### Airports

|Request|Type|Name|Function|Note|
|--|--|--|--|--|
|GET||getAirport|指定した既存の空港コードに対応する空港名、都道府県コード、都道府県名を返す|要：都道府県コードから都道府県名を取得|
|GET||getAirportsInPref|指定した既存の都道府県に存在する空港コード、空港名を全て返す|要：都道府県コードから都道府県名を取得|
|GET||getAllAirports|全ての既存の空港コードとこれに対応する空港名、都道府県コード、都道府県名を返す|要：都道府県コードから都道府県名に変換|
|POST||createAirport|既存とは重複しない任意の空港コードで空港を新規追加|要：既存の都道府県コード/都道府県名から都道府県コードに変換|
|PATCH||updateAirport|指定した既存の空港コードに対応する空港名、都道府県コードを書き換え||
|DELETE||deleteAirport|指定した既存の空港コードに対応する空港名と所在都道府県を削除||

