plugins {
    alias(libs.plugins.rmldev.android.library)
    alias(libs.plugins.rmldev.android.compose)
}

android {
    namespace = "com.rmldev.georgianalphabet.core.ui"
}

dependencies {
    api(project(":core:navigation:api"))
    api(libs.androidx.lifecycle.viewModel.navigation3)
    implementation(libs.androidx.lifecycle.runtimeCompose)

    runtimeOnly(libs.androidx.navigation3.runtime)
}