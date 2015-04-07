scalaVersion := "2.11.6"

name := "TypeScript importer for Scala.js"

version := "0.1-SNAPSHOT"

mainClass := Some("org.scalajs.tools.tsimporter.Main")

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.1",
  "com.github.scopt" %% "scopt" % "3.3.0"
)

organization := "org.scalajs.tools"

scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-feature",
  "-encoding", "utf8"
)

Revolver.settings

val filesToParse = Seq("d3", "crossfilter").map(p => s"../DefinitelyTyped/$p/$p.d.ts") ++ Seq("../DefinitelyTyped/dcjs/dc.d.ts")

Revolver.reStartArgs := Seq("--output", "packages", "--package", "pl.relationsystems.js") ++ filesToParse