lazy val commonSettings = Seq(
  name := "http4s-test-doobie",
  organization    := "org.fostash",
  version := "1.0-SNAPSHOT",
  scalaVersion := "2.12.8",
  scalacOptions ++= Seq(
    "-feature",
    "-deprecation",
    "-Xfatal-warnings",
    "-Ywarn-value-discard",
    "-Xlint:missing-interpolator",
    "-language:higherKinds",             // Allow higher-kinded types
    "-Ypartial-unification"
  ),
)

lazy val Http4sVersion = "0.20.1"
lazy val DoobieVersion = "0.7.0"
lazy val H2Version = "1.4.197"
lazy val FlywayVersion = "5.2.4"
lazy val CirceVersion = "0.11.1"
lazy val PureConfigVersion = "0.10.2"
lazy val LogbackVersion = "1.2.3"
lazy val ScalaTestVersion = "3.0.5"
lazy val ScalaMockVersion = "4.1.0"
lazy val Log4catsVersion      = "0.2.0"
lazy val Specs2Version = "4.1.0"

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    commonSettings,
    Defaults.itSettings,
    libraryDependencies ++= Seq(
      "org.http4s"            %% "http4s-blaze-server"  % Http4sVersion,
      "org.http4s"            %% "http4s-circe"         % Http4sVersion,
      "org.http4s"            %% "http4s-dsl"           % Http4sVersion,
      "org.http4s"            %% "http4s-blaze-client"  % Http4sVersion,

      "org.tpolecat"          %% "doobie-core"          % DoobieVersion,
      "org.tpolecat"          %% "doobie-h2"            % DoobieVersion,
      "org.tpolecat"          %% "doobie-hikari"        % DoobieVersion,
      "org.tpolecat"          %% "doobie-specs2"        % DoobieVersion % "it, test",
      "org.tpolecat"          %% "doobie-scalatest"     % DoobieVersion % "it, test",

      "mysql"                 % "mysql-connector-java"  % "5.1.47",
      "com.h2database"        %  "h2"                   % H2Version,

      "org.flywaydb"          %  "flyway-core"          % FlywayVersion,

      "io.circe"              %% "circe-generic"        % CirceVersion,
      "io.circe"              %% "circe-literal"        % CirceVersion % "it,test",
      "io.chrisdavenport"     %% "log4cats-slf4j"       % Log4catsVersion,
      "com.github.pureconfig" %% "pureconfig"           % PureConfigVersion,

      "ch.qos.logback"        %  "logback-classic"      % LogbackVersion,

      "org.scalatest"         %% "scalatest"            % ScalaTestVersion % "it,test",
      "org.scalamock"         %% "scalamock"            % ScalaMockVersion % "test",
      "org.specs2"            %% "specs2-core"          % Specs2Version % "test"
    )
  )