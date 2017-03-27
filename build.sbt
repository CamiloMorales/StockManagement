name := """chapter5-demo"""

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala, SbtWeb)

//doc in Compile <<= target.map(_ / "none")

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  "com.typesafe.play" %% "anorm" 				% "2.4.0",
  //cache,
  javaWs,
  "org.xerial"      % "sqlite-jdbc"     % "3.16.1",
  "org.webjars" 		%% "webjars-play" 	% "2.4.0-1",
  "org.webjars" 		%  "bootstrap" 			% "3.1.1-2",
  "org.webjars"     %  "flat-ui"        % "bcaf2de95e",
  "org.webjars" 		%  "react" 				  % "0.13.3",
  "org.webjars" 		%  "marked" 			  % "0.3.2",
  "org.webjars"     %  "jquery"         % "2.1.4"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

enablePlugins(SbtNativePackager)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

fork in run := true