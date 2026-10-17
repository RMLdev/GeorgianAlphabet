plugins {
    alias(libs.plugins.rmldev.kotlin.library)
    alias(libs.plugins.rmldev.hilt)
}

dependencies {
    api(libs.kotlinx.coroutines.core)
}