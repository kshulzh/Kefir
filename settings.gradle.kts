plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "kefir"

include(
    "compiler-plugin",
    "gradle-plugin",
    "kefir-tools",
)

include(
    "api",
    "builder",
    "model",
    "model-api",
    "model-ir",
    "model-utils",
    "test",
    "ir-helper",
    "transform-ir",
)

