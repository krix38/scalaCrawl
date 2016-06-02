lazy val root = (project in file(".")).
settings(
  name := "scalaCrawl",
  version := "1.0",
  scalaVersion := "2.11.8"
)

libraryDependencies += "org.jsoup" % "jsoup" % "1.7.2"