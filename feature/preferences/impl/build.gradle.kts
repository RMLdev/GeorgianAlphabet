plugins {
    alias(libs.plugins.rmldev.feature.impl)
    alias(libs.plugins.rmldev.serialization)
    alias(libs.plugins.rmldev.coroutines)
}

dependencies {
    api(project(":feature:preferences:api"))
    implementation(project(":core:datastore:api"))
}
