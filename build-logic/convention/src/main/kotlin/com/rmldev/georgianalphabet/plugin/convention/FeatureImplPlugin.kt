package com.rmldev.georgianalphabet.plugin.convention

import org.gradle.api.Plugin
import org.gradle.api.Project

class FeatureImplPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.rmldev.plugin.hilt")
            pluginManager.apply("com.rmldev.plugin.kotlin.library")
        }
    }
}