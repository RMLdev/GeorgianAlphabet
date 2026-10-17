package com.rmldev.georgianalphabet.plugin.convention

import org.gradle.api.Plugin
import org.gradle.api.Project

class FeatureImplPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            applyPlugin("rmldev-hilt")
            applyPlugin("rmldev-kotlin-library")
        }
    }
}