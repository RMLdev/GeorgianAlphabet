package com.rmldev.georgianalphabet.plugin.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

class PureKotlinLibPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        applyPlugin("kotlin-jvm")
        applyPlugin("dependency-analysis")
        applyPlugin("rmldev-testing")
        applyPlugin("rmldev-static-analysis")

        configureKotlin<KotlinJvmProjectExtension>()
    }
}
