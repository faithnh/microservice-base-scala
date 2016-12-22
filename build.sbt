import sbt.Keys._
import sbt.Tests
import NativePackagerHelper._
import ScoverageSbtPlugin._

version := "1.0"

// 必要に応じて名前を変更しよう
name := "microservice-base-scala"

scalaVersion := "2.11.8"

lazy val configMode = sys.props.getOrElse("CONFIG_MODE", default = "development")

// プロジェクトの設定
lazy val core = (project in file("core"))
  .disablePlugins (plugins.JUnitXmlReportPlugin)
  .settings(commonSettings)

lazy val web = (project in file("web")).dependsOn(core)
  .enablePlugins(PlayScala)
  .disablePlugins (plugins.JUnitXmlReportPlugin)
  .settings(commonSettings)
  .settings(logSettingsForWeb)
  .settings(
    unmanagedResourceDirectories in Compile += baseDirectory.value / "../conf",

    libraryDependencies ++= coreDeps,
    routesGenerator := InjectedRoutesGenerator
  )

lazy val generator = (project in file("generator")).dependsOn(core)
  .settings(commonSettings)

/// 詳細な設定
lazy val commonSettings = Seq(
  scalaVersion := "2.11.8",
  libraryDependencies ++= coreDeps,
  resolvers ++= Seq("snapshots", "releaes").map(Resolver.sonatypeRepo),
  resolvers += Resolver.jcenterRepo,

  // これを入れないと、activatorのjavaプロセスを再利用するため、runした時に、
  // javaOptionsなどが反映されません。
  fork := true,

  // 並列実行すると、DBのテスト時にコネクションが、テストが失敗するため */
  parallelExecution in Test := false,

  // テストを走らせる場合に、application.confを参照できるようにするため
  javaOptions += "-Dconfig.file=" + baseDirectory.value / "../conf/application.conf",

  unmanagedResourceDirectories in Compile += baseDirectory.value / "../conf",

  // sbt-native-pluginで環境変数で定義したコンフィグファイルから設定を読み込めるようにするため
  bashScriptExtraDefines := Seq(
    """
      |CONFIG_MODE="${CONFIG_MODE:-development}"
      |addJava "-Dconfig.file=${app_home}/../conf/$CONFIG_MODE.conf"
    """.stripMargin,
    """addJava "-Dapp.home=${app_home}/../""""
  )
)

// web以外のプロジェクトを作った場合、利用する
lazy val logSettings = Seq(
  javaOptions in Compile += "-Dlogback.configurationFile=" + baseDirectory.value / "../conf/logback-%s-%s.xml".format(name.value, configMode),
  bashScriptExtraDefines += """addJava "-Dlogback.configurationFile=${app_home}/../conf/logback-%s-$CONFIG_MODE.xml"""".format(name.value)
)

lazy val logSettingsForWeb = Seq(
  javaOptions in Compile += "-Dlogger.file=" + baseDirectory.value / "../conf/logback-%s-%s.xml".format(name.value, configMode),
  bashScriptExtraDefines +=  """addJava "-Dlogger.file=${app_home}/../conf/logback-%s-$CONFIG_MODE.xml"""".format(name.value)
)

// 依存するライブラリ系の設定
lazy val slickVersion           = "3.1.1"
lazy val slickJodaMapperVersion = "2.2.0"
lazy val scalikeVersion         = "2.5.0"
lazy val playVersion            = "2.5.8"
lazy val spec2Version           = "3.8.5"
lazy val config_mode            = sys.props.getOrElse("CONFIG_MODE", "development")

lazy val coreDeps = Seq(
  // Config & Log
  Seq(
    "com.typesafe"                   %  "config"          % "1.3.0",
    "com.iheart"                     %% "ficus"           % "1.2.3",
    "org.slf4j"                      %  "slf4j-api"       % "1.7.21",
    "ch.qos.logback"                 %  "logback-classic" % "1.1.7"
  ),

  // Persistence
  Seq(
    "mysql"                          %  "mysql-connector-java"             % "5.1.35",
    "com.rabbitmq"                   %  "amqp-client"                      % "3.6.5",
    "com.zaxxer"                     %  "HikariCP"                         % "2.4.5",
    "com.typesafe.slick"             %% "slick"                            % slickVersion,
    "com.typesafe.slick"             %% "slick-hikaricp"                   % slickVersion,
    "com.typesafe.slick"             %% "slick-codegen"                    % slickVersion,
    "com.github.tototoshi"           %% "slick-joda-mapper"                % slickJodaMapperVersion,
    "org.scalikejdbc"                %% "scalikejdbc"                      % scalikeVersion,
    "org.scalikejdbc"                %% "scalikejdbc-syntax-support-macro" % scalikeVersion,
    "com.chuusai"                    %% "shapeless"                        % "2.3.1",
    "io.underscore"                  %% "slickless"                        % "0.3.0"
  ),

  // Tests
  Seq(
    "org.specs2"                     % "specs2-core_2.11"       % spec2Version,
    "org.specs2"                     % "specs2-mock_2.11"       % spec2Version,
    "org.specs2"                     % "specs2-scalacheck_2.11" % spec2Version,
    "org.scalacheck"                 % "scalacheck_2.11"        % "1.13.2"
  ),

  // Utils
  Seq(
    "joda-time"                      % "joda-time"             % "2.7",
    "org.joda"                       % "joda-convert"          % "1.7",
    "com.typesafe.play"              % "play-json_2.11"        % playVersion,
    "ai.x"                           %% "play-json-extensions" % "0.8.0",
    "com.github.tototoshi"           %% "play-json-naming"     % "1.1.0"
  ),
  // web
  Seq(
    "com.typesafe.play"                %% "play-jdbc"       % playVersion,
    "com.typesafe.play"                %% "play-cache"      % playVersion,
    "com.typesafe.play"                %% "play-ws"         % playVersion,
    "com.typesafe.play"                %% "filters-helpers" % playVersion
  )
).flatten

onLoadMessage <<= (name,crossVersion,scalaVersion,scalaBinaryVersion,onLoadMessage){
  (name,crossV,scalaV,binaryV,currentMessage) =>
    println("\n" + name + "\nscalaVersion = "+ scalaV + ", crossVersion = " + crossV + ", binaryVersion = " + binaryV + "\n")
    currentMessage
}

// scalikejdbcのジェネレータを有効にするため
scalikejdbcSettings
