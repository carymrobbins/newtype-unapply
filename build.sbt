scalaVersion := "2.12.4"

enablePlugins(JmhPlugin)

libraryDependencies += "io.estatico" %% "newtype" % "0.3.0"

resolvers += Resolver.sonatypeRepo("releases")
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)