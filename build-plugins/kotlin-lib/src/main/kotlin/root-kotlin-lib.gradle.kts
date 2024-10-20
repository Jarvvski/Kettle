import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    base
    kotlin("jvm")

    id("com.google.devtools.ksp")
    id("com.autonomousapps.dependency-analysis")
}

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


    tasks.named<Test>("test") {
        useJUnitPlatform()
    }

    group = "com.github.jarvvski.bus"
    version = "0.0.1"
}
