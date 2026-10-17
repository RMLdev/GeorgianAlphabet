package com.rmldev.georgianalphabet.plugin.convention

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

/** JVM toolchain version from the catalog (`versions.jvmToolchain`). */
val Project.projectJavaVersionInt
    get(): Int = libs.findVersion("jvmToolchain").get().requiredVersion.toInt()

/** Applies a plugin by its alias from the catalog `[plugins]`, e.g. `rmldev-hilt`. */
fun Project.applyPlugin(alias: String) {
    pluginManager.apply(libs.findPlugin(alias).get().get().pluginId)
}
