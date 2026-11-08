import org.gradle.testkit.runner.GradleRunner
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class FeatureConventionPluginTest {
    @get:Rule
    val temporaryFolder = TemporaryFolder()

    @Test
    fun `allows a dependency on core`() {
        val result = runner(":core:model").build()
        assertTrue(result.output.contains("BUILD SUCCESSFUL"))
    }

    @Test
    fun `rejects a dependency on another feature`() {
        val result = runner(":feature:analytics").buildAndFail()
        assertTrue(
            result.output.contains(
                ":feature:profile must not depend on :feature:analytics"
            )
        )
    }

    private fun runner(dependencyPath: String): GradleRunner {
        val projectDir = temporaryFolder.newFolder()
        fun write(path: String, contents: String) {
            File(projectDir, path).apply {
                parentFile.mkdirs()
                writeText(contents.trimIndent())
            }
        }

        write("settings.gradle.kts", """
            plugins {
                id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
            }
            dependencyResolutionManagement {
                repositories { mavenCentral() }
            }
            rootProject.name = "convention-test"
            include(":core:model", ":feature:profile", ":feature:analytics")
        """)
        write("gradle/libs.versions.toml", """
            [libraries]
            kotlin-test-junit = { module = "org.jetbrains.kotlin:kotlin-test-junit", version = "2.4.21" }
        """)
        write("core/model/build.gradle.kts", """
            plugins { `java-library` }
        """)
        write("feature/analytics/build.gradle.kts", """
            plugins { `java-library` }
        """)
        write("feature/profile/build.gradle.kts", """
            plugins { id("example.feature") }
            dependencies {
                implementation(project("$dependencyPath"))
            }
        """)
        return GradleRunner.create()
            .withProjectDir(projectDir)
            .withPluginClasspath()
            .withArguments(":feature:profile:check", "--stacktrace")
    }
}
