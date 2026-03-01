plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.vanniktech.maven.publish)
}

group = extra["project.group"]!!
version = extra["project.version"]!!

dependencies {
    compileOnly(kotlin("compiler-embeddable"))
    api(project(":kefir-tools")) {
        exclude(group = "org.jetbrains.kotlin")
    }

    implementation(project(":transform-ir")) {
        exclude(group = "org.jetbrains.kotlin")
    }
    implementation(project(":api"))
    implementation(project(":model-api"))
    implementation(project(":model"))
    implementation(project(":model-ir")) {
        exclude(group = "org.jetbrains.kotlin")
    }
    implementation(libs.kshku.problemgraph) {
        exclude(group = "org.jetbrains.kotlin")
    }
    testImplementation(kotlin("test"))
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}

kotlin {
    compilerOptions {
        javaParameters = true
        freeCompilerArgs.add("-Xdebug")
        freeCompilerArgs.add("-opt-in=org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi")
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}

mavenPublishing {
    publishToMavenCentral(false)
    coordinates(group.toString(), "compiler-plugin", version.toString())

    pom {
        name = "Kefir compiler plugin for code processing"
        description = "Module contains kotlin kotlin compiler plugin for code processing"
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