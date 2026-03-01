# Kefir — model-utils

model-utils hosts small, reusable helpers around the Kefir model (model-api). These utilities make it easier to traverse
package/class hierarchies, collect files/declarations, and perform common operations without depending on a specific
backend (FIR/IR).

Think of model-utils as convenience extensions on top of the model contracts: light, composable functions you can call
from processors, tests, and transformations.

## When should I use this module?

- You work with Kefir’s model and need handy traversal/collection helpers.
- You want backend-agnostic utilities that operate on model-api interfaces.
- You prefer tiny, dependency-light functions over boilerplate iteration code.

## Highlights

- Package/file traversal helpers for KtPackageScope:
    - `getPackages()` — collect current and nested package scopes.
    - `getFiles()` — collect files from current and nested packages.
- Class declaration flattener for KtClassElement:
    - `getDeclarations()` — collect all declarations, including from nested/inner classes.

## Relationship to other modules

- model-api — Contracts these helpers operate on (e.g., KtPackageScope, KtFileElement, KtClassElement).
- model — Common implementations and utilities that complement these helpers.
- model-ir / transform-ir — IR-facing implementations and transforms that can still benefit from model-level utilities.

See docs/modules.md for a high-level overview of all modules.

## Adding a dependency

Gradle (Kotlin DSL):

```kotlin
dependencies {
    implementation("io.github.kshulzh.kefir:model-utils:0.0.1")
    implementation("io.github.kshulzh.kefir:model-api:0.0.1")
}
```

Maven:

```xml

<dependency>
    <groupId>io.github.kshulzh.kefir</groupId>
    <artifactId>model-utils</artifactId>
    <version>0.0.1</version>
</dependency>
<dependency>
<groupId>io.github.kshulzh.kefir</groupId>
<artifactId>model-api</artifactId>
<version>0.0.1</version>
</dependency>
```

Note: Snapshot builds may require an additional repository. Check repository docs for the latest coordinates.

## Quick look: Traversal helpers

Package and file collection from a package hierarchy:

```kotlin
// Collect all package scopes including nested ones
val allPackages = rootPackage.getPackages()

// Collect all files from current and nested packages
val allFiles = rootPackage.getFiles()
```

Flatten declarations of a class including nested/inner classes:

```kotlin
val flatDecls = someClass.getDeclarations()
```

These helpers are small extension functions that operate on model-api interfaces, so they can be used with any backend
that implements those contracts.

## Source layout

- src/commonMain/kotlin/io/github/kshulzh/kefir/model/utils/** — Extension utilities
    - file.kt — `KtPackageScope.getPackages()`, `KtPackageScope.getFiles()`
    - declaration.kt — `KtClassElement.getDeclarations()`

## Versioning and compatibility

The module is evolving. Until 1.0, minor breaking changes are possible. Refer to releases and migration notes when
available.

## License

Apache License 2.0. See the repository LICENSE file for details.
