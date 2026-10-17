plugins {
    alias(libs.plugins.rmldev.android.library)
}

android {
    namespace = "com.rmldev.georgianalphabet.core.ui"
}

dependencies {
    api(project(":core:navigation:api"))
    api(libs.paging.common)
    api(libs.androidx.lifecycle.viewModel.navigation3)

    runtimeOnly(libs.androidx.runtime)
    runtimeOnly(libs.androidx.navigation3.runtime)
}