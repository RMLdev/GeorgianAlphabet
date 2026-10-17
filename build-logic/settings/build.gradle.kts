plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

gradlePlugin {
    plugins {
        register("modules") {
            id = "com.rmldev.plugin.includeSubdirsAsModules"
            implementationClass =
                "com.rmldev.georgianalphabet.plugin.settings.IncludeSubdirsAsModulesPlugin"
        }
    }
}