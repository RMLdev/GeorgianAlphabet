package com.rmldev.georgianalphabet.plugin.convention

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AndroidLibPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")
            pluginManager.apply("com.autonomousapps.dependency-analysis")
            pluginManager.apply("com.rmldev.testing")

            configure<LibraryExtension> {
                resourcePrefix = path.split("""\W""".toRegex())
                    .drop(1)
                    .distinct()
                    .joinToString(separator = "_")
                    .lowercase() + "_"

                defaultConfig.apply {
                    minSdk = AndroidProjectConfig.MIN_SDK

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                compileSdk {
                    version = release(AndroidProjectConfig.COMPILE_SDK) {
                        minorApiLevel = AndroidProjectConfig.COMPILE_SDK_MINOR_LEVEL
                    }
                }

                configureKotlin<KotlinAndroidProjectExtension>()
            }
        }
    }
}