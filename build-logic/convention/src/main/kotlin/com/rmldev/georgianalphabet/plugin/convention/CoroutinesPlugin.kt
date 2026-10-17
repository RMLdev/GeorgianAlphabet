package com.rmldev.georgianalphabet.plugin.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CoroutinesPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            dependencies {
                "implementation"(project(":core:coroutines"))
            }
        }
    }
}