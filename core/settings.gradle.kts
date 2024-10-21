pluginManagement {
    includeBuild("../build-plugins")
}

plugins {
    // none yet
}

rootProject.name = "core"

includeBuild("../local-kt-ext")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}
