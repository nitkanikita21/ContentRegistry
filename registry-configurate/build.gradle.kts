plugins {
    `java-library`
    `maven-publish`
}

dependencies {
    api(project(":registry-core"))
    api(libs.sponge.configurate)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}
