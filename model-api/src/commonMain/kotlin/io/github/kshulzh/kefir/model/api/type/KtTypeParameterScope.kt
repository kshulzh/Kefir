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
 * Represents a scope that contains a collection of Kotlin type parameters.
 *
 * This interface provides functionality for interacting with type parameters
 * in a given scope. It extends the [KtElement] interface, inheriting behavior
 * for visitation through a [KtVisitor]. This allows for systematic traversal
 * and processing of elements within the type parameter scope.
 */
interface KtTypeParameterScope : KtElement {
    /**
     * A mutable list of type parameters within the current type parameter scope.
     *
     * This property represents the collection of [KtTypeParameterElement] instances that are
     * declared in the context of the enclosing [KtTypeParameterScope]. Each type parameter
     * can define constraints, variance, annotations, and other type-related metadata.
     *
     * The list is mutable, allowing dynamic modifications to add, remove, or update the type
     * parameters associated with the scope. These changes directly affect the definitions and
     * usages of type parameters in the corresponding scope.
     */
    val typeParameters: MutableList<KtTypeParameterElement>

    /**
     * Accepts a visitor that processes this type parameter scope.
     *
     * @param visitor The visitor instance that will process the type parameter scope.
     * @param data Additional data to provide context or carry information during the visiting process.
     * @return The result of processing the type parameter scope by the visitor.
     */
    override fun <R, D> accept(visitor: KtVisitor<R, D>, data: D): R = visitor.visitTypeParameterScope(this, data)
}