# Kefir — model-api

The model-api module defines the public, platform-agnostic Kotlin interfaces that describe Kefir’s code model:
declarations, types, expressions, annotations, arguments, names, and paths. These interfaces are consumed by processors
and utilities across the repository (model, model-ir, builder, transform-ir, etc.).

Think of model-api as the vocabulary of the Kefir model. Other modules provide concrete implementations (e.g., model,
model-ir) or helpers (builder, model-utils) that operate on these contracts.

## When should I use this module?

- You are writing tools or processors that should not depend on a specific backend (FIR/IR) implementation.
- You need a stable set of interfaces to traverse or describe Kotlin program elements.
- You build libraries that integrate with Kefir and want to exchange data via common contracts.

## Key concepts

- KtElement — Root interface for all model nodes.
- KtName — Strongly-typed name abstraction instead of raw strings.
- KtPath — Qualified paths for packages and types.
- Annotations API — KtAnnotationElement, KtAnnotationsScope.
- Arguments/Parameters API — KtArgumentElement, KtArgumentsScope, KtParameterElement, KtParametersScope.
- Types — KtTypeElement and related contracts.
- Expressions — KtExpressionElement and basic expression nodes.

These are minimal, dependency-light interfaces intended to be implemented by backends and utilities.

## Relationship to other modules

- model — Provides common implementations of many API contracts.
- model-ir — IR-oriented model and utilities, implementing parts of model-api for IR.
- builder — DSL to construct model elements that conform to model-api.
- transform-ir — Transformations that operate on IR using model-api shaped abstractions.

See docs/modules.md for a high-level repository overview.

## Adding a dependency

Gradle (Kotlin DSL):

```kotlin
dependencies {
    implementation("io.github.kshulzh.kefir:model-api:0.0.1")
}
```

Maven:

```xml

<dependency>
    <groupId>io.github.kshulzh.kefir</groupId>
    <artifactId>model-api</artifactId>
    <version>0.0.1</version>
</dependency>
```

Note: Snapshots may require an additional repository; see docs/install.md in this repo for up-to-date coordinates and
repositories.

These snippets demonstrate how implementations supply storage and behavior while consumers rely only on the interfaces.

## Source layout

- src/commonMain/kotlin/io/github/kshulzh/kefir/model/api/** — Core API packages:
    - annotation — Annotation model contracts.
    - arg — Arguments and parameters.
    - expression — Expression contracts.
    - type — Type contracts.
    - plus shared primitives like KtElement, KtName, KtPath, KtExternalElement.

## Versioning and compatibility

The API is evolving. Minor breaking changes may occur until 1.0. For stability guarantees and migration notes, check the
repository release notes and CHANGELOG (when available).

## License

Apache License 2.0. See the repository LICENSE file for details.
