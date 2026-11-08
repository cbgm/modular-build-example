plugins {
    base
}

tasks.named("check") {
    dependsOn(
        ":core:model:check",
        ":feature:profile:check",
        ":feature:analytics:check",
        ":app:check",
        gradle.includedBuild("build-logic").task(":test"),
    )
}

tasks.register("qualityCheck") {
    group = "verification"
    description = "Checks the modules and tests the convention plugins."
    dependsOn(tasks.named("check"))
}
