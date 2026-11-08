import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType

class JvmTestingConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("example.jvm.library")

        val libs = extensions
            .getByType<VersionCatalogsExtension>()
            .named("libs")

        dependencies.add(
            "testImplementation",
            libs.findLibrary("kotlin-test-junit").orElseThrow()
        )

        tasks.withType<Test>().configureEach {
            useJUnit()
            maxParallelForks = 1
            testLogging {
                events("failed", "skipped")
            }
        }
    }
}
