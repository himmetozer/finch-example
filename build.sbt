name := "finch-example"

version := "0.1"

scalaVersion := "2.12.8"

val finchV = "0.28.0"
val twitterServerV = "19.3.0"
val circeVersion = "0.10.1"
val quill = "2.4.2"
val circe = "0.9.3"
val metricsV = "4.0.4"
val prometheusSimpleClientV = "0.6.0"
val slf4jV = "1.7.25"
val scalaLoggingV = "3.9.0"

libraryDependencies ++= Seq(
  "com.github.finagle"       %% "finch-core"                     % finchV,
  "com.github.finagle"       %% "finch-generic"                  % finchV,
  "com.github.finagle"       %% "finch-circe"                    % finchV,
  "com.twitter"              %% "twitter-server"                 % twitterServerV,
  "com.twitter"              %% "finagle-stats"                  % twitterServerV,
  "com.twitter"              %% "twitter-server-logback-classic" % twitterServerV,
  "ch.qos.logback"           % "logback-classic"                 % "1.2.3",
  "io.getquill"              %% "quill-async-postgres"           % quill,
  "com.github.carrambata"    %% "retry4s"                        % "0.1.0" exclude ("io.netty", "netty-common"),
  "io.circe"                 %% "circe-generic"                  % circe
)
