import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency

class FeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("example.jvm.library")
        pluginManager.apply("example.jvm.testing")

        val ownerPath = path

        configurations.configureEach {
            dependencies
                .withType(ProjectDependency::class.java)
                .all {
                    val dependencyPath = path
                    val forbidden =
                        dependencyPath.startsWith(":feature:") ||
                            dependencyPath == ":app"

                    if (forbidden) {
                        throw GradleException(
                            "$ownerPath must not depend on $dependencyPath"
                        )
                    }
                }
        }
    }
}
