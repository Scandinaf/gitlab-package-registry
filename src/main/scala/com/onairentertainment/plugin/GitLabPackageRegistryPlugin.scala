package com.onairentertainment.plugin

import lmcoursier.definitions.Authentication
import lmcoursier.syntax._
import sbt.Keys.{csrConfiguration, publishMavenStyle, resolvers, updateClassifiers}
import sbt.librarymanagement.MavenRepository
import sbt.{AutoPlugin, PluginTrigger, Setting}

object GitLabPackageRegistryPlugin extends AutoPlugin {

  val PackageRegistryUri   = "PACKAGES_RW_URI"
  val PackageRegistryToken = "PACKAGES_RW_TOKEN"
  val CustomAuthHeader     = "Private-Token"

  override def trigger: PluginTrigger = allRequirements

  override def projectSettings: Seq[Setting[_]] = {
    val uri        = EnvVariableHelper.getRequiredEnvironmentVariable(PackageRegistryUri)
    val token      = EnvVariableHelper.getRequiredEnvironmentVariable(PackageRegistryToken)
    val repository = MavenRepository("gitlab", uri)
    val authentication = Authentication(
      user     = "user",
      password = "password",
      headers  = Seq((CustomAuthHeader, token)),
      optional = false,
      realmOpt = None
    )

    Seq(
      resolvers += repository,
      csrConfiguration ~= (_.addRepositoryAuthentication(repository.name, authentication)),
      updateClassifiers / csrConfiguration ~= (_.addRepositoryAuthentication(repository.name, authentication)),
      publishMavenStyle := true,
      aether.AetherKeys.aetherCustomHttpHeaders := Map(CustomAuthHeader -> token)
    )
  }
}
