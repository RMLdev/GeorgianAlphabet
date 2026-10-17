package com.rmldev.georgianalphabet.plugin.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidComposePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            configure<CommonExtension> {
                configureCompose(this)
            }
//            pluginManager.withPlugin("com.android.library") {
//                configure<LibraryExtension> {
//                    configureCompose(this)
//                }
//            }
//
//            pluginManager.withPlugin("com.android.application") {
//
//            }

        }
    }

    private fun Project.configureCompose(commonExtension: CommonExtension) {
        commonExtension.apply {
            buildFeatures.apply {
                compose = true
            }
        }

        composeDependencies()
    }

    private fun Project.composeDependencies() = dependencies {
        val bom = libs.findLibrary("androidx-compose-bom").get()
        "implementation"(platform(bom))
        "implementation"(libs.findLibrary("androidx-compose-ui-tooling-preview").get())
        "debugImplementation"(libs.findLibrary("androidx-compose-ui-tooling").get())
    }
}