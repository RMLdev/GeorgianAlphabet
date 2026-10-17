plugins {
    alias(libs.plugins.rmldev.android.library)
    alias(libs.plugins.rmldev.hilt)
    alias(libs.plugins.rmldev.coroutines)
}

android {
    namespace = "com.rmldev.georgianalphabet.core.audio"
}

dependencies {
    api(project(":core:audio:api"))
}
