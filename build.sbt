
name := "jTextPortal"

version := "1.0"

scalaVersion := "2.9.1"

resolvers ++= Seq("Scala-Tools Maven2  Repository" at "http://scala-tools.org//repo-releases/",
	          "Maven Natives Repo" at "http://mavennatives.googlecode.com/svn/repo/",
              "slick" at "http://slick.cokeandcode.com/mavenrepo/",
			  "Scala Tools Snapshots" at "http://scala-tools.org/repo-snapshots/",
              "ScalaNLP Maven2" at "http://repo.scalanlp.org/repo",
			  Classpaths.typesafeResolver)
			  
resolvers += Resolver.url("sbt-plugin-releases", new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns)
			  
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse" % "1.5.0")

addSbtPlugin("com.github.philcali" % "sbt-lwjgl-plugin" % "3.1.1")

libraryDependencies += "org.slf4j" % "slf4j-api" % "1.6.1"

libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.6.1"

libraryDependencies += "log4j" % "log4j" % "1.2.14"

libraryDependencies += "slick" % "slick" % "274" intransitive()

libraryDependencies += "org.scalala" %% "scalala" % "1.0.0.RC2"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.7.1" % "test"

seq(lwjglSettings: _*)
