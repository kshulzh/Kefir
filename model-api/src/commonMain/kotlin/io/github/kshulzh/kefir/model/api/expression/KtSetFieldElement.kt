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

package io.github.kshulzh.kefir.model.api.expression

import io.github.kshulzh.kefir.model.api.declatation.KtFieldElement

/**
 * Represents a field-setting expression within the Kotlin abstract syntax tree (AST).
 *
 * A `KtSetFieldElement` models an expression that assigns a value to a field,
 * potentially with a receiver object. It is commonly used to represent assignment
 * operations, such as setting a property on an object.
 *
 * This interface extends [KtExpressionElement], inheriting its properties for type
 * and hierarchical AST placement.
 */
interface KtSetFieldElement : KtExpressionElement {
    /**
     * Refers to a field element in the Kotlin model structure within the context of this class.
     *
     * This variable represents a [KtFieldElement], which models fields in Kotlin classes or objects.
     * It provides access to and manipulation of the properties, type, and value associated with a given field.
     */
    var field: KtFieldElement

    /**
     * Represents the receiver expression for the given set field element.
     *
     * The `receiver` property defines the expression that provides the context or scope
     * for accessing a field or property. Within the abstract syntax tree (AST), it is a reference
     * to a [KtExpressionElement], indicating the element to which the field access is being applied.
     *
     * For example, in the expression `obj.field = value`, `obj` serves as the receiver for the field assignment.
     *
     * This property is nullable, allowing flexibility in scenarios where the receiver context may not
     * be explicitly defined or applicable (e.g., in cases of implicit receivers).
     */
    var receiver: KtExpressionElement?

    /**
     * Represents the expression used to assign or set a field value in a Kotlin abstract syntax tree (AST).
     *
     * This property is a nullable instance of [KtExpressionElement], which defines
     * the expression or value being assigned to the field. It is typically used in scenarios
     * where a specific value is being set or updated during code analysis or processing.
     *
     * It plays a key role in modeling constructs such as property setters or field assignments
     * in a Kotlin-based abstract syntax tree.
     */
    var value: KtExpressionElement?
}