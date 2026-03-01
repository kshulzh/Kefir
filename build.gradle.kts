allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
        maven("https://maven.google.com")
    }
}
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.0")
    }
}

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.jvm) apply false
}

group = extra["project.group"]!!
version = extra["project.version"]!!