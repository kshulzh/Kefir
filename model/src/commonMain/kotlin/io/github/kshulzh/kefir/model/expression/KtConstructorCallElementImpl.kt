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

package io.github.kshulzh.kefir.model.expression

import io.github.kshulzh.kefir.model.api.KtAttributes
import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.declatation.KtConstructorElement
import io.github.kshulzh.kefir.model.api.expression.KtConstructorCallElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.utils.createListNullableDelegate

/**
 * Represents an implementation of a constructor call element within the Kotlin abstract syntax tree (AST).
 *
 * This class is a concrete implementation of the [KtConstructorCallElement] interface, which models a specific
 * invocation of a constructor in Kotlin code. It also implements the [KtAttributes] interface, enabling the
 * storage and management of custom attributes or metadata associated with the constructor call.
 *
 * The `KtConstructorCallElementImpl` class provides properties to access the invoked constructor, the list
 * of arguments passed during the invocation, the parent element in the AST hierarchy, and a mutable map for
 * storing attributes. Additionally, it supports type information retrieval for the constructor call expression.
 *
 * @param constructor The [KtConstructorElement] representing the constructor being invoked.
 * @param arguments A mutable list of argument expressions as [KtExpressionElement] instances, with a default empty list.
 * @param parent The parent element of this constructor call in the AST, which may be null for root or unlinked elements.
 * @param attributes A map to store custom attributes or metadata associated with this constructor call.
 *
 * @see KtConstructorCallElement
 * @see KtConstructorElement
 * @see KtAttributes
 */
class KtConstructorCallElementImpl(
    override val constructor: KtConstructorElement,
    arguments: MutableList<KtExpressionElement?> = mutableListOf(),
    override var parent: KtElement? = null,
    override var attributes: MutableMap<String, Any> = mutableMapOf(),
) : KtConstructorCallElement, KtAttributes {
    /**
     * Represents the type information associated with this element.
     *
     * This property holds a reference to a [KtTypeElement], which provides metadata
     * about the data type or return type of the associated element in the Kotlin type system.
     * It can be null if no specific type information is defined. The property is
     * designed to support lazy initialization or deferred resolution in certain scenarios.
     */
    override var type: KtTypeElement?
        get() = TODO("Not yet implemented")
        set(value) {}

    /**
     * Represents the list of argument expressions in a constructor call element.
     *
     * This property is a mutable list containing optional [KtExpressionElement] instances,
     * representing the arguments passed to a constructor call in the Kotlin abstract syntax tree (AST).
     * Each expression element in the list holds a connection to its parent element within the AST,
     * facilitating hierarchical navigation.
     *
     * The property utilizes a delegated mechanism, provided by the `createListDelegate` utility,
     * to track changes to the argument list and to ensure proper updates to the parent-child
     * relationship within the AST structure.
     */
    override val arguments: MutableList<KtExpressionElement?> by createListNullableDelegate(
        arguments,
        KtExpressionElement::parent
    )
}