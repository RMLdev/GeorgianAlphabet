package com.rmldev.georgianalphabet.plugin.convention

import com.android.build.api.dsl.CommonExtension
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

            else -> TODO("Unsupported project extension $this ${T::class}")
        }.apply {
            jvmToolchain(projectJavaVersionInt)
        }
    }