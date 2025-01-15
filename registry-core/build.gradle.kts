plugins {
    `java-library`
    `maven-publish`
}

dependencies {
    compileOnlyApi(libs.guava)
    api(libs.jetbrains.annotations)
    api(libs.vavr)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}

configurations.all {
    // Check for updates every build
    resolutionStrategy.cacheDynamicVersionsFor(0, "seconds")
}