plugins {
    id("root-kotlin-lib")
    `java-library`
}

dependencies {
    api(libs.result4k)

    implementation(libs.logger)
    implementation(kotlin("reflect"))
    implementation(libs.coroutines)
    implementation(libs.guava)
    implementation("com.github.jarvvski.bus:local-kt-ext")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation(libs.junit.jupiter.engine)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
