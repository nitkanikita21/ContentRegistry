plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.plugin.yml) apply false
    alias(libs.plugins.run.paper) apply false
    alias(libs.plugins.shadowJar) apply false
}

allprojects {
    group = "me.nitkanikita21"
    version = "1.2.0-SNAPSHOT"
}

val projectsToPublish = listOf(
    ":registry-core",
    ":registry-configurate",
    ":registry-cloud-framework"
).map { project(it) }

subprojects {

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.codemc.org/repository/maven-public/")
        maven("https://maven.enginehub.org/repo/")
    }

    if(!projectsToPublish.contains(this)) return@subprojects

    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    java {
        withSourcesJar()
    }

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                from(components["java"])

                groupId = project.group.toString()
                version = project.version.toString()
                artifactId = base.archivesName.get()

                pom {
                    name.set(base.archivesName.get())
                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("https://opensource.org/licenses/MIT")
                        }
                    }
                }

                versionMapping {
                    usage("java-api") {
                        fromResolutionOf("runtimeClasspath")
                    }
                    usage("java-runtime") {
                        fromResolutionResult()
                    }
                }
            }
        }

        repositories {
            mavenLocal()

            maven("https://repo.codemc.io/repository/nitkanikita21/") {
                val mavenUsername = System.getenv("JENKINS_USERNAME")
                val mavenPassword = System.getenv("JENKINS_PASSWORD")
                if(mavenUsername != null && mavenPassword != null) {
                    credentials {
                        username = mavenUsername
                        password = mavenPassword
                    }
                }
            }
        }
    }
}