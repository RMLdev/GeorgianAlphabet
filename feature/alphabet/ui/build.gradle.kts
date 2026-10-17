plugins {
    alias(libs.plugins.rmldev.feature.ui)
    alias(libs.plugins.rmldev.serialization)
    alias(libs.plugins.rmldev.coroutines)
}

android {
    namespace = "com.rmldev.georgianalphabet.feature.alphabet.ui"
}

dependencies {
    api(project(":feature:alphabet:api"))
    implementation(project(":feature:preferences:api"))
    implementation(libs.kotlinx.collections.immutable)
}
