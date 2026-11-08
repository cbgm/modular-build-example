plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.kotlin.gradle.plugin)
    testImplementation(gradleTestKit())
    testImplementation(libs.junit)
}

gradlePlugin {
    plugins {
        register("jvmLibrary") {
            id = "example.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
        register("jvmTesting") {
            id = "example.jvm.testing"
            implementationClass = "JvmTestingConventionPlugin"
        }
        register("feature") {
            id = "example.feature"
            implementationClass = "FeatureConventionPlugin"
        }
    }
}

tasks.test {
    useJUnit()
}
