import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
    application
}

group = "essaenko"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("io.ktor:ktor-server-netty:1.6.7")
    implementation("io.ktor:ktor-websockets:1.6.7")
    implementation("io.ktor:ktor-serialization:1.6.7")

    implementation("ch.qos.logback:logback-classic:1.2.10")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
}
repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}