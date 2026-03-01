# Kefir Builder module

The Kefir Builder module provides a small, fluent Kotlin DSL for constructing Kefir model elements (packages, files,
declarations, types, and statements). It is intended for use inside Kefir processors to describe what should be
generated or transformed without interacting with compiler IR directly.

What it provides (high level):

- io builders: Package, File, and io.Class navigation helpers.
- declaration builders: Class, Constructor, Fun, Property accessors (Getter/Setter), Body.
- expression builders: variables, calls, literals (e.g., str("text")), Set/Field/Param helpers, CallDelegate.
- statement builders: St(...), Return(...), If/Else helpers, etc.
- type helpers: Type(...), predefined KtBaseTypeElements (e.g., UNIT, STRING).

Typical usage
Inside your KtProcessor implementation you receive a KtPackageScope and can use the builder DSL to define content to
generate:

```kotlin
// Inside your KtProcessor: fun KtPackageScope.generate(ktContext: KtContext) {
fun KtPackageScope.generate(ktContext: KtContext) {
    Package("com.example.generated") {
        File("Hello.kt") {
            Class("Hello") {
                Constructor {
                    Body {
                        // delegate to Any() ctor as an example
                        St(
                            CallDelegate(
                                ktContext.external!!.Package("kotlin")
                                    .Class("Any")!!.declarations
                                    .filterIsInstance<KtConstructorElement>()
                                    .first()
                            )
                        )
                    }
                }

                Fun("greet") {
                    type = KtBaseTypeElements.UNIT
                    Body {
                        // add statements here
                    }
                }
            }
        }
    }
}
```

Gradle coordinates

- Group: io.github.kshulzh.kefir
- Artifact: builder
- Version: 0.0.1 (or the current project version)

Gradle dependency example (in your processor module):

```kotlin
dependencies {
    implementation("io.github.kshulzh.kefir:api:0.0.1")
    implementation("io.github.kshulzh.kefir:builder:0.0.1")
}
```

Notes

- The DSL is annotated with a KefirDslMarker to reduce accidental scope leaks between nested builders.
- Some symbols may be provided by model-api (Kt*Element interfaces) that the builder instantiates or references; ensure
  you also depend on matching versions of api/model modules if you use those types directly.
- The io.Class helper is a lookup/navigation utility for classes from external packages; the declaration.Class builder
  creates new classes.

See also

- api/README.md — registering processors and high-level API
- docs/ — plans and design notes