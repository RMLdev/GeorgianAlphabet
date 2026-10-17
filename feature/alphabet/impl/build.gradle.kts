plugins {
    alias(libs.plugins.rmldev.feature.impl)
}

dependencies {
    api(project(":feature:alphabet:api"))
}