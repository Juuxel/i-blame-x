plugins {
    java
    idea
    id("fabric-loom") version "0.2.1-SNAPSHOT"
}

base {
    archivesBaseName = "i-blame-x"
}

version = "1.0.0+1.14"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.getByName<ProcessResources>("processResources") {
    inputs.property("version", project.version)
    filesMatching("fabric.mod.json") {
        expand(
            mutableMapOf(
                "version" to project.version
            )
        )
    }
}

minecraft {
}

dependencies {
    minecraft("com.mojang:minecraft:1.14")
    mappings("net.fabricmc:yarn:1.14+build.1")

    // Fabric
    modCompile("net.fabricmc:fabric-loader:0.4.6+build.141")
}
