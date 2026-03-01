# Kefir Compiler Plugin (processor)

This module contains the Kefir compiler plugin entry point and glue code that discovers and runs your Kefir processors
during Kotlin compilation.

What it is

- A Kotlin compiler plugin packaged as a jar.
- Exposes a ComponentRegistrar (based on KefirComponentRegistrar) that wires up IR/transform passes and invokes
  registered KtProcessor implementations.
- Uses Java ServiceLoader to discover your registrar classes at build time.

When you need it

- You are writing a processor (code generation or transformation) that uses Kefir’s API/Builder/Model.
- You want the processor to run automatically during the Kotlin compilation of your project.

Using the plugin in another project
Option A: Use a published artifact

- Publish this module to your Maven repository (local or remote).
- In a consumer project, add the jar to the Kotlin compiler plugin classpath.

Version compatibility

- Kotlin: JDK 17+, Kotlin 2.0+ (keep aligned with this repository’s version).
- Keep versions of kefir:api, kefir:builder, and kefir:model* consistent.

See also

- api/README.md — writing processors and registration overview
- builder/README.md — DSL for building model elements
- docs/ — design notes and plans
