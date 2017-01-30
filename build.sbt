






// ---------------------------------------------------------------------------------- 


lazy val osName =
  System.getProperty("os.name").split(" ")(0).toLowerCase()

lazy val startOnFirst =
  if (osName == "mac")
    Some("-XstartOnFirstThread")
  else
    None

val lwjglVersion = "3.1.1"


//// Setting up native library extraction ////

ivyConfigurations += config("natives")

lazy val nativeExtractions = SettingKey[Seq[(String, NameFilter, File)]](
  "native-extractions", "(jar name partial, sbt.NameFilter of files to extract, destination directory)"
)

lazy val extractNatives = TaskKey[Unit]("extract-natives", "Extracts native files")


//// Project Configuration ////

name := """jTextPortal"""

version := "1.0"

scalaVersion := "2.11.8"

scalacOptions ++= Seq(
  "-Xlint",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-unused",
  "-Ywarn-unused-import",
  "-unchecked",
  "-deprecation",
  "-feature",
  "-encoding", "UTF-8",
  "-target:jvm-1.8"
)

javacOptions ++= Seq(
  "-Xlint",
  "-encoding", "UTF-8",
  "-source", "1.8",
  "-target", "1.8"
)

testOptions += Tests.Argument("-oD")

javaOptions ++= {
  val options = List (
    s"-Djava.library.path=${baseDirectory.value}/lib/${osName}"
  )

  startOnFirst map (_ :: options) getOrElse options
}

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest"      % "3.0.1"      % "test",


  "org.lwjgl" % "lwjgl" % lwjglVersion,
  "org.lwjgl" % "lwjgl-assimp" % lwjglVersion,
  "org.lwjgl" % "lwjgl-bgfx" % lwjglVersion,
  "org.lwjgl" % "lwjgl-egl" % lwjglVersion,
  "org.lwjgl" % "lwjgl-glfw" % lwjglVersion,
  "org.lwjgl" % "lwjgl-jawt" % lwjglVersion,
  "org.lwjgl" % "lwjgl-jemalloc" % lwjglVersion,
  "org.lwjgl" % "lwjgl-lmdb" % lwjglVersion,
  "org.lwjgl" % "lwjgl-nanovg" % lwjglVersion,
  "org.lwjgl" % "lwjgl-nfd" % lwjglVersion,
  "org.lwjgl" % "lwjgl-nuklear" % lwjglVersion,
  "org.lwjgl" % "lwjgl-openal" % lwjglVersion,
  "org.lwjgl" % "lwjgl-opencl" % lwjglVersion,
  "org.lwjgl" % "lwjgl-opengl" % lwjglVersion,
  "org.lwjgl" % "lwjgl-opengles" % lwjglVersion,
  // "org.lwjgl" % "lwjgl-ovr" % lwjglVersion,
  "org.lwjgl" % "lwjgl-par" % lwjglVersion,
  "org.lwjgl" % "lwjgl-sse" % lwjglVersion,
  "org.lwjgl" % "lwjgl-stb" % lwjglVersion,
  "org.lwjgl" % "lwjgl-tinyfd" % lwjglVersion,
  "org.lwjgl" % "lwjgl-vulkan" % lwjglVersion,
  "org.lwjgl" % "lwjgl-xxhash" % lwjglVersion,


  "org.lwjgl" % "lwjgl" % lwjglVersion  % "natives" classifier "natives-linux",
  "org.lwjgl" % "lwjgl-assimp" % lwjglVersion  % "natives" classifier "natives-linux",
  "org.lwjgl" % "lwjgl-bgfx" % lwjglVersion  % "natives" classifier "natives-linux",
  "org.lwjgl" % "lwjgl-glfw" % lwjglVersion  % "natives" classifier "natives-linux",
  "org.lwjgl" % "lwjgl-jemalloc" % lwjglVersion  % "natives" classifier "natives-linux",
  "org.lwjgl" % "lwjgl-lmdb" % lwjglVersion  % "natives" classifier "natives-linux",
  "org.lwjgl" % "lwjgl-nanovg" % lwjglVersion  % "natives" classifier "natives-linux",
  "org.lwjgl" % "lwjgl-nfd" % lwjglVersion  % "natives" classifier "natives-linux",
  "org.lwjgl" % "lwjgl-nuklear" % lwjglVersion  % "natives" classifier "natives-linux",
  "org.lwjgl" % "lwjgl-openal" % lwjglVersion  % "natives" classifier "natives-linux",
  "org.lwjgl" % "lwjgl-opengl" % lwjglVersion  % "natives" classifier "natives-linux",
  "org.lwjgl" % "lwjgl-opengles" % lwjglVersion  % "natives" classifier "natives-linux",
  // "org.lwjgl" % "lwjgl-ovr" % lwjglVersion  % "natives" classifier "natives-linux",
  "org.lwjgl" % "lwjgl-par" % lwjglVersion  % "natives" classifier "natives-linux",
  "org.lwjgl" % "lwjgl-sse" % lwjglVersion  % "natives" classifier "natives-linux",
  "org.lwjgl" % "lwjgl-stb" % lwjglVersion  % "natives" classifier "natives-linux",
  "org.lwjgl" % "lwjgl-tinyfd" % lwjglVersion  % "natives" classifier "natives-linux",
  "org.lwjgl" % "lwjgl-xxhash" % lwjglVersion  % "natives" classifier "natives-linux",


  // scalanlp
  "org.scalanlp" %% "breeze" % "0.12",

  // Native libraries are not included by default. add this if you want them (as of 0.7)
  // Native libraries greatly improve performance, but increase jar sizes. 
  // It also packages various blas implementations, which have licenses that may or may not
  // be compatible with the Apache License. No GPL code, as best I know.
  "org.scalanlp" %% "breeze-natives" % "0.12",

  // The visualization library is distributed separately as well.
  // It depends on LGPL code
  "org.scalanlp" %% "breeze-viz" % "0.12"
)

nativeExtractions <<= (baseDirectory) { base => Seq (

  ("lwjgl-platform-3.1.1-natives-linux.jar",   AllPassFilter, base / "lib/linux"),
  ("lwjgl-platform-3.1.1-natives-windows.jar", AllPassFilter, base / "lib/windows"),
  ("lwjgl-platform-3.1.1-natives-osx.jar",     AllPassFilter, base / "lib/mac")
)}

extractNatives := {
   val base = baseDirectory.value
  val _classifiers = updateClassifiers.value
  val res = _classifiers.select(artifact =  artifactFilter(extension = "jar", classifier = "natives-linux"))
  res.foreach { file =>
    IO.unzip(file, base / "lib/linux")
  }
  // println("**")
  // println(_classifiers)
  // val st = 
  // val deps = libraryDependencies.value
  // val nativeDeps = deps.filter(d => d.configurations.exists(s => s.contains("natives")))
  
  //   
  //    val cp = (dependencyClasspath in Runtime).value
  //    cp.foreach{ f =>
  //      println(f)
  //      f.metadata.get(artifact.key).foreach { artifact =>
  //        println(artifact)
  //        if (artifact.classifier.exists(s => s == "natives-linux")) {
  //          IO.unzip(f.data, base / "lib/linux")
  //        }
  //      }
  //      // println(f.metadata.get(artifact.key).get.configurations)
  //      // println(s"${f.metadata.get(moduleID.key)} => ${f.data}")
  //    }


  // nativeDeps.foreach { f =>
  //    // println(s"${f.metadata.get(moduleID.key)} => ${f.data}")
  // }
}
// extractNatives <<= (nativeExtractions, update) map { (ne, up) =>
//   val jars = up.select(configurationFilter("natives"))

//   ne foreach { case (jarName, fileFilter, outputPath) =>
//     jars find(_.getName.contains(jarName)) map { jar =>
//       IO.unzip(jar, outputPath)
//     }
//   }
// }

//compile in Compile <<= (compile in Compile) dependsOn (extractNatives)

fork in run := true

cancelable := true

exportJars := true

