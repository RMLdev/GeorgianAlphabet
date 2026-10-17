plugins {
    alias(libs.plugins.rmldev.android.library)
    alias(libs.plugins.rmldev.hilt)
    alias(libs.plugins.rmldev.serialization)
    alias(libs.plugins.rmldev.coroutines)
}

android {
    namespace = "com.rmldev.georgianalphabet.core.network.impl"
}

dependencies {
    api(project(":core:network:api"))

    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
}