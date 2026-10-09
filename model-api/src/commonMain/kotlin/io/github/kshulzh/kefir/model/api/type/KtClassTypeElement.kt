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

package io.github.kshulzh.kefir.model.api.type

import io.github.kshulzh.kefir.model.api.KtPath
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationsScope
import io.github.kshulzh.kefir.model.api.declaration.KtClassElement
import io.github.kshulzh.kefir.model.api.utils.KtVisitor

/**
 * Represents a model for a Kotlin class type within the type system.
 *
 * This interface is an extension of `KtTypeElement` and is used to model
 * Kotlin class types, including their associated package, class name, nullability,
 * and type arguments.
 */
interface KtClassTypeElement : KtTypeElement, KtTypeArgumentScope, KtAnnotationsScope {
    /**
     * Represents the package path of a class type element in Kotlin.
     * It is modeled using the `KtPath` type, which stores the fully qualified path
     * as a list of components (`KtName` values). This property is used in various
     * transformation contexts, such as Intermediate Representation (IR) and
     * Frontend Intermediate Representation (FIR), to resolve the package
     * information for a class type element.
     */
    val ktPackage: KtPath

    /**
     * Represents the class path of the target type in the Kotlin type model.
     *
     * This immutable value is an instance of [KtPath] and is part of the `KtClassTypeElement` interface,
     * providing the path hierarchy to the class definition. It is utilized during type transformations
     * and intermediate representations (IR/FIR) to derive or construct type information.
     *
     * The path is typically represented in a dot-separated string format, indicating the structural
     * hierarchy of the class within its package namespace.
     */
    val ktClass: KtPath

    /**
     * Indicates whether the type represented by this instance allows nullability.
     *
     * If `true`, the type is nullable and can represent `null` as a valid value.
     * If `false`, the type is non-nullable and cannot accept `null` as a valid value.
     *
     * This property is useful for type transformations and validations,
     * especially when interacting with Kotlin's nullable and non-nullable type system.
     */
    val isNullable: Boolean

    /**
     * Represents the list of type arguments associated with a type element.
     *
     * Each type argument is an instance of [KtTypeElement] and provides details about
     * a specific type parameter used in generic type constructs. This property is mutable,
     * allowing modifications to the type arguments for scenarios such as transformation or mapping.
     *
     * Commonly utilized within type transformation contexts, such as transforming
     * intermediate representations of types in both IR (Intermediate Representation) and FIR
     * (Frontend Intermediate Representation).
     */
    override var typeArguments: MutableList<KtTypeElement>

    val klass: KtClassElement?

    override fun <R, D> accept(visitor: KtVisitor<R, D>, data: D): R = visitor.visitClassType(this, data)
}