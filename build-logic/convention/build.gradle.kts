/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    `kotlin-dsl`
}

group = "com.rmldev.georgianalphabet"

kotlin {
    jvmToolchain(17)
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("pureKotlinLibPlugin") {
            id = libs.plugins.rmldev.kotlin.library.get().pluginId
            implementationClass =
                "com.rmldev.georgianalphabet.plugin.convention.PureKotlinLibPlugin"
        }

        register("hiltModulePlugin") {
            id = libs.plugins.rmldev.hilt.get().pluginId
            implementationClass =
                "com.rmldev.georgianalphabet.plugin.convention.HiltModulePlugin"
        }

        register("androidLibPlugin") {
            id = libs.plugins.rmldev.android.library.get().pluginId
            implementationClass =
                "com.rmldev.georgianalphabet.plugin.convention.AndroidLibPlugin"
        }

        register("androidComposePlugin") {
            id = libs.plugins.rmldev.android.compose.get().pluginId
            implementationClass =
                "com.rmldev.georgianalphabet.plugin.convention.AndroidComposePlugin"
        }

        register("serializationPlugin") {
            id = libs.plugins.rmldev.serialization.get().pluginId
            implementationClass =
                "com.rmldev.georgianalphabet.plugin.convention.SerializationPlugin"
        }

        register("coroutinesPlugin") {
            id = libs.plugins.rmldev.coroutines.get().pluginId
            implementationClass =
                "com.rmldev.georgianalphabet.plugin.convention.CoroutinesPlugin"
        }

        register("featureApiPlugin") {
            id = libs.plugins.rmldev.feature.api.get().pluginId
            implementationClass =
                "com.rmldev.georgianalphabet.plugin.convention.FeatureApiPlugin"
        }

        register("featureImplPlugin") {
            id = libs.plugins.rmldev.feature.impl.get().pluginId
            implementationClass =
                "com.rmldev.georgianalphabet.plugin.convention.FeatureImplPlugin"
        }

        register("featureUiPlugin") {
            id = libs.plugins.rmldev.feature.ui.get().pluginId
            implementationClass =
                "com.rmldev.georgianalphabet.plugin.convention.FeatureUiPlugin"
        }

        register("applicationPlugin") {
            id = libs.plugins.rmldev.android.application.get().pluginId
            implementationClass =
                "com.rmldev.georgianalphabet.plugin.convention.ApplicationPlugin"
        }
    }
}
