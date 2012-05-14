resolvers ++= Seq(
    DefaultMavenRepository,
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
    "Typesafe Other Repository" at "http://repo.typesafe.com/typesafe/repo/",
    "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"
)
