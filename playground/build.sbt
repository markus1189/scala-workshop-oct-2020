ThisBuild / scalaVersion     := "2.13.3"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "de.codecentric"
ThisBuild / organizationName := "codecentric"

lazy val versions = new {
  lazy val akkaVersion = "2.6.10"
}

lazy val root = (project in file("."))
  .settings(
    name := "playground",
    libraryDependencies ++= List(
      "com.typesafe.akka" %% "akka-actor" % versions.akkaVersion,
      "com.typesafe.akka" %% "akka-actor-typed" % versions.akkaVersion,
      "com.typesafe.akka" %% "akka-stream" % versions.akkaVersion,
      "com.typesafe.akka" %% "akka-testkit" % versions.akkaVersion % Test,
      "org.scalatest" %% "scalatest" % "3.2.2" % Test
    )
  )
