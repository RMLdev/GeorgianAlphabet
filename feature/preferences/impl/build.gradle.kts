plugins {
    alias(libs.plugins.rmldev.feature.impl)
    alias(libs.plugins.rmldev.serialization)
}

dependencies {
    api(project(":feature:preferences:api"))
    implementation(project(":core:datastore:api"))
    implementation(libs.kotlinx.coroutines.core)
}
