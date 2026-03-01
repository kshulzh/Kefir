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

package io.github.kshulzh.kefir.model.api.declatation

import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationsScope
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.modifiers.KtModifierScope
import io.github.kshulzh.kefir.model.api.type.KtTypeElement

/**
 * Represents a field element in the Kotlin model structure.
 *
 * A `KtFieldElement` is a declaration element that models fields within a Kotlin class or object.
 * It provides functionality to specify the type, value, and associated modifiers or annotations for the field.
 * As part of the Kotlin model hierarchy, it integrates with scopes for both modifiers and annotations.
 */
interface KtFieldElement : KtDeclarationElement,
    KtModifierScope,
    KtAnnotationsScope {
    /**
     * Represents the type associated with a field element in the Kotlin model structure.
     *
     * This property defines the declared type of the field as a [KtTypeElement].
     * It can be used to determine or modify the type of the field within the associated
     * `KtFieldElement`. The type can be nullable, indicating that the field may or may not
     * have a declared type.
     */
    var type: KtTypeElement?

    /**
     * Represents the value assigned to a field element within a Kotlin declaration.
     *
     * This property holds an instance of [KtExpressionElement] which represents the expression
     * assigned to the field. The expression can be null, indicating that no value has been set
     * or that the field is uninitialized.
     *
     * The value may dynamically reflect the runtime state or the default value of the field
     * it represents. It is associated with the containing [KtFieldElement].
     */
    var value: KtExpressionElement?
}