import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    application
}

group = "org.destinationsol"
version = "1.0-SNAPSHOT"

// Versions
val gdxVersion = "1.10.0"

repositories {
    mavenCentral()
}

dependencies {
    // All the libGDX stuff, for rendering the actual mesh editor window
    implementation("com.badlogicgames.gdx:gdx:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-box2d:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-box2d-platform:$gdxVersion:natives-desktop")
    implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "16"
}

application {
    mainClass.set("MainKt")
}