name := "ScalaPlayground"

version := "1.0"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.0" % "test"

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.10" % "2.2-M1"

libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "1.2.0"

libraryDependencies += "com.datastax.cassandra" % "cassandra-driver-core" % "2.0.2"

libraryDependencies += "com.jsuereth" % "scala-arm_2.10" % "1.3"