# Kefir test module

Utilities for writing fast, self‑contained tests around Kefir processors and generated code. It wraps
kotlin-compile-testing and ships small reflection helpers so you can:

- Compose Kotlin/Java sources in-memory
- Run them through a Kefir compiler plugin (K2)
- Assert results using a compact DSL and reflection shortcuts

What it provides (high level):

- KefirCompilation — add Kotlin/Java sources and compile them with an embedded KefirTestComponentRegistrar.
- KefirResults — exposes the underlying JvmCompilationResult and an asserts { } infix for readable checks.
- Reflection helpers — Klass, Nested, construct, invoke, get, set and NamedParam for easy interaction with compiled
  classes (including companion objects and nullable returns).

Coordinates

- Group: io.github.kshulzh.kefir
- Artifact: test
- Version: 0.0.1 (or your current project version)

Gradle (use in your processor project tests)

```kotlin
plugins { kotlin("jvm") }

dependencies {
    testImplementation("io.github.kshulzh.kefir:test:0.0.1")
    // kotlin-compile-testing is brought transitively in this module's build; 
    // add it explicitly only if your setup requires a specific version.
}
```

Quick start

```kotlin
KefirCompilation {
    kotlin(
        "Foo.kt",
        """
        class Foo(val p: String) {
            fun greet(name: String) = "Hello, " + name + ", from " + p
            class Bar
            companion object {
                var x: Int = 0
                fun singleton() = "SINGLETON"
            }
        }
        """
    )
} process { /* optional Processor lambda; can be empty */ } asserts {
    // result.exitCode contains the compilation status
    val foo = Klass("Foo")
    val inst = foo.construct("planet")!!
    val greet = inst.invoke("greet", "World")

    // nested class
    val bar = foo.Nested("Bar")

    // companion get/set and call
    foo.set("x", 42)
    val x = foo.get("x")
    val single = foo.invoke("singleton")
}
```

Reflection helpers cheat‑sheet

- Klass(name: String): KClass<*> — load a class from the compiled output: `val k = results.Klass("com.example.Foo")` or
  simply `Klass("Foo")` for default package.
- KClass.Nested(name): KClass<*> — get a nested class by its simple name.
- KClass.construct(vararg args): Any? — call a matching constructor. Supports:
    - positional args: `Foo::class.construct(1, "name")`
    - named args: `Foo::class.construct(NamedParam("id", 1), NamedParam("name", "Bob"))`
    - nullability is respected.
- invoke(name, vararg args): Any? — call a member function on an instance; if receiver is a KClass, calls on its
  companion object.
- get(name): Any? — read a property; works with instances and with KClass companion.
- set(name, value): Unit — write a mutable property; works with instances and with KClass companion.

Providing a processor (optional)
KefirCompilation wires KefirTestComponentRegistrar with a K2-compatible KefirIrExtension. You can pass your Processor
lambda to process { } to run custom FIR/IR transformations during tests:

```kotlin
KefirCompilation {
    kotlin("A.kt", "class A")
} process {
    // this: KtProcessor. Implement your visit hooks here if needed
    // e.g., transform classes, functions, etc.
} asserts {
    // assert on compiled classes or side effects
}
```

Version compatibility

- JDK: 17+
- Kotlin: aligned with this repository’s compiler version
- K2: supported

Notes

- This module is intended for tests only (testImplementation scope).
- When running in CI, ensure the test JVM has permissions to load compiled classes from kotlin-compile-testing’s
  classloader.

See also

- api/ — core processor interfaces
- transform-ir/ — helper layer for FIR/IR access and utilities
- builder/ — DSL-style builders for declarations, types, and expressions
