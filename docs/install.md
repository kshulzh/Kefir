# Install and use Kefir

This guide shows how to apply the Gradle plugin, add your processor, and register Kefir extensions.

## 1) Apply the Gradle plugin in a project

```kotlin
plugins {
    id("io.github.kshulzh.kefir") version "0.0.1"
}

dependencies {
    // Your processor published to mavenLocal or a repository
    kefir("your:processor:1.0.0")
    // Or include a local module
    // kefir(project(":processor"))
}
```

## 2) Create a processor module

build.gradle.kts

```kotlin
plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("compiler-embeddable"))
    implementation("io.github.kshulzh.kefir:api:0.0.1")
    implementation("io.github.kshulzh.kefir:model-api:0.0.1")
    implementation("io.github.kshulzh.kefir:model:0.0.1")
    implementation("io.github.kshulzh.kefir:builder:0.0.1")
    implementation("io.github.kshulzh.kefir:transform-ir:0.0.1") // optional if you work with IR transforms
    compileOnly("io.github.kshulzh.kefir:kefir-tools:0.0.1")
    testImplementation(kotlin("test"))
}
```

## 3) Register extensions

YourKefirComponentRegistrar.kt

```kotlin
class YourKefirComponentRegistrar : KefirComponentRegistrar() {
    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        // Option A: provide a top-level DSL function to run
        registerIrExtension(KtPackageScope::main)

        // Option B: or register a KtProcessor implementation directly
        registerIrExtension(KtProcessorImpl())
    }
}
```

Main.kt (the DSL entry point used in Option A)

```kotlin
fun KtPackageScope.main(ktContext: KtContext) {
    Package("com.example") {
        File("D.kt") {
            Class("D") {
                Constructor {
                    Body {
                        St(
                            CallDelegate(
                                ktContext.external!!.Package("kotlin")
                                    .Class("Any")!!.declarations.filterIsInstance<KtConstructorElement>()
                                    .first()
                            )
                        )
                    }
                }
                Fun("foo") {
                    type = KtBaseTypes.UNIT
                    Body {}
                }
            }
        }
    }
}
```

KtProcessorImpl.kt (Option B)

```kotlin
class KtProcessorImpl : KtProcessor {
    override fun KtPackageScope.process(ktContext: KtContext) {
        Package("sample2") {
            File("C.kt") {
                Class("C") {
                    Fun("foo", mutableListOf()) {
                        Body { Return(Const("Hello World!")) }
                    }
                    Fun("foo2") { body = Const("Hello World! 2") }
                    Var("b", KtBaseTypes.STRING) {
                        Getter { Return(Field) }
                        Setter { Field Set Param("value") }
                    }
                }
                Class("B")
            }
        }
    }
}
```

## 4) Service registration

Create the service file so the compiler can discover your registrar:

resources/META-INF/services/io.github.kshulzh.kefir.compiler.plugin.KefirComponentRegistrar

```
example.YourKefirComponentRegistrar
```
