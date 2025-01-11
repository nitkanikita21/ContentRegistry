plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.plugin.yml)
    alias(libs.plugins.run.paper)
    alias(libs.plugins.shadowJar)
}

group = "me.nitkanikita21"
version = "1.0"

dependencies {
    implementation(project(":registry-core"))
    implementation(project(":registry-configurate"))
    library("org.spongepowered:configurate-hocon:4.1.2")

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
}

tasks.test {
    useJUnitPlatform()
}

tasks.runServer {
    minecraftVersion("1.20.4")

    downloadPlugins {
        modrinth("ViaVersion", "5.0.3")
        modrinth("ViaBackwards", "5.0.3")
        modrinth("ServerUtils", "3.5.4")
        hangar("TabTPS", "1.3.21")
    }
}

paper {
    main = "me.nitkanikita21.example.TestPlugin"
    loader = "me.nitkanikita21.example.TestPluginLoader"
    generateLibrariesJson = true

    apiVersion = "1.20"
}