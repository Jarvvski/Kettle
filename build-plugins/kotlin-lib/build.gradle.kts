plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

dependencies {
    implementation(gradleApi())
    implementation(libs.kotlin.gradle)
    implementation(libs.googleDevToolsKsp)
    implementation(libs.depAnalysis)
}
