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

package io.github.kshulzh.kefir.model.declatation

import io.github.kshulzh.kefir.model.api.KtAttributes
import io.github.kshulzh.kefir.model.api.KtName
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.declaration.KtDeclarationsScope
import io.github.kshulzh.kefir.model.api.declaration.KtFieldElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.modifiers.KtModifier
import io.github.kshulzh.kefir.model.api.type.KtTypeElement

/**
 * Implementation of the [KtFieldElement] interface representing a Kotlin field element
 * in the abstract model hierarchy. This class provides concrete behavior for managing
 * information related to fields such as their name, type, value, modifiers, annotations,
 * and attributes.
 *
 * The `KtFieldElementImpl` class extends [KtAttributes] to support dynamic metadata
 * through attributes. It models fields with optional type and value definitions,
 * and integrates them into a declaration's scope.
 *
 * @property name Represents the name of the field.
 * @property type The type element associated with the field. Can be null if the field
 * does not explicitly declare a type.
 * @property value The value assigned to the field, represented as an expression element.
 * Null if the field does not have an initial value.
 * @property declarationsScope The scope within which the field declarations are valid.
 * Can be null for fields without a specific declaration scope.
 * @property modifiers A mutable set of modifiers applied to the field, enabling specification
 * of visibility, mutability, or other characteristics.
 * @property annotations A list of annotations associated with the field, allowing runtime or
 * compile-time metadata to be attached.
 * @property attributes A mutable map for storing custom or dynamic metadata associated
 * with the field.
 */
class KtFieldElementImpl(
    override var name: KtName,
    override var type: KtTypeElement? = null,
    override var value: KtExpressionElement? = null,
    override var declarationsScope: KtDeclarationsScope? = null,
    override var modifiers: MutableSet<KtModifier> = mutableSetOf(),
    override var annotations: MutableList<KtAnnotationElement> = mutableListOf(),
    override var attributes: MutableMap<String, Any> = mutableMapOf()
) : KtFieldElement, KtAttributes {
    override fun toString() = "<FIELD> $name"
}