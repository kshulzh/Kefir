# Kefir — model

The model module provides common, platform-agnostic implementations and utilities for the Kefir model defined in
model-api. It contains concrete element implementations (e.g., types) and helper functions that are reused by
higher-level modules and backends.

Think of model as the default implementation layer: model-api declares the contracts, while model supplies practical,
lightweight implementations and utilities used in tests, builders, and IR transforms.

## When should I use this module?

- You need default implementations for model-api contracts without committing to a specific backend.
- You want utilities to parse or construct common model elements (e.g., simple type parsing).
- You are writing processors or tests that can operate on the common model.

## Highlights

- Implementations for core model concepts (e.g., class type elements).
- Utilities in `utils` for working with the model, including a tiny type parser.
- Designed to be small and dependency-light.

## Relationship to other modules

- model-api — Contracts the model implements. You generally depend on this alongside model.
- model-ir — IR-oriented implementations/utilities; may interoperate with or adapt from model.
- builder — DSL for constructing elements that plug into model/model-api.
- model-utils — Shared helpers used across model layers.
- transform-ir — Transformations that can consume model-shaped elements.

See docs/modules.md for a full overview.

## Adding a dependency

Gradle (Kotlin DSL):

```kotlin
dependencies {
    implementation("io.github.kshulzh.kefir:model:0.0.1")
    implementation("io.github.kshulzh.kefir:model-api:0.0.1")
}
```

Maven:

```xml

<dependency>
    <groupId>io.github.kshulzh.kefir</groupId>
    <artifactId>model</artifactId>
    <version>0.0.1</version>
</dependency>
<dependency>
<groupId>io.github.kshulzh.kefir</groupId>
<artifactId>model-api</artifactId>
<version>0.0.1</version>
</dependency>
```

Note: Snapshot builds may require additional repositories. Check repository docs for the latest coordinates.

## Quick look: Types and utilities

The module includes a simple type parser that turns a string like a qualified class name with type arguments into a
model type element.

Example:

```kotlin
val t1 = typeOf("kotlin:String?")      // nullable String
val t2 = typeOf("kotlin:List<kotlin:Int>")
val t3 = typeOf("my.company.pkg:Foo<bar:Baz?>?")
```

The produced values implement model-api type contracts and can be consumed uniformly by other modules.

## Source layout

- src/commonMain/kotlin/io/github/kshulzh/kefir/model/** — Core implementations and utilities
    - type — Implementations like KtClassTypeElementImpl
    - utils — Helpers such as typeOf and observer delegates

## Versioning and compatibility

The module is evolving. Until 1.0, minor breaking changes are possible. Refer to releases and migration notes when
available.

## License

Apache License 2.0. See the repository LICENSE file for details.
