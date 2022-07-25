//Scala
val circeV = "0.14.2"
val enumeratumV = "1.7.0"
val http4sV = "0.23.9"
val http4sClientV = "0.5.0"
val doobieV = "1.0.0-RC1"
val flyWayV = "8.5.0"
val log4catsV = "2.2.0"
val logbackClassicV = "1.2.10"
val pureConfigV = "0.17.1"
//Java
val kafkaV = "3.1.0"
//compiler plugins
val betterMonadicForV = "0.3.1"

ThisBuild / version := "0.1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .settings(
    name := "kafka-streams-course",
    Test / fork := true,
    scalaVersion := "2.13.8",
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % betterMonadicForV),
    libraryDependencies ++= Seq(
      "org.apache.kafka" % "kafka-clients" % kafkaV,
      "org.apache.kafka" % "kafka-streams" % kafkaV,
      "org.apache.kafka" %% "kafka-streams-scala" % kafkaV,
      "io.circe" %% "circe-core" % circeV,
      "io.circe" %% "circe-generic" % circeV,
      "io.circe" %% "circe-parser" % circeV
    )
  )

