plugins {
    `java-library`
    `maven-publish`
}

group = "me.nitkanikita21"
version = "1.0"

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.codemc.org/repository/maven-public/")
        maven("https://maven.enginehub.org/repo/")
    }

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                from(components["java"])

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
        }
    }
}