import Dependencies.Ver
import com.typesafe.sbt.packager.docker.{DockerChmodType, DockerPermissionStrategy}

name := "mqttosse"
organization := "com.github.jw3"
scalaVersion := "2.13.1"
git.useGitDescribe := true

scalacOptions ++= Seq(
  "-encoding",
  "UTF-8",
  "-feature",
  "-unchecked",
  "-deprecation",
  "-language:postfixOps",
  "-language:implicitConversions",
  "-Ywarn-unused:imports,privates,locals,implicits",
  "-Xfatal-warnings",
  "-Xlint:_"
)

libraryDependencies ++= Seq(
  "com.iheart" %% "ficus" % Ver.ficus,
  "com.typesafe.akka" %% "akka-stream" % Ver.akka,
  "com.typesafe.akka" %% "akka-http" % Ver.akkaHttp,
  "com.typesafe.akka" %% "akka-http-spray-json" % Ver.akkaHttp,
  "com.lightbend.akka" %% "akka-stream-alpakka-sse" % Ver.alpakka,
  "com.lightbend.akka" %% "akka-stream-alpakka-mqtt" % Ver.alpakka,
  "ch.qos.logback" % "logback-classic" % Ver.logback,
  "com.typesafe.scala-logging" %% "scala-logging" % Ver.scalaLogging,
  "org.scalactic" %% "scalactic" % Ver.scalatest % Test,
  "org.scalatest" %% "scalatest" % Ver.scalatest % Test
)

dockerBaseImage := sys.env.getOrElse("BASE_IMAGE", "openjdk:8-jre-slim")
dockerUpdateLatest := true
dockerEnvVars += "PATH" → "$PATH:/opt/docker/bin"
dockerExposedPorts ++= Seq(9000)
dockerPermissionStrategy := DockerPermissionStrategy.MultiStage
dockerChmodType := DockerChmodType.UserGroupWriteExecute

enablePlugins(JavaServerAppPackaging, DockerPlugin, GitVersioning)
