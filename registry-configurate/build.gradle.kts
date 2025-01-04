plugins {
    `java-library`
    `maven-publish`
}

group = "me.nitkanikita21"
version = "1.0"

dependencies {
    api(project(":registry-core"))
    api(libs.sponge.configurate)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}

tasks.test {
    useJUnitPlatform()
}