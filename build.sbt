val Version = new {
  val Cats = "2.10.0"
  val MUnit = "1.0.0-M11"
  val Scala = "3.3.3"
}

inThisBuild(
  Def.settings(
    developers := List(Developer("taig", "Niklas Klein", "mail@taig.io", url("https://taig.io/"))),
    dynverVTagPrefix := false,
    homepage := Some(url("https://github.com/taig/hmap/")),
    licenses := List("MIT" -> url("https://raw.githubusercontent.com/taig/hmap/main/LICENSE")),
    organization := "io.taig",
    organizationHomepage := Some(url("https://taig.io/")),
    scalaVersion := Version.Scala,
    versionScheme := Some("early-semver")
  )
)

lazy val root = project
  .in(file("."))
  .settings(noPublishSettings)
  .aggregate(core.jvm, core.js, core.native)

lazy val core = crossProject(JVMPlatform, JSPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .withoutSuffixFor(JVMPlatform)
  .in(file("./modules/core"))
  .enablePlugins(BlowoutYamlPlugin)
  .settings(
    Compile / console / scalacOptions -= "-Wunused:all",
    Compile / scalacOptions ++= "-source:future" :: "-rewrite" :: "-new-syntax" :: "-Wunused:all" :: Nil,
    blowoutGenerators ++= {
      val workflows = file(".github") / "workflows"
      BlowoutYamlGenerator.lzy(workflows / "main.yml", GitHubActionsGenerator.main) ::
        BlowoutYamlGenerator.lzy(workflows / "branches.yml", GitHubActionsGenerator.branches) ::
        Nil
    },
    libraryDependencies ++=
      "org.typelevel" %%% "cats-core" % Version.Cats ::
        "org.scalameta" %%% "munit" % Version.MUnit % "test" ::
        Nil,
    name := "hmap"
  )
