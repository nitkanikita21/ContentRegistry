plugins {
    `java-library`
    `maven-publish`
}

java {
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    api(project(":registry-core"))
    api(libs.sponge.configurate)

    compileOnly(libs.lombok)
    compileOnly(libs.bundles.cloud.framework)
    compileOnly(libs.paper)
    annotationProcessor(libs.lombok)
}
