# Kefir model-ir module

Concrete, IR-oriented implementations and utilities for the Kefir model-api. This module provides lightweight wrappers
around Kotlin IR (and select FIR/external bridges) exposing a stable, model-api-shaped surface that processors and
helpers can use without dealing directly with compiler internals.

What it provides (high level):

- IR-backed elements that implement model-api contracts:
    - Declarations — classes, functions, properties, constructors, files (KtIrClassElement, KtIrFunctionElement,
      KtIrPropertyElement, KtIrConstructorElement, KtIrFileElement, utils).
    - Types — class type and helpers (KtIrClassTypeElement, type utils).
    - Expressions — consts, blocks, if, get/set field/value, constructor call, bodies (KtIrConstElement,
      KtIrBlockElement, KtIrIfElement, KtIrGetValueElement, KtIrGetFieldElement, KtIrSetFieldElement,
      KtIrConstructorCallElement, Body/Block utils).
    - Statements — return and statement wrappers (KtIrReturnStatementElement, KtIrStatementElement, statement utils).
    - Annotations and Arguments — IR-backed annotation and argument nodes with small utilities.
- IO and root scopes — KtIrPackageElement, KtIrRootPackageElement for navigating packages/files; external/FIR roots for
  bridging when needed (e.g., KtExternalRootPackageElement, KtFirRootPackageElement).
- Utilities — small observable/lazy delegates used by the model (DelayObserverDelegate, DelayObserverListDelegate,
  DifMutableList/Set, ObserverDelegate, etc.).

Coordinates

- Group: io.github.kshulzh.kefir
- Artifact: model-ir
- Version: 0.0.1 (or your current project version)

Gradle (Kotlin DSL)

```kotlin
plugins { kotlin("jvm") }

dependencies {
    implementation("io.github.kshulzh.kefir:model-api:0.0.1")
    implementation("io.github.kshulzh.kefir:model-ir:0.0.1")
}
```

When should I use model-ir?

- You are writing a compiler plugin or helper that works with Kotlin IR but you want a stable, testable surface aligned
  with model-api.
- You build transformations in transform-ir and prefer model-api-shaped wrappers for declarations, types, expressions,
  statements, and annotations.
- You need simple package/file navigation (root, packages) backed by IR and optional FIR/external bridges.

Selected building blocks (as present in this module)

- Declarations: KtIrClassElement, KtIrFunctionElement, KtIrPropertyElement, KtIrConstructorElement, KtIrFileElement +
  declaration utils.
- Expressions: KtIrConstElement, KtIrBlockElement, KtIrBodyBlockElement, KtIrIfElement, KtIrGetValueElement,
  KtIrGetFieldElement, KtIrSetFieldElement, KtIrConstructorCallElement + expression utils.
- Statements: KtIrReturnStatementElement, KtIrStatementElement, KtIrStatementExpressionElement + statement utils.
- Types: KtIrClassTypeElement + type utils.
- Annotations & Args: KtIrAnnotationElement, KtIrArgumentElement + utils.
- IO & Roots: KtIrPackageElement, KtIrRootPackageElement; bridges: KtExternalRootPackageElement,
  KtFirRootPackageElement.
- Utils: DelayObserverDelegate, DelayObserverListDelegate, DifMutableList, DifMutableSet, ObserverDelegate.

Version compatibility

- Kotlin: JDK 17+, Kotlin compiler version aligned with this repository.
- Keep model-ir and model-api versions in sync across your projects.

Notes

- API surface may evolve with Kotlin compiler internals; prefer using model-api interfaces exposed here instead of
  direct IR/FIR types where possible.
- This module is focused on providing wrappers/adapters; for higher-level transform flow and utilities, see
  transform-ir.

See also

- model-api — core interfaces implemented by this module.
- transform-ir — helper APIs and contexts for building transformations.
- builder — DSL-style builders for declarations, types, and expressions that conform to model-api.
- model-utils — small cross-backend helpers that complement the model.
- ir-helper — focused, low-level IR helpers that can be used alongside model-ir.
