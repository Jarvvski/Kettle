plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.kotlin.gradle)
    implementation(libs.googleDevToolsKsp)
    implementation(libs.depAnalysis)
}
