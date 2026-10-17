package com.rmldev.georgianalphabet.plugin.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class FeatureUiPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.rmldev.plugin.hilt")
            pluginManager.apply("com.rmldev.android.library")
            pluginManager.apply("com.rmldev.android.compose")

            dependencies {
                "implementation"(project(":core:ui"))
                "implementation"(project(":core:designsystem"))

//                "implementation"(libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                "implementation"(libs.findLibrary("androidx.runtime").get())
                "implementation"(libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
                "implementation"(libs.findLibrary("androidx.hilt.lifecycle.viewModelCompose").get())
                "implementation"(libs.findLibrary("androidx.navigation3.runtime").get())
            }
        }
    }
}