plugins {
    id("root-kotlin-lib")
    application
}

dependencies {
    implementation("com.github.jarvvski.bus:core")
    implementation(libs.logger)
    implementation(libs.coroutines)

    runtimeOnly(libs.sl4j.simple)
}

application {
    mainClass = "com.github.jarvvski.bus.playground.AppKt"
}
