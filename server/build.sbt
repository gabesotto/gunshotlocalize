scalaVersion := "2.10.0"

autoCompilerPlugins := true

libraryDependencies <+= scalaVersion { v => compilerPlugin("org.scala-lang.plugins" % "continuations" % v) }

scalacOptions ++= Seq("-unchecked", "-deprecation", "-P:continuations:enable")

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Sonatype" at "https://oss.sonatype.org/content/groups/scala-tools/"

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.10.0-RC1" % "2.1.0-RC1"

libraryDependencies += "com.mongodb.casbah" % "casbah_2.9.0-1" % "2.1.5.0"
