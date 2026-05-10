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
import io.github.kshulzh.kefir.model.api.declaration.KtConstructorElement
import io.github.kshulzh.kefir.model.api.declaration.KtDeclarationsScope
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.modifiers.KtModifier
import io.github.kshulzh.kefir.model.api.type.KtTypeParameterElement

/**
 * Represents an implementation of a Kotlin constructor element within the abstract syntax tree (AST).
 *
 * This class serves as a concrete implementation of the [KtConstructorElement] interface, providing
 * structure and behavior for Kotlin constructor elements. It also implements the [KtAttributes]
 * interface to support the addition of dynamic attributes.
 *
 * A constructor element is a specialized declaration that encapsulates the logic necessary to
 * initialize an instance of the corresponding class. It may include a body of code, declarations
 * accessible within its scope, and a set of modifiers that describe its properties.
 *
 * @property body The body of the constructor, represented as an optional [KtExpressionElement].
 * @property declarationsScope The scope of declarations associated with the constructor, or `null` if undefined.
 * @property name The name of the constructor element, defaulting to "<constructor>".
 * @property modifiers A mutable set of [KtModifier] representing the modifiers applied to the element.
 * @property attributes A mutable map for attaching additional metadata or custom properties to the constructor element.
 */
class KtConstructorElementImpl(
    override var body: KtExpressionElement?,
    override var declarationsScope: KtDeclarationsScope?,
    override var name: KtName = "<counstructor>",
    override var modifiers: MutableSet<KtModifier> = mutableSetOf(),
    override val annotations: MutableList<KtAnnotationElement> = mutableListOf(),
    override var attributes: MutableMap<String, Any> = mutableMapOf(),
    override var typeParameters: MutableList<KtTypeParameterElement> = mutableListOf(),
) : KtConstructorElement, KtAttributes {
    override fun toString(): String = "<CONSTRUCTOR>"
}