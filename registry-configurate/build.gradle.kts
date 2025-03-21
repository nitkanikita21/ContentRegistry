plugins {
    `java-library`
    `maven-publish`
}

java {
    withSourcesJar()
}

dependencies {
    api(project(":registry-core"))
    api(libs.sponge.configurate)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}
