# MicroService Base by scala

scalaの基盤構築に利用するためのベースプロジェクトです。

## インストール
本プロジェクトに必要なものは以下の通り。
- git 2.0.0+
- Java(OpenJDK) 1.8.0+
- Scala 2.11.8+
- Docker 1.10.0+ （1.12.x を推奨）
- Docker-Compose 1.6.0+ (1.8.x を推奨)

開発用のエディタは基本自由ですが、特にこだわりがなければIntelliJ IDEAを推奨します。
https://www.jetbrains.com/idea/download/  

プロジェクトをクローンする

```
$ git clone https://github.com/faithnh/microservice-base-scala
```

自動フォーマットを適応する  

```
$ cd microservice-base-scala
$ cp scalafmt/pre-commit .git/hooks/
```

サーバーを起動してみる

```
# 初期またはコンテナの中身を変更した場合は、以下を実行すること
$ docker-compose build --no-cache

$ docker-compose up -d

$ docker ps --format "{{.Names}}"
sample_db_1 0.0.0.0:3306->3306/tcp

# webプロジェクトをコンパイルする
$ ./sbt.sh web/compile
...
[success] Total time: xx s, completed

# webプロジェクトを走らせる
$ ./sbt.sh web/run
...
// 終了はCtrl + C
```

## プロジェクト構成

```
├── build.sbt scalaのビルド設定
├── conf
│   ├── application.conf 全体の設定ファイル
│   ├── development.conf 環境ごとの設定ファイル（デフォルトはこちらを使います）
│   ├── logback-web-環境名.xml logbackの設定ファイル（webのプロジェクト用）
│   ├── production.conf 本番用の設定ファイル
├── core ビジネスロジックはここにまとめます
│   └── src
│       └── main
│           └── scala
│               ├── common ドメイン共通で利用されるだろうロジックを書きます（ビジネスロジックはここに置いてはいけません）
│               │   ├── scalikejdbc scalikeによるDBコンテナ
│               │   │   └── DataBaseContainer.scala
│               │   └── slick slickによるDBコンテナ
│               │       ├── Database.scala
│               │       └── DatabaseContainer.scala
│               └── com パッケージ構成は原則「ドメインの逆順.コンテキスト名」にしています
│                   └── example
│                       └── eventapi コンテキスト単位でapplication・domain・infrastructureパッケージを配置し、管理
│                           ├── application アプリケーション層（application serviceに関係するクラスが入ります）
│                           ├── domain ドメイン層（domain serviceやrepository・daoなどのクラスが入ります。ライブラリに依存する実装はここで行いません）
│                           └── infrastructure インフラ層（MySQLなどのライブラリに依存する実装はここで行います）
├── docker-compose.yml docker-composeの設定ファイル（一括でDockerコンテナを立ち上げるための設定します）
├── generator DBスキーマからslickのコードを作成するためのジェネレータ
│   └── src
│       └── main
│           └── scala
│               └── CodeGenMySQL.scala
├── mysql mysqlのDockerコンテナの定義
│   └── eventapi
│       ├── Dockerfile
│       ├── initialize.sh DBの初期化設定をするためのスクリプト
│       ├── my.cnf DBの設定はここで定義しています（Dockerコンテナ上で使われます）
│       └── tables DBに定義するテーブルやデータはここに格納します
│           ├── tables.sql 追加したいテーブルはここに定義します
│           └── data.sql 追加したいデータはここに定義します
├── project
│   ├── build.properties
│   ├── plugins.sbt sbt-pluginの設定ファイル
│   ├── project
│   └── scalikejdbc.properties scalikejdbcのジェネレータ用途の設定ファイル
├── sbt-launch-0.13.12.jar
├── sbt.bat sbtのスクリプト（windows向け）
├── sbt.sh sbtのスクリプト(linux/mac向け)
├── scalafmt scalaのフォーマットを行うためのスクリプト
│   ├── pre-commit
│   └── scalafmt.jar
└── web play frameworkによるwebアプリはここで構築します
    ├── app
    │   └── com
    │       └── example
    │           └── eventapi
    │               └── application
    │                   └── controller play frameworkのコントローラはここで定義します
    └── conf
        └── routes ルートの設定ファイル
```
