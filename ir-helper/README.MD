# Kefir — ir-helper

The ir-helper module contains small utilities and thin wrappers that make it easier to work with Kotlin IR (Intermediate
Representation) inside compiler plugins and IR-based transforms. It focuses on representing model elements that are
initialized via IR lambdas, helping you defer IR construction until the plugin context and module are available.

Think of ir-helper as glue between high-level model-shaped elements and the Kotlin IR backend: it provides simple,
lightweight building blocks you can compose in your IR transformations.

## When should I use this module?

- You are implementing IR transformations and want to lazily construct IR nodes once IrPluginContext/IrModuleFragment
  are available.
- You need small helper wrappers to represent declarations, statements, or expressions that are backed by IR
  initializers.
- You want to keep your processor code backend-agnostic at the model level, but still provide IR implementations when
  targeting the compiler IR.

## Highlights

- IrInitElement<T> — a tiny protocol for elements that carry an initializer: `(IrModuleFragment, IrPluginContext) -> T`.
- Ready-to-use wrappers:
    - KtIrInitDeclarationElement — model declaration backed by an IR initializer.
    - KtIrInitStatementElement — model statement backed by an IR initializer.
    - KtIrInitExpressionElement — model expression backed by an IR initializer.
- Minimal by design: no heavy abstractions, just small helpers you can plug into your pipeline.

## Relationship to other modules

- model-api — Contracts these wrappers implement (e.g., declarations, statements, expressions).
- model-ir — Broader IR-oriented model and utilities; can be used alongside ir-helper.
- transform-ir — Transformations that operate on IR and may leverage these helpers.
- builder / model — Higher-level builders and common implementations that can interoperate with IR-backed elements.

See docs/modules.md for a high-level overview.

## Adding a dependency

Gradle (Kotlin DSL):

```kotlin
dependencies {
    implementation("io.github.kshulzh.kefir:ir-helper:0.0.1")
}
```

Maven:

```xml

<dependency>
    <groupId>io.github.kshulzh.kefir</groupId>
    <artifactId>ir-helper</artifactId>
    <version>0.0.1T</version>
</dependency>
```

Note: Snapshot builds may require an additional repository. Check repository docs for the latest coordinates.

## Quick look: IR-backed elements

The helpers let you describe model elements that build their IR form only when needed (with access to IrPluginContext
and IrModuleFragment).

```kotlin
// Expression backed by an IR initializer
val expr = KtIrInitExpressionElement(
    initializer = { module, context ->
        // Build and return an IrExpression using context and module
        // e.g., create a constant or call into symbol builders
        TODO("return IrExpression")
    }
)

// Statement backed by an IR initializer
val stmt = KtIrInitStatementElement(
    initializer = { module, context ->
        // Produce an IrStatement (e.g., a block or return)
        TODO("return IrStatement")
    }
)

// Declaration backed by an IR initializer
val decl = KtIrInitDeclarationElement(
    initializer = { module, context ->
        // Create and return an IrDeclaration (e.g., function/class/property)
        TODO("return IrDeclaration")
    }
)
```

These wrappers implement model-api contracts so they can fit into model-shaped pipelines, while still producing concrete
IR when executed.

## Source layout

- src/main/kotlin/io/github/kshulzh/kefir/ir/helper/** — Helpers and wrappers
    - IrInitElement.kt — Common protocol for IR-initialized elements
    - KtIrInitExpressionElement.kt — Expression wrapper
    - KtIrInitStatementElement.kt — Statement wrapper
    - KtIrInitDeclarationElement.kt — Declaration wrapper

## Versioning and compatibility

The module is evolving. Until 1.0, minor breaking changes are possible. Refer to releases and migration notes when
available.

## License

Apache License 2.0. See the repository LICENSE file for details.
