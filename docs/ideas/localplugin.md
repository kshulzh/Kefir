# Local plugin layout (plan)

Goal

- Make it easy to develop a Kefir-based compiler plugin inside a regular project using dedicated source sets.
- Keep business code (app/library) separate from plugin code, but allow sharing utilities where needed.

Proposed source sets

- commonMain — your regular shared production code (KMP) or main (JVM only).
- commonTest — your regular tests.
- kefirCommonMain — shared utilities and model/builders used by the plugin and optionally by app/tests.
- kefirMain — the plugin implementation itself (IR/FIR transforms, Processor wiring).

Why this layout

- Clear separation: avoids leaking compiler APIs and keeps classpath clean for app code.
- Faster iteration: you can run the app/tests while evolving the plugin in the same build.
- Shared helpers: put common utilities (e.g., model/builders, small analyzers) into kefirCommonMain.

Gradle configuration (JVM project)

- In build.gradle.kts of the module that will host the local plugin:

```kotlin
plugins {
    kotlin("jvm")
}

kotlin {
    sourceSets {
        val main by getting
        val test by getting

        val kefirCommonMain by creating {
            // Put code shared between plugin and (optionally) app code
            // e.g. helper data structures, plain Kotlin utilities
            dependencies {
                implementation("io.github.kshulzh.kefir:model-api:0.0.1")
                implementation("io.github.kshulzh.kefir:builder:0.0.1")
            }
        }

        val kefirMain by creating {
            // Compiler plugin implementation lives here
            dependsOn(kefirCommonMain)
            dependencies {
                implementation("io.github.kshulzh.kefir:api:0.0.1")
                implementation("io.github.kshulzh.kefir:transform-ir:0.0.1")
                implementation("io.github.kshulzh.kefir:model-ir:0.0.1")
                // Kotlin compiler embeddings as required by your setup
            }
        }

        // Make the main source set NOT depend on plugin code
        main.dependsOn(kefirCommonMain)
    }
}
```

Gradle configuration (Kotlin Multiplatform)

- Add two custom source sets for JVM target only. Example sketch:

```kotlin
kotlin {
    jvm() // other targets as needed

    sourceSets {
        val commonMain by getting
        val commonTest by getting

        val jvmMain by getting

        val kefirCommonMain by creating {
            dependsOn(commonMain)
            // Shared helpers usable by plugin and jvmMain
        }

        val kefirMain by creating {
            dependsOn(kefirCommonMain)
            // Plugin implementation for JVM (K2)
            dependencies {
                implementation("io.github.kshulzh.kefir:api:0.0.1")
                implementation("io.github.kshulzh.kefir:transform-ir:0.0.1")
                implementation("io.github.kshulzh.kefir:model-ir:0.0.1")
            }
        }

        jvmMain.dependsOn(kefirCommonMain)
    }
}
```

Wiring the local compiler plugin

- Option A: Use Kefir gradle-plugin (recommended for published plugins). Point it to the plugin artifact.
- Option B: Local registrar for tests/dev via kotlin-compile-testing (see test/README.MD) — quickest iteration.
- Option C: Composite build (includedBuild) where a separate plugin module produces the compiler plugin JAR used by the
  app module.

Option B (quick dev loop with test module)

- In your tests, use KefirCompilation from the test module and pass a Processor lambda. This avoids packaging a real
  plugin while you prototype transforms.
- See test/README.MD for detailed examples.

Option C (composite build)

- Create a separate :my-plugin module with plugin code (contents of kefirMain may be moved there later).
- In settings.gradle.kts of the consumer build:

```kotlin
includeBuild("../my-plugin")
```

- Then add the dependency to the consumer’s build using the gradle-plugin or direct compiler plugin configuration.

Folder layout example (JVM)

- src/main/kotlin — application/library code
- src/test/kotlin — tests
- src/kefirCommonMain/kotlin — shared helpers used by the plugin and possibly tests
- src/kefirMain/kotlin — compiler plugin code (KtProcessor implementations, transforms)

What goes where

- kefirCommonMain:
    - Pure Kotlin helpers, model/builders usage, configuration data classes.
    - No direct references to org.jetbrains.kotlin.* compiler internals if you want to reuse in app code.
- kefirMain:
    - KtProcessor implementations, IR/FIR transforms, wiring (registrars if needed).
    - May depend on compiler internals via transform-ir/model-ir.

IDE and builds

- IntelliJ IDEA recognizes custom source sets. If code highlighting fails, re-import Gradle.
- Keep Kotlin and Kefir versions aligned (see README.md and module READMEs).
- JDK 17+ recommended.

Migration notes

- Start by placing experimental plugin code in kefirMain while keeping reusable utilities in kefirCommonMain.
- When the plugin stabilizes, consider extracting a dedicated module (compiler-plugin style) and depend on it from the
  app via the gradle-plugin or compiler arguments.

References

- README.md — repository overview
- api/README.md — core processor interfaces (KtProcessor)
- transform-ir/README.MD — contexts and utilities to build transforms
- model-ir/README.MD — IR-backed model implementations
- test/README.MD — fast iteration with kotlin-compile-testing