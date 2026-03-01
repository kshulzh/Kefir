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
 * Represents a scope for managing a collection of Kotlin language modifiers.
 *
 * This interface provides an abstraction for working with a set of modifiers
 * (`KtModifier`), enabling their manipulation in a scoped manner.
 * Modifiers in this context typically represent language features such as
 * visibility, modality, and special behavior applicable to classes,
 * functions, properties, and other Kotlin code constructs.
 *
 * By utilizing this scope, it is possible to define, examine, or modify the
 * set of associated modifiers within a specific context, ensuring clear and
 * organized modifier management.
 */
interface KtModifierScope {
    /**
     * Represents a mutable collection of `KtModifier` instances associated with a given
     * scope. This property allows managing, inspecting, or modifying the set of Kotlin
     * modifiers applied to a particular language construct.
     *
     * Each `KtModifier` in the set corresponds to a specific characteristic, such as
     * visibility or modality, that affects the behavior or accessibility of the associated
     * element. The modifiers stored here influence the semantics of the scoped construct
     * within the Kotlin language model.
     *
     * Modifications to this set directly alter the modifiers applied to the scope, and the
     * collection may be adjusted programmatically to reflect various language features.
     */
    var modifiers: MutableSet<KtModifier>
}