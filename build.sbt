name := """kenyapatches"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
   jdbc,
  evolutions,
  specs2 % Test,
  "org.webjars" %% "webjars-play" % "2.4.0-1",
  "org.webjars" % "bootstrap" % "3.3.5",
  "jp.t2v" %% "play2-auth" % "0.14.1",
    "jp.t2v" %% "play2-auth-social" % "0.14.1",
    "jp.t2v" %% "play2-auth-test" % "0.14.1" % "test",
    "org.mindrot" % "jbcrypt" % "0.3m",
  "org.scalikejdbc" %% "scalikejdbc"                  % "2.2.9",
  "org.scalikejdbc" %% "scalikejdbc-config"           % "2.2.9",
  "org.scalikejdbc" %% "scalikejdbc-play-initializer" % "2.4.2",
  "org.scalikejdbc"      %% "scalikejdbc-play-fixture"      % "2.4.2",
  "ch.qos.logback"  %  "logback-classic"    % "1.1.3",
    play.sbt.Play.autoImport.cache,
  "mysql" % "mysql-connector-java" % "5.1.27"
)

libraryDependencies += "org.postgresql" % "postgresql" % "9.4-1200-jdbc41" 

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

//auto-gen of case classes
scalikejdbcSettings