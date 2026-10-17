package com.rmldev.georgianalphabet.plugin.convention

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

inline fun <reified T : KotlinBaseExtension> Project.configureKotlin() =
    configure<T> {
        when (this) {
            is KotlinAndroidProjectExtension -> {
                compilerOptions
            }

            is KotlinJvmProjectExtension -> {
                compilerOptions
            }

            else -> error("Unsupported Kotlin extension ${T::class.simpleName}: only Android and JVM projects are supported")
        }.apply {
            jvmToolchain(projectJavaVersionInt)
        }
    }