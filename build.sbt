
lazy val akka = (project in file("akka_streams")).settings(
  inThisBuild(List(
    organization := "com.zendesk",
    scalaVersion := "2.11.11"
  )),
  name := "rules-engine",
  version := "1.0.0",
  mainClass in Compile := Some("com.zendesk.RulesEngine"),
  libraryDependencies ++=
    Dependencies.akka ++
    Dependencies.json ++
    Dependencies.spec2
)

lazy val flink = (project in file("flink")).settings(
  inThisBuild(List(
    organization := "com.zendesk",
    scalaVersion := "2.11.11"
  )),
  name := "rules-engine",
  version := "1.0.0",
  mainClass in Compile := Some("com.zendesk.FlinkRulesEngine"),
  libraryDependencies ++=
    Dependencies.flink ++
    Dependencies.json ++
    Dependencies.spec2,
  scalacOptions in Test ++= Seq("-Yrangepos")
)

lazy val spark = (project in file("spark")).settings(
  inThisBuild(List(
    organization := "com.zendesk",
    scalaVersion := "2.11.11"
  )),
  fork := true,
  name := "rules-engine",
  version := "1.0.0",
  mainClass in Compile := Some("com.zendesk.SparkRulesEngine"),
  libraryDependencies ++= Dependencies.spark
)
