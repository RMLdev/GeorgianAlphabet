plugins {
    alias(libs.plugins.rmldev.feature.impl)
    alias(libs.plugins.rmldev.coroutines)
}

dependencies {
    api(project(":feature:alphabet:api"))
    implementation(project(":feature:alphabet:db"))
}
