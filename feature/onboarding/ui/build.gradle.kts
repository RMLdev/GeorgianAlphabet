plugins {
    alias(libs.plugins.rmldev.feature.ui)
    alias(libs.plugins.rmldev.serialization)
}

android {
    namespace = "com.rmldev.georgianalphabet.feature.onboarding.ui"
}

dependencies {
    api(project(":feature:onboarding:api"))
    implementation(project(":feature:alphabet:api"))
    implementation(project(":feature:preferences:api"))
}
