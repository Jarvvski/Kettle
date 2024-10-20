pluginManagement {
    includeBuild("build-plugins")
}

plugins {
    // none yet
}

rootProject.name = "event-bus"

includeBuild("extensions")
includeBuild("playground")

includeBuild("core")

