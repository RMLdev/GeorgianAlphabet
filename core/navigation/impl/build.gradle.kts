plugins {
    alias(libs.plugins.rmldev.android.library)
    alias(libs.plugins.rmldev.android.compose)
    alias(libs.plugins.rmldev.hilt)
    alias(libs.plugins.rmldev.serialization)
}

android {
    namespace = "com.rmldev.georgianalphabet.core.navigation.impl"
}

dependencies {
    api(project(":core:navigation:api"))
    implementation(libs.androidx.lifecycle.viewModel.navigation3)
    implementation(libs.androidx.navigation3.runtime)

    runtimeOnly(libs.androidx.compose.runtime)
}