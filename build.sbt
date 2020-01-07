import scalariform.formatter.preferences._

name := "Pure2048"

assemblyJarName in assembly := "2048.jar"

version := "0.0.1"

scalaVersion := "2.13.1"

scalacOptions ++= Seq(
  "-deprecation"
)

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core"   % "2.0.0",
  "org.typelevel" %% "cats-effect" % "2.0.0",

  "org.scalatest" %% "scalatest"   % "3.0.8" % "test"
)

//================================================================================
// Scalariform configurations
//================================================================================
scalariformPreferences := scalariformPreferences.value
  .setPreference(AlignArguments, true)
  .setPreference(AlignParameters, true) // seems to be buggy
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 80)
  .setPreference(AllowParamGroupsOnNewlines, true)
  .setPreference(DanglingCloseParenthesis, Prevent)
  .setPreference(DoubleIndentConstructorArguments, true)
  .setPreference(DoubleIndentMethodDeclaration, true)
  .setPreference(FirstParameterOnNewline, Preserve)
  .setPreference(FirstArgumentOnNewline, Preserve)
  .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, true)
  .setPreference(PreserveSpaceBeforeArguments, true)
  .setPreference(SingleCasePatternOnNewline, false)
  .setPreference(SpaceBeforeContextColon, true)
