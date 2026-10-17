package com.rmldev.georgianalphabet.plugin.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * JVM unit tests: JUnit4 + kotlin.test, kotlinx-coroutines-test, Turbine and Kover.
 * Applied automatically by the base plugins (android library/application, kotlin library).
 */
class TestingPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            applyPlugin("kover")

            dependencies {
                "testImplementation"(libs.findLibrary("junit").get())
                "testImplementation"(libs.findLibrary("kotlin-test-junit").get())
                "testImplementation"(libs.findLibrary("kotlinx-coroutines-test").get())
                "testImplementation"(libs.findLibrary("turbine").get())
            }

            pluginManager.withPlugin("com.android.base") {
                configure<CommonExtension> {
                    // android.util.Log and other android.jar stubs return default values
                    testOptions.unitTests.isReturnDefaultValues = true
                }
            }
        }
    }
}
