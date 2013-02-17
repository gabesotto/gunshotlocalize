scalaVersion := "2.10.0"

autoCompilerPlugins := true

libraryDependencies <+= scalaVersion { v => compilerPlugin("org.scala-lang.plugins" % "continuations" % v) }

scalacOptions ++= Seq("-unchecked", "-deprecation", "-P:continuations:enable")

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Sonatype" at "https://oss.sonatype.org/content/groups/scala-tools/"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.1.0"

libraryDependencies += "com.github.tmingos" % "casbah_2.10" % "2.5.0-SNAPSHOT"
