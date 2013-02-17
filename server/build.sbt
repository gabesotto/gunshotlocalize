scalaVersion := "2.10.0"

autoCompilerPlugins := true

libraryDependencies <+= scalaVersion { v => compilerPlugin("org.scala-lang.plugins" % "continuations" % v) }

scalacOptions ++= Seq("-unchecked", "-deprecation", "-P:continuations:enable")

resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Sonatype"            at "https://oss.sonatype.org/content/groups/scala-tools/",
  "Sonatype Snapshots"  at "https://oss.sonatype.org/content/repositories/snapshots/",
  "spray repo"          at "http://repo.spray.io/"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka"  %% "akka-actor"     % "2.1.0",
  "org.mongodb"        %% "casbah"         % "2.5.0",
  "io.spray"           %  "spray-can"      % "1.1-M7",
  "io.spray"           %  "spray-routing"  % "1.1-M7"
)
