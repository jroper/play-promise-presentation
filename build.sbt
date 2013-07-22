// Project info

name := "play-promise-presentation"

organization := "au.id.jazzy"

version := "2.0.0-SNAPSHOT"

scalaVersion := "2.10.2"

// Dependencies

libraryDependencies ++= Seq(
    "com.typesafe.play" %% "play-java" % "2.2-SNAPSHOT"
)

// Test dependencies

libraryDependencies ++= Seq(
    "com.typesafe.play" %% "play-test" % "2.2-SNAPSHOT" % "test"
)

