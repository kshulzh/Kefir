# FIR-specific representations

Goal: Allow authors to attach or provide FIR-specific variants of elements when needed, while keeping the primary model
backend-agnostic.

Two styles to consider:

1) Inline FIR block inside the main declaration (keeps things colocated)

```kotlin
Class("A") {
    Fir(A) {
        // optional, backend-specific attributes
    }
}
```

2) Separate explicit FIR declarations (useful for stubs or when FIR differs significantly)

```kotlin
Class("A") { /* model-first definition */ }

FirClass("A") {
    // overrides or FIR-only properties
}
```

Design notes:

- Provide lightweight FIR wrappers that mirror the subset of features used by processors.
- Establish clear merge/precedence rules when both model and FIR sections define the same attribute.
- Keep FIR API optional so users who do not target FIR are not impacted.

Acceptance criteria:

- Users can supply FIR-specific data either inline or via dedicated Fir* elements.
- The processing pipeline recognizes and applies FIR data where appropriate.
- Conflicts are resolved deterministically and documented.