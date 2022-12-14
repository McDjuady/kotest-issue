import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform") version "1.7.20"
    id("io.kotest.multiplatform") version "5.5.4"
}

group = "com.mcdjuady"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    targets {
        js(IR) { // LEGACY or BOTH are unsupported
            browser() // to compile for the web
            nodejs {
                testTask {
                    environment("something","something")
                    useMocha()
                }
                binaries.library()
            }
            useCommonJs()
        }
        jvm()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.kotest:kotest-framework-api:5.5.4")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(npm("dockerode","3.3.4"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation("io.kotest:kotest-framework-engine:5.5.4")
                implementation("io.kotest:kotest-assertions-core:5.5.4")
            }
        }
    }
}

tasks.wrapper {
    gradleVersion = "7.5"
    distributionType = Wrapper.DistributionType.ALL
}