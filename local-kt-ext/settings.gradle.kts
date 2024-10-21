pluginManagement {
    includeBuild("../build-plugins")
}

plugins {
    // none yet
}

rootProject.name = "local-kt-ext"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}
