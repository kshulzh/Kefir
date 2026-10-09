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

import io.github.kshulzh.kefir.model.api.declaration.KtFieldElement
import io.github.kshulzh.kefir.model.api.utils.KtVisitor

/**
 * Represents an expression element that retrieves the value of a specified field.
 *
 * A `KtGetFieldElement` corresponds to accessing a field within a class or object
 * in the Kotlin abstract syntax tree (AST). This interface models the relationship
 * between the field being accessed and the optional receiver expression that provides
 * the context for the field access.
 *
 * It extends [KtExpressionElement], allowing for its integration into the hierarchical
 * structure of the Kotlin AST. As such, a `KtGetFieldElement` can have type information
 * and be nested within other expressions.
 */
interface KtGetFieldElement : KtExpressionElement {
    /**
     * Represents the field element within the context of a `KtGetFieldElement`.
     *
     * This property refers to the associated field of type [KtFieldElement] that is being accessed or
     * utilized in the current scope. It links to a specific field declaration in the Kotlin structure,
     * enabling inspection or modification of its properties such as type, value, annotations, or
     * modifiers.
     *
     * The field may represent a variable declared within a class, object, or other scope in the AST
     * structure for Kotlin code elements.
     */
    var field: KtFieldElement

    /**
     * Represents the optional receiver expression for a field access operation.
     *
     * This property refers to the expression element that acts as the receiver
     * for field access in the context of the abstract syntax tree (AST).
     * It is nullable, implying that the field access may not always require
     * an explicit receiver (e.g., for access within the same scope).
     *
     * In the context of a [KtGetFieldElement], the `receiver` can point to an
     * instance or object that owns the accessed field.
     *
     * This property establishes a hierarchical relationship between the
     * field and its accessing context.
     */
    var receiver: KtExpressionElement?

    override fun <R, D> accept(visitor: KtVisitor<R, D>, data: D): R = visitor.visitGetField(this, data)
}