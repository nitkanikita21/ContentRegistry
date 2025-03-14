import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

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
    implementation(project(":registry-cloud-framework"))
    library("org.spongepowered:configurate-hocon:4.1.2")
    library(libs.bundles.cloud.framework)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
}

java {
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.runServer {
    minecraftVersion("1.21.1")

    downloadPlugins {
        modrinth("ViaVersion", "5.0.3")
        modrinth("ViaBackwards", "5.0.3")
    }
}

paper {
    main = "me.nitkanikita21.example.TestPlugin"
    loader = "me.nitkanikita21.example.TestPluginLoader"
    generateLibrariesJson = true
    load = BukkitPluginDescription.PluginLoadOrder.POSTWORLD

    apiVersion = "1.21"
}