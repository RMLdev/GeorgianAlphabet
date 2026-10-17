plugins {
    alias(libs.plugins.rmldev.android.application)
    alias(libs.plugins.rmldev.android.compose)
    alias(libs.plugins.rmldev.hilt)
    alias(libs.plugins.rmldev.coroutines)
    alias(libs.plugins.rmldev.serialization)
    alias(libs.plugins.room)
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

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.compose.material3.navigationSuite)
    implementation(libs.androidx.compose.material3.adaptive.navigation3)
    implementation(libs.androidx.navigation3.runtime)

    implementation(project(":core:audio:impl"))
    implementation(project(":core:coroutines"))
    implementation(project(":core:datastore:impl"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:navigation:impl"))
    implementation(project(":core:network:impl"))
    implementation(project(":core:ui"))

    implementation(project(":feature:alphabet:db"))
    implementation(project(":feature:alphabet:impl"))
    implementation(project(":feature:alphabet:ui"))
    implementation(project(":feature:onboarding:impl"))
    implementation(project(":feature:onboarding:ui"))
    implementation(project(":feature:preferences:impl"))

    implementation(libs.room.runtime)
    ksp(libs.room.compiler)

    testImplementation(libs.junit)
}
