pluginManagement {
    includeBuild("../build-plugins")
}

plugins {
    // none yet
}

rootProject.name = "kettle-db"

includeBuild("../local-kt-ext")
includeBuild("../kettle-core")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}
