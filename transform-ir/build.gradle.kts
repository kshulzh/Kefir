plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.vanniktech.maven.publish)
}

group = extra["project.group"]!!
version = extra["project.version"]!!

dependencies {
    implementation(project(":model-api"))
    implementation(project(":ir-helper"))
    implementation(libs.kshku.problemgraph)

    implementation(kotlin("compiler-embeddable"))
    testImplementation(kotlin("test"))
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}

tasks.jar {
    archiveBaseName.set("kefir-transform-ir")
}

mavenPublishing {
    publishToMavenCentral(false)
    coordinates(group.toString(), "transform-ir", version.toString())

    pom {
        name = "Kefir Transform IR"
        description = "Module contains functionality for transforming Module Api classes into IR/FIR"
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
