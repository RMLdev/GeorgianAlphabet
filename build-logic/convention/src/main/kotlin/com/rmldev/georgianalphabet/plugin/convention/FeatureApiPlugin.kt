package com.rmldev.georgianalphabet.plugin.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class FeatureApiPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.rmldev.plugin.kotlin.library")
            pluginManager.apply("com.rmldev.serialization")

            dependencies {
                "api"(project(":core:navigation:api"))
            }
        }
    }
}