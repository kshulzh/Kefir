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
 * Represents a set of modality modifiers that can be applied to classes, functions, or properties
 * in Kotlin. Modality modifiers define the behavior or usage of the associated element.
 *
 * This enumeration implements the [KtModifier] interface, making it usable within the modifier system.
 */
enum class KtModalityModifier : KtModifier {
    /**
     * Represents the `final` modality modifier in Kotlin.
     *
     * Denotes that a class, function, or property cannot be overridden or extended.
     */
    FINAL,

    /**
     * Represents the `sealed` modifier in Kotlin.
     *
     * The `sealed` modifier restricts the inheritance hierarchy of a class to a predefined set of subclasses,
     * ensuring all possible subclasses are known at compile-time. It can only be applied to classes, not to
     * functions or properties.
     *
     * A class marked as `sealed` is implicitly abstract and cannot be instantiated directly. Subclasses
     * of a sealed class must be defined in the same file as the sealed class to maintain exhaustive
     * and controlled hierarchy visibility.
     */
// NB: class can be sealed but not function or property
    SEALED,

    /**
     * Represents an `open` modifier that allows the annotated class, function, or property to be extended or overridden.
     */
    OPEN,

    /**
     * Represents the `abstract` modifier in Kotlin.
     *
     * The `abstract` modifier is used to indicate that a class or a member declaration is abstract.
     * Abstract classes cannot be instantiated directly and are intended to be extended by other classes.
     * Abstract member declarations (such as functions or properties) do not have an implementation in the abstract class
     * and must be overridden in derived classes, unless the derived class is also abstract.
     *
     * This modifier is commonly applied to:
     * - Classes: Marking them as abstract to enforce inheritance.
     * - Functions or properties within abstract classes: Indicating that these members must be implemented in subclasses.
     *
     * Example context: Modifiers like `abstract`, `final`, `sealed`, or `open` define the modality of a class or member in Kotlin.
     */
    ABSTRACT
}

/**
 * Represents the modality modifier of a [KtModifierScope].
 *
 * This variable allows getting or setting a [KtModalityModifier] for a [KtModifierScope].
 * When getting, it retrieves the first instance of [KtModalityModifier] from the `modifiers` set.
 * When setting, it removes the existing modality modifier (if any) and adds the new one to the `modifiers` set.
 *
 * If no modality modifier is present, the getter returns `null`.
 *
 * Supported modality modifiers correspond to the values defined in the [KtModalityModifier] enum.
 */
var KtModifierScope.modality: KtModalityModifier?
    get() = modifiers.filterIsInstance<KtModalityModifier>().firstOrNull()
    set(value) {
        modality?.also {
            modifiers.remove(it)
        }

        value?.also {
            modifiers.add(it)
        }
    }