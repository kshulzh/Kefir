# Parameters for plugins

Goal: Provide a simple, type-safe way to pass configuration parameters to Kefir processors/plugins from Gradle.

Why: Processors often need options (e.g., output dir, toggles). A first-class parameters API improves usability over
ad-hoc system properties.

Gradle DSL design:

```kotlin
kefir {
    // Global/shared options (optional)
    option("enableFir", true)

    // Named configuration for your plugin
    val yourPlugin by params {
        // Simple types
        string("packageName", "com.example.generated")
        bool("generateStubs", default = false)
        int("maxLineLength", default = 120)

        // Collections
        stringList("include", default = listOf("**/*.kt"))
        stringMap("replacements") { default = mapOf("Foo" to "Bar") }
    }
}
```

Processor side API:

```kotlin
class KtProcessorImpl : KtProcessor {
    override fun KtPackageScope.process(ktContext: KtContext) {
        val p = ktContext.options.forPlugin("yourPlugin")
        val pkg = p.string("packageName")
        val stubs = p.bool("generateStubs")
        val maxLen = p.int("maxLineLength")
        // use include/replacements as needed
    }
}
```

Implementation notes:

- Options are serialized from Gradle into compiler plugin options.
- Provide a thin wrapper over CompilerConfiguration to expose typed getters with defaults.
- Validate unknown/missing keys with clear diagnostics; allow env/system overrides.

Acceptance criteria:

- Gradle users can define plugin params with the kefir block.
- Processors can read typed options via ktContext.options.
- Reasonable defaults and validation with actionable error messages.