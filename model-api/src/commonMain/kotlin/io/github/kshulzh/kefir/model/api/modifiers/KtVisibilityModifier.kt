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
 * Represents the visibility modifiers in Kotlin that indicate the scope of accessibility
 * for classes, functions, properties, and other declarations.
 *
 * A visibility modifier defines the accessibility of a declaration within Kotlin's language
 * scoping rules. This enum models the possible visibility modifiers (`public`, `protected`,
 * `private`, and `internal`) that are used to control access levels in source code.
 *
 * Each constant corresponds to one of Kotlin's visibility keywords:
 * - PUBLIC: Accessible from any scope.
 * - PROTECTED: Accessible within the declaring class and its subclasses.
 * - PRIVATE: Accessible only within the declaring class or file.
 * - INTERNAL: Accessible within the same module.
 *
 * Implements the [KtModifier] interface as it is a specific category of Kotlin modifiers.
 */
enum class KtVisibilityModifier : KtModifier {
    /**
     * Represents the `public` visibility modifier in Kotlin.
     *
     * This modifier indicates that the associated element is accessible from any location.
     * It is the broadest visibility scope available in Kotlin.
     *
     * The `PUBLIC` modifier is typically associated with classes, functions, properties,
     * and other constructs to denote that they have no access restrictions.
     */
    PUBLIC,

    /**
     * Indicates the `protected` visibility modifier in Kotlin.
     *
     * The `protected` modifier restricts visibility to the containing class and its subclasses.
     * This is typically used for properties, functions, or constructors that are intended
     * to be accessible only within a specific inheritance hierarchy, encouraging encapsulation
     * while allowing controlled extensibility.
     *
     * Part of the `KtVisibilityModifier` enum, which represents the visibility-related modifiers
     * in Kotlin's abstract syntax model.
     */
    PROTECTED,

    /**
     * Represents the `private` visibility modifier in Kotlin.
     *
     * The `private` modifier restricts visibility to the containing class or file. In the case of
     * a top-level declaration, it is only accessible within the same file. For class members,
     * it ensures accessibility exclusively within the containing class instance.
     *
     * This modifier is crucial for encapsulation, offering a way to protect implementation details
     * and prevent unintended external access or modification. It typically aids in maintaining
     * code integrity and improving maintainability by adhering to encapsulation principles.
     */
    PRIVATE,

    /**
     * Represents the `internal` visibility modifier in Kotlin.
     *
     * The `internal` visibility restricts access to members within the same module.
     * It allows defining elements that should not be publicly exposed but
     * are shared across multiple files in the same module.
     *
     * This modifier is applicable to classes, functions, properties, and other
     * declarations within a Kotlin program. It plays a role in modularizing code
     * and encapsulating functionality without exposing it outside the module boundary.
     */
    INTERNAL
}

/**
 * Represents the visibility modifier associated with the current `KtModifierScope`.
 *
 * This property allows getting or setting the visibility modifier of the scope. If a visibility
 * modifier (e.g., `PUBLIC`, `PRIVATE`, `PROTECTED`, or `INTERNAL`) exists within the scope's
 * `modifiers` set, it is returned. If no visibility modifier has been defined, `null` is returned.
 *
 * When setting a new visibility modifier, any existing visibility modifier within the scope will
 * be removed, and the new modifier (if not `null`) will be added to the `modifiers` set. This ensures
 * that the `modifiers` collection contains at most one visibility modifier at any time.
 */
var KtModifierScope.visibility: KtVisibilityModifier?
    get() = modifiers.filterIsInstance<KtVisibilityModifier>().firstOrNull()
    set(value) {
        visibility?.also {
            modifiers.remove(it)
        }

        value?.also {
            modifiers.add(it)
        }
    }