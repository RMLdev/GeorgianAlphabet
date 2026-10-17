plugins {
    alias(libs.plugins.rmldev.android.application)
    alias(libs.plugins.rmldev.android.compose)
    alias(libs.plugins.rmldev.hilt)
    alias(libs.plugins.rmldev.coroutines)
    alias(libs.plugins.rmldev.serialization)
}

android {
    namespace = "com.rmldev.georgianalphabet"

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-dev"
        }

        release {
            optimization {
                enable = true
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.material3.navigationSuite)
    implementation(libs.androidx.compose.material3.adaptive.navigation3)
    implementation(libs.androidx.navigation3.runtime)

    implementation(project(":core:coroutines"))
    implementation(project(":core:datastore:impl"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:navigation:impl"))
    implementation(project(":core:network:impl"))
    implementation(project(":core:ui"))

    implementation(project(":feature:alphabet:impl"))
    implementation(project(":feature:alphabet:ui"))
    implementation(project(":feature:preferences:impl"))

    testImplementation(libs.junit)
}
