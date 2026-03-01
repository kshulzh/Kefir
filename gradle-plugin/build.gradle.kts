plugins {
    alias(libs.plugins.kotlin.jvm)
    id("java-gradle-plugin")
    alias(libs.plugins.vanniktech.maven.publish)
}

group = extra["project.group"]!!
version = extra["project.version"]!!

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://maven.google.com")
    maven("https://plugins.gradle.org/m2/")
    google()
}

dependencies {
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin-api:${libs.versions.kotlin.get()}")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:${libs.versions.kotlin.get()}")
    compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable:${libs.versions.kotlin.get()}")
    compileOnly("com.android.tools.build:gradle:${libs.versions.agp.get()}")
    implementation(gradleApi())
    implementation(kotlin("gradle-plugin-api"))
}

gradlePlugin {
    plugins {
        create("Kefir") {
            id = "io.github.kshulzh.kefir"
            implementationClass = "io.github.kshulzh.kefir.gradle.plugin.KefirGradlePlugin"
        }
    }
}

tasks.register("sourcesJar", Jar::class) {
    group = "build"
    description = "Assembles Kotlin sources"

    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
    dependsOn(tasks.classes)
}

mavenPublishing {
    publishToMavenCentral(false)
    coordinates(group.toString(), "gradle-plugin", version.toString())

    pom {
        name = "Kefir gradle plugin for code processing"
        description = "Module contains gradle plugin"
        inceptionYear = "2026"
        url = "https://github.com/kshulzh/Kefir"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "kshulzh"
                name = "Kirill Shulzhenko"
                url = "https://github.com/kshulzh/"
                email = "kirill.shulzhenko2000@gmail.com"
                organization = "kshulzh"
                organizationUrl = "https://github.com/kshulzh"

            }
        }
        scm {
            url = "https://github.com/kshulzh/Kefir"
            connection = "scm:git:git://github.com/kshulzh/Kefir.git"
            developerConnection = "scm:git:ssh://git@github.com/kshulzh/Kefir.git"
        }
    }
}