plugins {
    alias(libs.plugins.rmldev.android.library)
    alias(libs.plugins.rmldev.android.compose)
}

android {
    namespace = "com.rmldev.georgianalphabet.core.designsystem"
}

dependencies {
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material3.adaptive)
    api(libs.androidx.compose.runtime)

    implementation(libs.androidx.compose.ui.text.google.fonts)
}