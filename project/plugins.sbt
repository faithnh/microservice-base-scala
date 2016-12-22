// For code Format
addSbtPlugin("com.typesafe.sbt" % "sbt-scalariform" % "1.3.0")
// For test coverage
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.0")

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.8")

// plugins.sbt

addSbtPlugin("com.github.tototoshi" % "sbt-slick-codegen" % "0.1.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.1.1")

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.35"

addSbtPlugin("org.scalikejdbc" %% "scalikejdbc-mapper-generator" % "2.5.0")