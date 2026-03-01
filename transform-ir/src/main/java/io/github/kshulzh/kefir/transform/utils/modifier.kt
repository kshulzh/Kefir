/*
 * Copyright (c) 2026. Kirill Shulzhenko
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

package io.github.kshulzh.kefir.transform.utils

import io.github.kshulzh.kefir.model.api.modifiers.*
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.descriptors.DescriptorVisibility
import org.jetbrains.kotlin.descriptors.Modality


/**
 * Provides a mapping between a [KtVisibilityModifier] and its corresponding [DescriptorVisibility].
 *
 * The property maps visibility modifiers used in Kotlin source code to their respective representations
 * in the Kotlin compiler's descriptor model. This allows for a unified representation of visibility
 * across different stages of the compilation process.
 *
 * The mapping includes:
 * - `PUBLIC` to `DescriptorVisibilities.PUBLIC`
 * - `PRIVATE` to `DescriptorVisibilities.PRIVATE`
 * - `PROTECTED` to `DescriptorVisibilities.PROTECTED`
 * - `INTERNAL` to `DescriptorVisibilities.INTERNAL`
 *
 * This property is a read-only computed property and is determined based on the value of the
 * associated [KtVisibilityModifier] instance.
 */
val KtVisibilityModifier.descriptorVisibility: DescriptorVisibility
    get() = when (this) {
        KtVisibilityModifier.PUBLIC -> DescriptorVisibilities.PUBLIC
        KtVisibilityModifier.PRIVATE -> DescriptorVisibilities.PRIVATE
        KtVisibilityModifier.PROTECTED -> DescriptorVisibilities.PROTECTED
        KtVisibilityModifier.INTERNAL -> DescriptorVisibilities.INTERNAL
    }

/**
 * An extension property for `KtModifierScope` that retrieves the `DescriptorVisibility`
 * corresponding to the visibility modifier within the current scope.
 *
 * This property acts as a utility to interpret and extract the descriptor-level visibility
 * information, if it is available, from the underlying `visibility` modifier of the scope.
 *
 * @receiver The `KtModifierScope` instance for which the visibility descriptor is being accessed.
 * @return The `DescriptorVisibility` associated with the visibility modifier, or `null`
 *         if the visibility modifier is not present or cannot be resolved.
 */
val KtModifierScope.visibilityDescriptor: DescriptorVisibility? get() = visibility?.descriptorVisibility

/**
 * Retrieves the corresponding [Modality] value for a given [KtModalityModifier].
 *
 * Maps the Kotlin modality modifier (such as `final`, `open`, `abstract`, or `sealed`)
 * to its equivalent representation in the [Modality] enumeration.
 *
 * - [KtModalityModifier.FINAL] maps to [Modality.FINAL].
 * - [KtModalityModifier.OPEN] maps to [Modality.OPEN].
 * - [KtModalityModifier.ABSTRACT] maps to [Modality.ABSTRACT].
 * - [KtModalityModifier.SEALED] maps to [Modality.SEALED].
 *
 * This property allows seamless conversion between [KtModalityModifier] and [Modality] values.
 */
val KtModalityModifier.modalityModifier: Modality
    get() = when (this) {
        KtModalityModifier.FINAL -> Modality.FINAL
        KtModalityModifier.OPEN -> Modality.OPEN
        KtModalityModifier.ABSTRACT -> Modality.ABSTRACT
        KtModalityModifier.SEALED -> Modality.SEALED
    }

/**
 * An extension property that retrieves the `modality` modifier associated with the current
 * `KtModifierScope`, if available.
 *
 * The `modalityModifier` provides access to the specific modifier that represents the modality of
 * the associated Kotlin code construct (e.g., `final`, `open`, `abstract`, or `sealed`). This property
 * interacts with the internal `modality` property of `KtModifierScope` and extracts the relevant
 * `modalityModifier`.
 *
 * @receiver The `KtModifierScope` instance on which this property is accessed.
 * @return The `modality` modifier as a `Modality` instance, or `null` if it is not defined within
 *         the current scope.
 */
val KtModifierScope.modalityModifier: Modality? get() = modality?.modalityModifier