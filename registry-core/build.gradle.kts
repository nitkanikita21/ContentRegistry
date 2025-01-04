plugins {
    `java-library`
    `maven-publish`
    kotlin("jvm")
}

group = "me.nitkanikita21"
version = "1.0"

dependencies {
    compileOnlyApi(libs.paper.api)
    api(libs.vavr)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
    useJUnitPlatform()
}

java {
    withSourcesJar()
}

configurations.all {
    // Check for updates every build
    resolutionStrategy.cacheDynamicVersionsFor(0, "seconds")
}

tasks.test {
    useJUnitPlatform()
}
repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(17)
}