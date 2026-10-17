// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.dependency.analysis)
    alias(libs.plugins.kover)
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.spotless)
}

// Merged coverage report: ./gradlew koverHtmlReport (build/reports/kover/html)
dependencies {
    // skip intermediate projects (:core, :feature:alphabet) that have no build.gradle.kts
    subprojects.filter { it.buildFile.exists() }.forEach { kover(it) }
}

// Unused and misdeclared dependencies: ./gradlew buildHealth

// ktlint checks only files changed against origin/main so the whole project
// isn't reformatted at once. Fix: ./gradlew spotlessApply
spotless {
    ratchetFrom("origin/main")

    kotlin {
        target("**/*.kt")
        targetExclude("**/build/**")
        ktlint(libs.versions.ktlint.get())
    }
    kotlinGradle {
        target("**/*.kts")
        targetExclude("**/build/**")
        ktlint(libs.versions.ktlint.get())
    }
}
