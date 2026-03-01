/*
 * Copyright (c) 2025-2026. Kirill Shulzhenko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.kshulzh.kefir.model.api.modifiers

/**
 * Represents a collection of special Kotlin modifiers that extend the behavior or characteristics
 * of language elements such as functions or properties. These modifiers provide advanced semantics
 * for individuals using them in Kotlin programming.
 *
 * The modifiers defined in this enumeration extend [KtModifier], making them usable within the
 * general modifier system. They are often used in advanced contexts such as coroutines, functional
 * operations, annotations, or cross-platform support.
 */
enum class KtModifiers : KtModifier {
    /**
     * Represents the `suspend` modifier in Kotlin.
     *
     * The `suspend` modifier is used to define a special type of function, known as a suspending
     * function, which can be paused and resumed during execution. Suspending functions are typically
     * used in conjunction with Kotlin's coroutine framework to handle asynchronous or non-blocking
     * operations in a concise and readable manner.
     *
     * Functions marked with the `suspend` modifier can only be called from other suspending functions
     * or within a coroutine context. This modifier enforces structured concurrency and helps ensure
     * that asynchronous operations are properly managed.
     */
    SUSPEND,

    /**
     * Represents the `infix` modifier in Kotlin.
     *
     * The `infix` modifier is applied to member functions or extension functions to allow them
     * to be called using infix notation, providing a more readable and natural syntax for
     * certain function calls.
     *
     * Key characteristics:
     * - Functions marked with the `infix` modifier must be member functions or extension functions.
     * - Infix notation allows the function to be called without using parentheses or the dot operator.
     * - Functions with a single parameter are suited for this modifier.
     *
     * Commonly used for functions that improve code readability in domain-specific languages.
     */
    INFIX,

    /**
     * Represents the `inline` modifier in Kotlin.
     *
     * The `inline` modifier is applied to functions to instruct the compiler to perform inline
     * expansion during compilation. Instead of calling the function, the compiler replaces each
     * call to the function with the actual code of the function. This can improve performance
     * by eliminating function call overhead and enabling other optimizations like non-local
     * returns within lambdas passed to inline functions.
     *
     * Commonly used in scenarios where higher-order functions are involved, allowing the compiler
     * to remove the runtime overhead of lambda expressions.
     */
    INLINE,

    /**
     * Represents the `operator` modifier in Kotlin.
     *
     * The `operator` modifier is used to indicate that a function implements an operator overloading,
     * enabling the function to be invoked using operator expressions (e.g., `+`, `-`, `[]`, etc.).
     *
     * Functions marked with the `operator` modifier must adhere to specific naming conventions
     * and parameter types defined by Kotlin's operator overloading rules. By marking a function
     * as `operator`, it permits the use of concise syntactic constructs and provides readability
     * to code involving mathematical operations, comparisons, or custom indexed access.
     *
     * This modifier is applicable only to member and extension functions of a class, object, or interface.
     */
    OPERATOR,

    /**
     * Represents the `tailrec` modifier in Kotlin.
     *
     * The `tailrec` modifier is used to mark a function as tail-recursive, meaning that
     * the function's last operation is a call to itself. This allows the Kotlin compiler
     * to optimize such recursive calls and replace them with iterative loops, preventing
     * potential stack overflow errors for deeply recursive calls.
     *
     * Functions marked with `tailrec` must adhere to specific rules:
     * - The recursive call must be in the tail position.
     * - The function must not perform additional operations after the recursive call.
     *
     * If these conditions are not met, the compiler generates an error, ensuring the
     * safety and correctness of the optimization.
     */
    TAILREC,

    /**
     * Represents the `external` modifier in Kotlin.
     *
     * The `external` modifier is used to indicate that a declaration is implemented in an external, platform-specific
     * way, rather than in Kotlin code. Declarations marked with this modifier are typically used to interact with
     * non-Kotlin code, such as native libraries, JavaScript environments, or system-specific APIs.
     *
     * The use of this modifier is common in interoperability scenarios where Kotlin code relies on external
     * implementation details not defined within the Kotlin language itself.
     */
    EXTERNAL,

    /**
     * Represents the `annotation` modifier in Kotlin.
     *
     * The `annotation` modifier identifies a Kotlin annotation class, which can be used to annotate
     * declarations such as classes, functions, properties, or parameters. Annotation classes act
     * as metadata carriers and serve as a structured way to attach information to code that can be
     * processed at compile-time or runtime through reflection.
     *
     * An annotation class itself is defined with the `annotation` keyword and can specify parameters,
     * default values, and usage targets. Annotated declarations must comply with the rules and
     * constraints defined by the annotation class.
     *
     * This modifier is part of the [KtModifiers] enumeration and is used in managing and distinguishing
     * the specific types of Kotlin modifiers in abstract modeling or code transformation systems.
     */
    ANNOTATION,

    /**
     * Represents the `crossinline` modifier in Kotlin.
     *
     * The `crossinline` modifier is used in inline function parameters to prohibit
     * non-local returns from a lambda that is passed to the function. This ensures
     * that the provided lambda is entirely executed within its invocation and cannot
     * influence the control flow of the surrounding function.
     *
     * This modifier is particularly useful when the lambda is intended to be inlined
     * but might be called in a different context (such as another thread or within nested
     * functions), where allowing non-local returns could lead to runtime errors or
     * unexpected behavior.
     */
    CROSSINLINE,

    /**
     * Represents the `noinline` modifier in Kotlin.
     *
     * The `noinline` modifier is applied to a parameter of a higher-order function to indicate
     * that the parameter should not be inlined during compilation. By marking a lambda parameter
     * with `noinline`, the compiler will treat it as a regular object, avoiding the inlining optimization.
     *
     * This is typically used for cases where inlining would lead to undesirable behavior, such as
     * when the parameter needs to be passed around as a function reference or stored beyond the
     * scope of the caller.
     */
    NOINLINE,

    /**
     * Represents the `reified` modifier in Kotlin.
     *
     * The `reified` modifier is applicable only to type parameters of inline functions.
     * It allows the type parameter to carry its runtime type information, enabling operations
     * that usually require reflection, such as type checking, casting, or accessing type information,
     * to be executed without the use of reflection.
     *
     * Using the `reified` modifier mandates that the function it modifies is also marked as `inline`.
     *
     * Common contexts for usage:
     * - Performing type checks (`if (value is T)`)
     * - Casting to the type of `T` (`value as T`)
     * - Accessing the `KClass` of the type parameter (`T::class`)
     *
     * Limitations:
     * - Only applicable in inline functions.
     * - Cannot be used with non-inline function contexts or where no runtime type info is available.
     */
    REIFIED,

    /**
     * Represents the `expect` modifier in Kotlin.
     *
     * The `expect` modifier is used to define a platform-independent declaration in multiplatform projects.
     * An `expect` declaration serves as a declaration placeholder, where the actual implementation is provided
     * using the `actual` modifier on the target platforms.
     *
     * Key characteristics of the `expect` modifier:
     * - It is used on declarations (classes, functions, properties, etc.) that are expected to have platform-specific implementations.
     * - The corresponding implementation on each platform must use the `actual` keyword, matching the signature of the `expect` declaration.
     * - `expect` declarations allow consistent API structures across different platforms.
     *
     * It is primarily used in Kotlin Multiplatform projects to share code and manage platform-specific differences effectively.
     */
    EXPECT,

    /**
     * Represents the `actual` modifier in Kotlin.
     *
     * The `actual` modifier is used in conjunction with the `expect` modifier in Kotlin Multiplatform projects
     * to provide platform-specific implementations of declarations shared across multiple platforms.
     *
     * A declaration marked with `actual` corresponds to an `expect` declaration with the same signature,
     * ensuring that platform-specific behavior is defined for each target platform in the project.
     */
    ACTUAL
}