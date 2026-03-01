# Modules overview

This repository is composed of several Gradle modules. Here is a quick guide to what each one does:

- [api](../api/README.MD) — Public-facing APIs used by processors and integrations.
- [builder](../builder/README.MD) — Kotlin DSL/builders to construct model elements conveniently.
- [compiler-plugin](../compiler-plugin/README.MD) — The Kotlin compiler plugin entry point that wires Kefir into the
  compiler.
- [gradle-plugin](../gradle-plugin/README.MD) — Gradle plugin that applies and configures Kefir in user projects.
- [ir-helper](../ir-helper/README.MD) — Helper utilities for working with Kotlin IR.
- [kefir-tools](../kefir-tools/README.MD) — Development helpers and utilities for building processors with Kefir.
- [model](../model/README.MD) — Core, common model of declarations, statements, and expressions.
- [model-api](../model-api/README.MD) — Public API for the model used by processors.
- [model-ir](../model-ir/README.MD) — IR-specific model and utilities.
- [model-utils](../model-utils/README.MD) — Shared utilities around the model (collections, helpers, etc.).
- [transform-ir](../transform-ir/README.MD) — Transformations that target Kotlin IR.
- [test](../test/README.MD) — Sample tests and usage examples.
