plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.vanniktech.maven.publish)
}

group = extra["project.group"]!!
version = extra["project.version"]!!

dependencies {
    implementation(kotlin("compiler-embeddable"))
    implementation(kotlin("reflect"))

    implementation(project(":api"))
    implementation(project(":builder"))
    implementation(project(":transform-ir"))
    implementation(project(":model-api"))
    implementation(project(":model"))
    implementation(project(":model-ir"))
    implementation(project(":compiler-plugin"))

    implementation("dev.zacsweers.kctfork:core:0.12.1")
    testImplementation(project(":model-utils"))
    implementation(kotlin("test"))
}

kotlin {
    compilerOptions {
        javaParameters = true
        freeCompilerArgs.add("-Xdebug")
        freeCompilerArgs.add("-opt-in=org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi")
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}

tasks.jar {
    archiveBaseName.set("kefir-test")
}

mavenPublishing {
    publishToMavenCentral(false)
    coordinates(group.toString(), "test", version.toString())

    pom {
        name = "Kefir Test"
        description = "Module contains test utilities and tests"
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