plugins {
    alias(libs.plugins.rmldev.kotlin.library)
}

dependencies {
    api(libs.kotlinx.coroutines.core)
    api(libs.hilt.core)
    api(libs.retrofit.core)
}