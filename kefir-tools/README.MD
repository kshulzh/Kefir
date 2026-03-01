# Kefir — kefir-tools

kefir-tools contains small utilities that make it easier to build Kotlin compiler processors on top of Kefir. It focuses
on conveniences for wiring your processors into the Kotlin compiler, especially the IR phase, without pulling in the
whole compiler infrastructure yourself.

Think of it as lightweight scaffolding for plugin authors: you extend a base registrar and register your processing
entry points (functions or processors) in a few lines.

## When should I use this module?

- You are writing a compiler plugin or processor that runs with Kefir and you want a minimal way to register your logic
  into the compiler.
- You target Kotlin K2 and need a simple `CompilerPluginRegistrar` base with sensible defaults.
- You prefer to register a plain function or a small processor, not deal with low‑level compiler hooks directly.

## Highlights

- KefirComponentRegistrar — an abstract base that:
    - sets `supportsK2 = true` to work with the K2 compiler;
    - exposes `registerIrExtension` overloads to register either:
        - a `KtProcessor` implementation, or
        - a plain `Processor` (wrapped internally into a `KtProcessor`).
- A simple `irExtensions` list holds your registered processors and can be consumed by your compiler integration.
- Designed to be used as `compileOnly` in processor projects.

## Relationship to other modules

- api — Common Kefir processor interfaces (`Processor`, `KtProcessor`, etc.).
- model-api / model — Model contracts and basic implementations often used by processors.
- transform-ir — Utilities for building IR transformations (optional, if you operate on IR).
- compiler-plugin — The module that wires everything into the Kotlin compiler; it depends on kefir-tools.

See docs/modules.md for a high-level overview and docs/install.md for end‑to‑end setup.

## Adding a dependency

Gradle (Kotlin DSL):

```kotlin
dependencies {
    // Typically used as compileOnly in your processor module
    compileOnly("io.github.kshulzh.kefir:kefir-tools:0.0.1")
}
```

Maven:

```xml

<dependency>
    <groupId>io.github.kshulzh.kefir</groupId>
    <artifactId>kefir-tools</artifactId>
    <version>0.0.1</version>
    <scope>provided</scope>
</dependency>
```

Note: Snapshot builds may require an additional repository. Check repository docs for the latest coordinates.

## Quick look: Registering your processor

Create your registrar and register either a function entry point or a processor implementation.

```kotlin
class YourKefirComponentRegistrar : KefirComponentRegistrar() {
    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        // Option A: register a top-level function that serves as the entry point
        registerIrExtension(KtPackageScope::main)

        // Option B: or register a Processor/KtProcessor implementation
        registerIrExtension(MyProcessor())
    }
}
```

A minimal processor/function could look like this:

```kotlin
// Function-style entry point
fun KtPackageScope.main(ktContext: KtContext) {
    Package("com.example") {
        File("Hello.kt") {
            Class("Hello") { Fun("greet") { body = Const("Hello, Kefir!") } }
        }
    }
}

// Processor-style entry point
class MyProcessor : Processor {
    override fun KtPackageScope.process(ktContext: KtContext) {
        Package("sample") { File("C.kt") { Class("C") } }
    }
}
```

Service registration so the compiler can discover your registrar:

```
resources/META-INF/services/io.github.kshulzh.kefir.compiler.plugin.KefirComponentRegistrar
example.YourKefirComponentRegistrar
```

For a complete, step‑by‑step guide, see docs/install.md.

## Source layout

- src/main/java/io/github/kshulzh/kefir/compiler/plugin/** — Registrar utilities
    - KefirComponentRegistrar.kt — Base registrar with `supportsK2 = true` and `registerIrExtension` helpers

## Versioning and compatibility

The module is evolving. Until 1.0, minor breaking changes are possible. Refer to releases and migration notes when
available.

## License

Apache License 2.0. See the repository LICENSE file for details.
