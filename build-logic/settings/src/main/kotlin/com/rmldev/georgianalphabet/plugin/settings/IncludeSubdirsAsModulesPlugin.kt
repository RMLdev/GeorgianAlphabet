package com.rmldev.georgianalphabet.plugin.settings

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings

class IncludeSubdirsAsModulesPlugin : Plugin<Settings> {
    private val ignored = setOf("build", "src")

    override fun apply(target: Settings) {
        target.includeSubmodules("core")
        target.includeSubmodules("feature")
    }

    fun Settings.includeSubmodules(dirName: String) {
        rootDir.resolve(dirName)
            .walkTopDown()
            .onEnter { !it.name.startsWith(".") && it.name !in ignored }
            .filter { it.resolve("build.gradle.kts").isFile }
            .forEach { dir ->
                val path = dir.relativeTo(rootDir).invariantSeparatorsPath.replace('/', ':')
                include(":$path")
            }
    }
}