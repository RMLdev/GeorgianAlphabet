plugins {
    alias(libs.plugins.rmldev.feature.ui)
    alias(libs.plugins.rmldev.serialization)
}

android {
    namespace = "com.rmldev.georgianalphabet.feature.alphabet.ui"
}

dependencies {
    api(project(":feature:alphabet:api"))
    implementation(libs.kotlinx.collections.immutable)
}