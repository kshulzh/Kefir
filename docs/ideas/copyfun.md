# Copy functions for model elements

Goal: Provide a safe deep-copy facility for all model element kinds (declarations, statements, expressions) in the model
module.

Why: Transformations often need to duplicate parts of the tree. A standardized copy API avoids ad-hoc cloning and
ensures references inside the subtree remain consistent.

Requirements:

- copy() for every concrete element type with the same semantics.
- Preserve parent-child relationships within the copied subtree.
- Support remapping of cross-references (e.g., Return targets, symbol refs) via a CopyContext/Mapping.

Design:

- Introduce CopyContext that stores a mapping from original -> copied nodes for elements that can be cross-referenced.
- During copy, register each newly created element in the context; resolve cross-links by consulting the mapping.
- Provide extension entry-points:
    - fun <T : KtElement> T.copy(): T
    - fun <T : KtElement> T.copy(context: CopyContext): T

Example (return target remap):

```kotlin
val original = Fun("f") { Body { val l = Label("L"); Return(to = l) } }
val copy = original.copy() // Return.to now points to the copied Label inside copy
```

Acceptance criteria:

- All element categories implement copy.
- Cross-references inside the copied subtree correctly point to the copied counterparts.
- Public API documented and covered by tests.