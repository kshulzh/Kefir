# Kefir transform-ir module

The transform-ir module provides helper APIs and utilities for building Kotlin FIR/IR-based transformations in Kefir
processors. It contains thin wrappers, contextual helpers, and small utilities so you can focus on expressing
transformations instead of wiring compiler internals.

What it provides (high level):

- Transform contexts (e.g., KtTransformContextImpl, local contexts) to access symbols, files, and utility builders
  during a pass.
- Utility functions for names, modifiers, and IR construction shortcuts.
- To FIR/IR transformers.

Coordinates

- Group: io.github.kshulzh.kefir
- Artifact: transform-ir
- Version: 0.0.1 (or your current project version)

Gradle (processor module)

```kotlin
plugins { kotlin("jvm") }

dependencies {
    implementation("io.github.kshulzh.kefir:api:0.0.1")
    implementation("io.github.kshulzh.kefir:transform-ir:0.0.1")
}
```

Selected building blocks (as present in this module)

- Fir/IR structures: KtFirStructure, KtFirStructureImpl, KtFirStructureFir, KtIrStructure helpers.
- Contexts: KtTransformContextImpl and local transform context variants.
- Utils: name.kt, modifier.kt, ir.kt, utils.kt — shortcuts to construct names, flags, and small IR nodes.
- Wrappers: FirWrapper and related helpers to traverse/inspect FIR with a stable surface.

Version compatibility

- Kotlin: JDK 17+, Kotlin compiler version aligned with this repository.
- Keep transform-ir and api versions in sync across your processor projects.

Notes

- API surface may evolve with Kotlin compiler internals; prefer depending on higher-level helpers exported here instead
  of direct FIR/IR types where possible.

See also

- api/README.md — core processor interfaces
- builder/README.md — DSL-style builders used by processors
- docs/ — additional plans and design notes
