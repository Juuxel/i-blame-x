plugins {
    java
    idea
    id("fabric-loom") version "0.2.5-SNAPSHOT"
}

base {
    archivesBaseName = "i-blame-x"
}

version = "1.0.1+1.14.4"

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
    minecraft("com.mojang:minecraft:1.14.4")
    mappings("net.fabricmc:yarn:1.14.4+build.12")

    // Fabric
    modImplementation("net.fabricmc:fabric-loader:0.6.1+build.165")
}
