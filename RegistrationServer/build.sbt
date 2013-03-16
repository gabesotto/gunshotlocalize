scalaVersion := "2.10.0"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

resolvers ++= Seq(
  "Sonatype"   at "https://oss.sonatype.org/content/groups/scala-tools/",
  "spray repo" at "http://repo.spray.io/"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor"    % "2.1.0",
  "org.mongodb"       %% "casbah"        % "2.5.0",
  "io.spray"          %  "spray-can"     % "1.1-M7",
  "io.spray"          %  "spray-routing" % "1.1-M7",
  "javax.mail"        %  "mail"          % "1.4.7"
)
