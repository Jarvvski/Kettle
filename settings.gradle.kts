pluginManagement {
    includeBuild("build-plugins")
}

plugins {
    // none yet
}

rootProject.name = "event-bus"

includeBuild("local-kt-ext")
includeBuild("playground")

includeBuild("kettle-core")

