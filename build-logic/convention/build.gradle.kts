plugins {
    `kotlin-dsl`
}

group = "com.rmldev.georgianalphabet"

kotlin {
    jvmToolchain(libs.versions.jvmToolchain.get().toInt())
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.kover.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
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

        register("staticAnalysisPlugin") {
            id = libs.plugins.rmldev.static.analysis.get().pluginId
            implementationClass =
                "com.rmldev.georgianalphabet.plugin.convention.StaticAnalysisPlugin"
        }

        register("testingPlugin") {
            id = libs.plugins.rmldev.testing.get().pluginId
            implementationClass =
                "com.rmldev.georgianalphabet.plugin.convention.TestingPlugin"
        }

        register("applicationPlugin") {
            id = libs.plugins.rmldev.android.application.get().pluginId
            implementationClass =
                "com.rmldev.georgianalphabet.plugin.convention.ApplicationPlugin"
        }
    }
}
