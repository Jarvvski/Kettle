import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    base
    kotlin("jvm")

    id("com.google.devtools.ksp")
    id("com.autonomousapps.dependency-analysis")
}

internal val Project.libs: VersionCatalog get() =
    project.extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencyAnalysis {
    issues {
        all {
            onAny {
                severity("fail")
            }
            onIncorrectConfiguration {
                severity("ignore")
            }
            onUsedTransitiveDependencies {
                severity("ignore")
            }
            onUnusedDependencies {
                exclude("org.jetbrains.kotlin:kotlin-stdlib")
            }
        }
    }
}

allprojects {
    group = "com.github.jarvvski.gradle"
    version = "0.1"

    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    tasks {
        withType<KotlinCompile>().all {
            compilerOptions {
                freeCompilerArgs.addAll(
                    "-opt-in=kotlin.RequiresOptIn",
                    "-Xcontext-receivers"
                )
                jvmTarget.set(JvmTarget.JVM_21)
            }
        }
    }

    dependencies {
        implementation("com.github.jarvvski.bus:local-kt-ext")

        implementation(libs.findLibrary("logger").get())
        implementation(libs.findLibrary("coroutines").get())
        implementation(libs.findLibrary("guava").get())

        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
        testImplementation(libs.findLibrary("junit.jupiter.engine").get())
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }


    tasks.named<Test>("test") {
        useJUnitPlatform()
    }

    group = "com.github.jarvvski.bus"
    version = "0.0.1"
}
