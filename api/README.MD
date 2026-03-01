# Kefir API module

The Kefir API module contains the minimal public interfaces used to write Kefir processors. It is intentionally small
and stable so your processors can depend on it without pulling in compiler backends.

What it provides (high level):

- KtProcessor — the main interface your processor implements.
- KtContext — runtime context with access to options and environment.
- Registration hooks used by the compiler plugin to discover and execute your processor.

Coordinates

- Group: io.github.kshulzh.kefir
- Artifact: api
- Version: 0.0.1 (or your current project version)

Gradle (processor module)

```kotlin
plugins { kotlin("jvm") }

dependencies {
    implementation("io.github.kshulzh.kefir:api:0.0.1")
}
```

Quick start

1) Implement a processor

```kotlin
class KtProcessorImpl : KtProcessor {
    override fun KtPackageScope.process(ktContext: KtContext) {
        // Build or transform model elements here
        // Example: create a simple package/file/class
        Package("sample") {
            File("Hello.kt") {
                Class("Hello")
            }
        }
    }
}
```

2) Register via a KefirComponentRegistrar in your processor project

```kotlin
class YourKefirComponentRegistrar : KefirComponentRegistrar() {
    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        registerIrExtension(KtProcessorImpl())
    }
}
```

3) Service registration
   Create the file at:

- resources/META-INF/services/io.github.kshulzh.kefir.compiler.plugin.KefirComponentRegistrar
  with content:

```
example.YourKefirComponentRegistrar
```

Version compatibility

- Kotlin: JDK 17+, Kotlin compiler version aligned with this repository.
- Keep api dependency version in sync with the rest of Kefir modules used by your project.

See also

- docs/install.md — end-to-end setup and examples
- docs/modules.md — overview of all modules
