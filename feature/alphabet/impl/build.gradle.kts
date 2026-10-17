plugins {
    alias(libs.plugins.rmldev.feature.impl)
}

dependencies {
    api(project(":feature:alphabet:api"))
    implementation(project(":feature:alphabet:db"))
    implementation(libs.kotlinx.coroutines.core)
}
