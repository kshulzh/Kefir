# Implement remaining expressions

Goal: Add missing expression node types to the model, with transformers and FIR/IR wrappers where applicable.

Scope:

- Audit current expression coverage (literals, calls, lambda, when, try/catch, elvis, safe/unsafe call, property access,
  index, new, cast/as?, is, return/break/continue with labels, this/super, object, annotations on expressions, etc.).
- Implement the missing ones in the model with consistent naming and properties.
- Provide builders/DSL entries in the builder module.
- Add transformers and backend wrappers for IR (existing) and FIR (where planned).

Design notes:

- Keep nodes minimal; attach type info where it is already part of the model conventions.
- Ensure cross-links (e.g., labeled returns, receivers) are well-defined and participate in copy/deepCopy.
- Update visitors/transformers to include new cases with sensible defaults.

Acceptance criteria:

- Model compiles with the extended set of expressions.
- Builders/DSL can construct all new expressions ergonomically.
- IR and (planned) FIR wrappers support the new nodes or fail with a clear TODO diagnostic.
- Tests demonstrate construction and a simple transform for each new expression type.