# Code generation (text) module

Goal: Provide a module that renders the Kefir model to Kotlin source code text (Kotlin PSI not required), similar to how
we can transform to IR/FIR, but targeting plain .kt output.

Why: Some processors want to emit source files rather than modify IR. A text-based backend is simpler to adopt and can
be used in non-compiler contexts (e.g., codegen tasks in Gradle).

Scope:

- Input: model API (declarations, statements, expressions).
- Output: well-formatted Kotlin .kt files written to a target directory.
- No dependency on IntelliJ PSI; keep it lightweight.

Design notes:

- Pretty-printer with indentation, imports management, and line wrapping.
- Configurable formatting (indent size, max line length, newline style).
- Minimal semantic checks (leave validation to the compiler).

Acceptance criteria:

- Given a model tree, the module produces compilable .kt source files.
- Imports and indentation are handled consistently.
- Public API stable enough to be used from Gradle tasks.