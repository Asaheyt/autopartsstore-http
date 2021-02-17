organization := "kz.asaheyt.inc"

name := "autopartsstore-http"

version := "0.1"

scalaVersion := "2.13.4"

//idePackagePrefix := Some("kz.asaheyt.inc.autopartsstore.http")

val AkkaVersion = "2.6.10"
val AkkaPersistenceCassandraVersion = "1.0.3"
val AkkaHttpVersion = "10.2.2"
val AkkaProjectionVersion = "1.0.0"
val circeVersion = "0.13.0"
val swaggerVersion = "2.1.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % AkkaVersion % "test",
  "com.typesafe.akka" %% "akka-slf4j" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % AkkaHttpVersion,

  "de.heikoseeberger" %% "akka-http-circe" % "1.29.1",
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,

  "com.zaxxer" % "HikariCP" % "3.4.1",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",

  "org.postgresql" % "postgresql" % "42.2.18",

  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "org.scalatest" %% "scalatest" % "3.2.2" % Test,
  "joda-time" % "joda-time" % "2.10.5")
