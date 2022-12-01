package com.onairentertainment.plugin

import com.gilcloud.sbt.gitlab.{GitlabCredentials, GitlabPlugin}
import com.onairentertainment.plugin.GitLabPackageRegistryPlugin.{CustomAuthHeader, PackageRegistryToken}
import sbt.Def
import sbt.Keys.*

object Publish {

  val Settings: Seq[Def.Setting[_]] = {
    EnvVariableHelper.getEnvironmentVariable(PackageRegistryToken) match {

      case Some(token) =>
        println(s"$PackageRegistryToken found...")
        Seq(
          GitlabPlugin.autoImport.gitlabCredentials := Some(GitlabCredentials(CustomAuthHeader, token)),
          GitlabPlugin.autoImport.gitlabProjectId := Some(71),
          publishMavenStyle := true,
          aether.AetherKeys.aetherCustomHttpHeaders := Map(CustomAuthHeader -> token)
        )

      case _ =>
        println(s"$PackageRegistryToken not found...")
        Seq.empty
    }
  }

  val DoNotPublishToRegistry: Seq[Def.Setting[_]] = Seq(
    publish / skip := true,
    publish := {},
    publishLocal := {}
  )

  val DoNotPublish: Seq[Def.Setting[_]] = DoNotPublishToRegistry ++ Seq(
    publishArtifact := false
  )
}
