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

package io.github.kshulzh.kefir.model.api.type

import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.utils.KtVisitor

/**
 * Represents a scope that holds type arguments in a Kotlin type system context.
 *
 * This interface defines a structure to manage and access type argument elements
 * associated with a type. Type arguments typically represent specific type substitutions
 * or constraints applied to generic types.
 *
 * It extends [KtElement] to include capabilities for visitor pattern processing, enabling
 * traversal and operation on its type argument elements in various contexts such as analysis,
 * transformation, or validation.
 */
interface KtTypeArgumentScope : KtElement {
    /**
     * A mutable list of type elements representing type arguments in a generic type declaration.
     *
     * This list contains instances of [KtTypeElement], which may represent different types such as
     * base types, user-defined types, or other complex type constructs. These type arguments are
     * typically used within contexts such as generic type declarations, type parameter bindings,
     * or function type signature definitions.
     *
     * The `typeArguments` property serves as a central point for accessing or modifying the type
     * arguments within a specific type-related scope, enabling comprehensive type modeling,
     * transformation, or validation processes.
     */
    val typeArguments: MutableList<KtTypeElement>

    /**
     * Accepts a visitor to process this `KtTypeArgumentScope`.
     *
     * @param visitor The visitor instance to process this element.
     * @param data Additional data passed to the visitor.
     * @return The result of processing this element by the visitor.
     */
    override fun <R, D> accept(visitor: KtVisitor<R, D>, data: D): R = visitor.visitTypeArgumentScope(this, data)
}