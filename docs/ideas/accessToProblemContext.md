# Access to problem context

Goal: Allow referencing elements that do not exist yet during DSL authoring and resolve them later when they become
available.

Why: When constructing code with the Kefir DSL, you may want to connect to a function/class/constructor that hasn't been
declared/created yet. A deferred reference mechanism makes iteration easier and avoids strict ordering constraints.

Proposal:

- Introduce a lightweight placeholder/reference type (e.g., PendingRef<T>) that can be created from a name/signature.
- Provide resolution hooks to bind PendingRef to the real element once it exists in the model (post-processing phase).
- Provide helpful diagnostics when a placeholder can't be resolved.

Sketch:

```kotlin
val funRef = PendingRef.ofFunction("com.example.Foo.bar", params = listOf(KtBaseTypeElements.STRING))

Fun("caller") {
    Body {
        Call(funRef) {
            Arg(str("value"))
        }
    }
}

// Later, when Foo.bar is created/loaded, the system resolves funRef -> actual declaration.
```

Acceptance criteria:

- Users can create placeholders for not-yet-existing declarations.
- Placeholders are resolved automatically after model assembly or fail with a clear error.
- Resolution is stable and cached; subsequent accesses receive the concrete element.