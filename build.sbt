//Scala
val circeV      = "0.14.2"
val pureConfigV = "0.17.1"
val fs2KafkaV   = "3.0.0-M9"
val log4catsV   = "2.3.0"
//Java
val kafkaV          = "3.1.1"
val flywayV         = "8.5.12"
val logbackClassicV = "1.2.11"
//compiler plugins
val betterMonadicForV = "0.3.1"

ThisBuild / version := "0.1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .settings(
    name         := "kafka-streams-course",
    Test / fork  := true,
    scalaVersion := "2.13.8",
    resolvers += "confluent" at "https://packages.confluent.io/maven/",
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % betterMonadicForV),
    libraryDependencies ++= Seq(
      "org.apache.kafka"       % "kafka-clients"       % kafkaV,
      "org.apache.kafka"       % "kafka-streams"       % kafkaV,
      "ch.qos.logback"         % "logback-classic"     % logbackClassicV,
      "org.flywaydb"           % "flyway-core"         % flywayV,
      "org.apache.kafka"      %% "kafka-streams-scala" % kafkaV,
      "com.github.fd4s"       %% "fs2-kafka-vulcan"    % fs2KafkaV,
      "com.github.pureconfig" %% "pureconfig"          % pureConfigV,
      "org.typelevel"         %% "log4cats-slf4j"      % log4catsV,
      "io.circe"              %% "circe-core"          % circeV,
      "io.circe"              %% "circe-generic"       % circeV,
      "io.circe"              %% "circe-parser"        % circeV
    )
  )
