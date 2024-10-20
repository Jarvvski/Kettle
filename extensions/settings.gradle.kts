pluginManagement {
    includeBuild("../build-plugins")
}

plugins {
    // none yet
}

rootProject.name = "extensions"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}
