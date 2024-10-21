plugins {
    id("root-kotlin-lib")
    `java-library`
}

dependencies {
    api(libs.result4k)

    implementation("com.github.jarvvski.bus:kettle-core")
    implementation(libs.dbScheduler)
}
