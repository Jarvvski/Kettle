plugins {
    // none yet
}
rootProject.name = "build-plugins"

include("kotlin-lib")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}
