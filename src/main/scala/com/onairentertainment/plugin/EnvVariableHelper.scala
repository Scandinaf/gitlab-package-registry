package com.onairentertainment.plugin

object EnvVariableHelper {

  type EnvVariable = String

  def getEnvironmentVariable(variableName: String): Option[EnvVariable] =
    sys.env.get(variableName)

  def getRequiredEnvironmentVariable(variableName: String): EnvVariable =
    getEnvironmentVariable(variableName)
      .getOrElse(
        sys.error(
          s"The following required environment variable could not be found - $variableName"
        )
      )
}
