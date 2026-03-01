# Common processing pipeline

Goal: Provide a unified transformation entry-point that can operate across backends (model-only, FIR, IR) with a
consistent API.

Background: A prototype exists, but proper FIR wrappers are still required for parity with the IR side.

Plan:

- Define a common ProcessingContext shared by backends (exposes project/module, logging, options, and symbol lookup).
- Add Updated io.github.kshulzh.kefir.compiler.plugin.KefirComponentRegistrar::registerCommon(processor: KtProcessor)

Acceptance criteria:

- Single entry-point (e.g., KtProcessor.process) works for model, FIR, and IR modes.
- FIR wrappers exist for the required element set to support feature parity with IR flows.
- Updated io.github.kshulzh.kefir.compiler.plugin.KefirComponentRegistrar
- Clear error/diagnostics if a feature is unavailable on a given backend.