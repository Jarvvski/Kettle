pluginManagement {
    includeBuild("../build-plugins")
}

plugins {
    // none yet
}

rootProject.name = "playground"

includeBuild("../core")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}
