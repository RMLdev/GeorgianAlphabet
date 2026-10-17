package com.rmldev.georgianalphabet.plugin.convention

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import java.util.Properties

class ApplicationPlugin : Plugin<Project> {

    /**
     * Release signing comes from environment variables or local.properties (not in git):
     * RELEASE_STORE_FILE, RELEASE_STORE_PASSWORD, RELEASE_KEY_ALIAS, RELEASE_KEY_PASSWORD.
     * Without them the release build is unsigned.
     */
    private fun Project.configureReleaseSigning(extension: ApplicationExtension) {
        val localProperties = Properties().apply {
            rootProject.file("local.properties").takeIf { it.isFile }?.inputStream()?.use(::load)
        }

        fun property(name: String): String? =
            providers.environmentVariable(name).orNull ?: localProperties.getProperty(name)

        val storeFile = property("RELEASE_STORE_FILE") ?: return

        extension.signingConfigs.create("release") {
            this.storeFile = rootProject.file(storeFile)
            storePassword = property("RELEASE_STORE_PASSWORD")
            keyAlias = property("RELEASE_KEY_ALIAS")
            keyPassword = property("RELEASE_KEY_PASSWORD")
        }
        extension.buildTypes.getByName("release").signingConfig = extension.signingConfigs.getByName("release")
    }


    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.application")
            pluginManager.apply("com.autonomousapps.dependency-analysis")
            pluginManager.apply("com.rmldev.testing")

            configure<ApplicationExtension> {
                defaultConfig.apply {
                    applicationId = AndroidProjectConfig.APPLICATION_ID

                    targetSdk = AndroidProjectConfig.TARGET_SDK
                    minSdk = AndroidProjectConfig.MIN_SDK

                    // Passed via environment variables on CI (VERSION_CODE = build number)
                    versionCode = providers.environmentVariable("VERSION_CODE")
                        .map(String::toInt)
                        .getOrElse(AndroidProjectConfig.VERSION_CODE)
                    versionName = providers.environmentVariable("VERSION_NAME")
                        .getOrElse(AndroidProjectConfig.VERSION_NAME)

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                compileSdk {
                    version = release(AndroidProjectConfig.COMPILE_SDK) {
                        minorApiLevel = AndroidProjectConfig.COMPILE_SDK_MINOR_LEVEL
                    }
                }

                configureReleaseSigning(this)

                configureKotlin<KotlinAndroidProjectExtension>()
            }
        }
    }
}