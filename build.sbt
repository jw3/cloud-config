organization := "com.rxthings"
name := "cloud-config"
version := "0.2-SNAPSHOT"
licenses +=("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))

scalaVersion := "2.11.7"
scalacOptions += "-target:jvm-1.8"

resolvers += "jw3 at bintray" at "https://dl.bintray.com/jw3/maven"
credentials += Credentials(Path.userHome / ".bintray" / ".credentials")

libraryDependencies ++= {
    val akkaVersion = "2.4.1"
    val akkaStreamVersion = "2.0.1"

    Seq(
        "com.typesafe" % "config" % "1.3.0",
        "net.ceedubs" %% "ficus" % "1.1.2",
        "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",

        "com.typesafe.akka" %% "akka-actor" % akkaVersion,
        "com.typesafe.akka" %% "akka-http-core-experimental" % akkaStreamVersion,

        "org.scalatest" %% "scalatest" % "2.2.5" % Test
    )
}
