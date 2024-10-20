plugins {
    id("root-kotlin-lib")
    `java-library`
}

dependencies {
    api(libs.result4k)

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation(libs.junit.jupiter.engine)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
