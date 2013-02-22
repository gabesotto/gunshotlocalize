scalaVersion := "2.10.0"

autoCompilerPlugins := true

libraryDependencies <+= scalaVersion { v => compilerPlugin("org.scala-lang.plugins" % "continuations" % v) }

scalacOptions ++= Seq("-unchecked", "-deprecation", "-P:continuations:enable")

resolvers ++= Seq(
  "Sonatype" at "https://oss.sonatype.org/content/groups/scala-tools/"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.1.0",
  "org.mongodb"       %% "casbah"     % "2.5.0"
)
