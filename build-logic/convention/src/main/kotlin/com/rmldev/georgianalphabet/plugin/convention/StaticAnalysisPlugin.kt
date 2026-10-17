package com.rmldev.georgianalphabet.plugin.convention

import dev.detekt.gradle.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * detekt with the shared `config/detekt/detekt.yml` and a per-module baseline (`detekt-baseline.xml`).
 * Update baselines: `./gradlew detektBaseline`.
 */
class StaticAnalysisPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("dev.detekt")

            configure<DetektExtension> {
                buildUponDefaultConfig.set(true)
                config.setFrom(rootProject.file("config/detekt/detekt.yml"))
                baseline.set(file("detekt-baseline.xml"))
            }
        }
    }
}
