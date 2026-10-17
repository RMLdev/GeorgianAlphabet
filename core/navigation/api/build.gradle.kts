plugins {
    alias(libs.plugins.rmldev.kotlin.library)
    alias(libs.plugins.rmldev.serialization)
}

dependencies {
    api(libs.kotlinx.serialization.json)
}