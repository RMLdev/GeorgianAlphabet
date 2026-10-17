plugins {
    alias(libs.plugins.rmldev.android.library)
    alias(libs.plugins.rmldev.hilt)
    alias(libs.plugins.rmldev.serialization)
    alias(libs.plugins.rmldev.coroutines)
}

android {
    namespace = "com.rmldev.georgianalphabet.core.datastore"
}

dependencies {
    api(project(":core:datastore:api"))

    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.tink)
}