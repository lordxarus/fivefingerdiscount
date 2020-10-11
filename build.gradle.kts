plugins {
    kotlin("jvm") version "1.3.72"
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("no.tornado:tornadofx:1.7.20")
    implementation(fileTree(mapOf("dir" to "lib", "include" to listOf("*.jar"))))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }


    task<Copy>("copyJar") {
            copy {
                from("build/libs/fivefingerdiscount-1.0-SNAPSHOT-all.jar")
                into("C:/Users/jeremy/DreamBot/Scripts")
            }
    }

    withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>().configureEach {
        finalizedBy(getTasksByName("copyJar", true))
    }
}