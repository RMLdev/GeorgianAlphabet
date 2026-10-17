plugins {
    alias(libs.plugins.rmldev.kotlin.library)
    alias(libs.plugins.ksp)
}

dependencies {
    api(libs.room.runtime)

    // DAO-тесты на JVM: тестовая @Database из src/test и встроенный SQLite
    kspTest(libs.room.compiler)
    testImplementation(libs.sqlite.bundled)
}
