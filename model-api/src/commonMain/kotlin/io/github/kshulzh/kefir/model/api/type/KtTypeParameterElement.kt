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

import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationsScope
import io.github.kshulzh.kefir.model.api.utils.KtVisitor

/**
 * Represents a type parameter in the Kotlin type system.
 *
 * This interface extends [KtTypeElement] and [KtAnnotationsScope], providing a base
 * abstraction for type parameters in Kotlin. A type parameter can have a name,
 * a variance modifier (`in`, `out`, or `invariant`), and a set of upper bounds
 * specifying its restrictions. It also allows associating annotations with the
 * type parameter and offers a scope for type parameter-specific operations.
 */
interface KtTypeParameterElement: KtTypeElement, KtAnnotationsScope  {
    /**
     * Represents the name associated with an object or entity.
     * This variable is typically used to store a descriptive identifier
     * or designation of a person, place, or thing.
     */
    var name: String
    /**
     * Holds a mutable collection of type elements representing the supertypes of a type parameter.
     *
     * This property is part of the `KtTypeParameterElement` interface, and it defines
     * the supertypes to which the type parameter is constrained. The list consists of
     * `KtTypeElement` instances, which represent various type constructs in the Kotlin type system.
     *
     * The supertypes defined in this list are used for type validation, resolution,
     * and other type hierarchy-related operations. Modifications to this list directly
     * affect the constraints on the type parameter.
     */
    var supperTypes: MutableList<KtTypeElement>
    /**
     * Defines the scope that contains type parameters associated with a specific Kotlin type.
     *
     * This variable holds an optional reference to a [KtTypeParameterScope], which provides
     * information about the type parameters declared within the context of a Kotlin type element.
     * The scope may include metadata such as the list of type parameters and their constraints.
     *
     * Commonly used in scenarios where type parameter resolution, validation, or analysis is
     * required, such as during intermediate representation (IR) or frontend type processing (FIR).
     */
    var typeParameterScope: KtTypeParameterScope?
    /**
     * Specifies the variance of the type parameter.
     *
     * Variance determines how a type parameter can be used in the context of type projections.
     * It allows for restrictions or flexibility in assigning types, aiding in type safety and correctness.
     *
     * Possible values:
     * - `INVARIANT`: The type parameter is used without any variance specification, meaning it operates in a strict, non-flexible manner.
     * - `IN`: The type parameter is contravariant, allowing it to accept supertypes of the specified type.
     * - `OUT`: The type parameter is covariant, allowing it to accept subtypes of the specified type.
     *
     * This property is utilized in scenarios involving type declarations and type boundaries.
     */
    var variance: Variance

    /**
     * Accepts a [KtVisitor] that processes this [KtTypeParameterElement].
     *
     * @param visitor The visitor instance that will process this element.
     * @param data Additional data to be passed to the visitor during processing.
     * @return The result of the visitor's operation on this element.
     */
    override fun <R, D> accept(visitor: KtVisitor<R, D>, data: D): R = visitor.visitTypeParameter(this, data)
}