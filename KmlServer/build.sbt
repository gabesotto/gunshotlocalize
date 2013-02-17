scalaVersion := "2.10.0"

resolvers ++= Seq(
  "Sonatype"   at "https://oss.sonatype.org/content/groups/scala-tools/",
  "spray repo" at "http://repo.spray.io/"
)

libraryDependencies ++= Seq(
  "org.mongodb" %% "casbah"        % "2.5.0",
  "io.spray"    %  "spray-can"     % "1.1-M7",
  "io.spray"    %  "spray-routing" % "1.1-M7"
)
