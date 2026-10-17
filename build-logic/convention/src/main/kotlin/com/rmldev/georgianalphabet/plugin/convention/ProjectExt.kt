package com.rmldev.georgianalphabet.plugin.convention

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType


val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

val Project.projectJavaVersionInt
    get(): Int = 17

val Project.javaVersion
    get(): JavaVersion = JavaVersion.VERSION_17