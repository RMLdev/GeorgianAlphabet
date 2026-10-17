package com.rmldev.georgianalphabet.plugin.convention

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class ApplicationPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.application")
            pluginManager.apply("com.rmldev.testing")

            configure<ApplicationExtension> {
                defaultConfig.apply {
                    applicationId = AndroidProjectConfig.APPLICATION_ID

                    targetSdk = AndroidProjectConfig.TARGET_SDK
                    minSdk = AndroidProjectConfig.MIN_SDK

                    versionCode = AndroidProjectConfig.VERSION_CODE
                    versionName = AndroidProjectConfig.VERSION_NAME

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