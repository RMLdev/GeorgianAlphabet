package com.rmldev.georgianalphabet.plugin.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class FeatureApiPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            applyPlugin("rmldev-kotlin-library")
            applyPlugin("rmldev-serialization")

            dependencies {
                "api"(project(":core:navigation:api"))
            }
        }
    }
}