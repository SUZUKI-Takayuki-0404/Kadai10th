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

[都道府県コード](sql/table-prefecture.sql)

<img src="https://user-images.githubusercontent.com/113277395/210732796-9837f61f-de6c-4700-8222-7cc238f8e04f.PNG" width="30%">

## API仕様書

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

## テスト一覧
