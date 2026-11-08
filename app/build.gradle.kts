plugins {
    id("example.jvm.library")
}

dependencies {
    implementation(project(":feature:profile"))
    implementation(project(":feature:analytics"))
}
