plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.vanniktech.maven.publish)
}

group = extra["project.group"]!!
version = extra["project.version"]!!

kotlin {
    //jvm
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("reflect"))
                implementation(project(":model-api"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

tasks.named("jvmJar", Jar::class) {
    archiveBaseName.set("kefir-model")
}

mavenPublishing {
    publishToMavenCentral(false)
    coordinates(group.toString(), "model", version.toString())

    pom {
        name = "Kefir model"
        description = "Module contains implementations of Module API"
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