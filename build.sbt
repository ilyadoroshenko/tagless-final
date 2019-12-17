name := "reqres"
version := "0.1"
scalaVersion := "2.13.1"

val http4sVersion = "0.21.0-SNAPSHOT"
val doobieVersion = "0.8.7"
val logbackVersion = "1.2.3"
val scalatestVersion = "3.1.0"
val scalatestMockitoVersion = "1.0.0-M2"
val mockitoVersion = "3.2.4"

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,

  "io.circe" %% "circe-generic" % "0.12.3",
  "io.circe" %% "circe-generic-extras" % "0.12.2",
  "io.circe" %% "circe-config" % "0.7.0",

  "org.tpolecat" %% "doobie-hikari" % doobieVersion,
  "org.tpolecat" %% "doobie-core"      % doobieVersion,
  "org.tpolecat" %% "doobie-postgres"  % doobieVersion,

  "ch.qos.logback" % "logback-classic" % logbackVersion,

  "org.scalatest" %% "scalatest" % scalatestVersion % Test,
  "org.scalatestplus" %% "scalatestplus-mockito" % scalatestMockitoVersion % Test,
  "org.mockito" % "mockito-core" % mockitoVersion % Test

)

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)

mainClass in reStart := Some("doroshenko.vidiq.Main")

scalacOptions ++= Seq("-deprecation", "-Ymacro-annotations")
