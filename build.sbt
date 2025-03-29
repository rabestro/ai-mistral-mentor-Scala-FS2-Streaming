ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.16"

lazy val root = (project in file("."))
  .settings(
    name := "FS2 Streaming Mastery Journey"
  )

libraryDependencies += "co.fs2" %% "fs2-core" % "3.12.0"
libraryDependencies += "co.fs2" %% "fs2-io" % "3.12.0"