lazy val akkaHttpVersion = "10.1.12"
lazy val akkaVersion    = "2.6.9"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "com.test",
      scalaVersion    := "2.13.1"
    )),
    name := "bid-service",
    libraryDependencies ++= Seq(
      "com.typesafe.akka"          %% "akka-http"                % akkaHttpVersion,
      "com.typesafe.akka"          %% "akka-http-spray-json"     % akkaHttpVersion,
      "com.typesafe.akka"          %% "akka-actor-typed"         % akkaVersion,
      "com.typesafe.akka"          %% "akka-stream"              % akkaVersion,
      "ch.qos.logback"             % "logback-classic"           % "1.2.3",
      "com.typesafe.scala-logging" %% "scala-logging"            % "3.9.2",
      "io.netty"                   % "netty-handler"             % "4.1.68.Final",

      "com.typesafe.akka" %% "akka-http-testkit"        % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"                % "3.1.0"         % Test,
      "org.mockito"       %% "mockito-scala"            % "1.15.0"        % Test,
      "org.json4s"        %% "json4s-jackson"           % "3.6.9"         % Test
    )
  )
