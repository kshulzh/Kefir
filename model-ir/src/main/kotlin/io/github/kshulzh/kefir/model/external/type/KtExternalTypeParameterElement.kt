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

package io.github.kshulzh.kefir.model.external.type

import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.api.type.KtTypeParameterElement
import io.github.kshulzh.kefir.model.api.type.KtTypeParameterScope
import io.github.kshulzh.kefir.model.api.type.Variance
import io.github.kshulzh.kefir.model.external.KtExternalRootPackageElement
import io.github.kshulzh.kefir.model.external.annotation.wrapExternalAnnotations
import io.github.kshulzh.kefir.transform.FirWrapper
import org.jetbrains.kotlin.fir.declarations.FirTypeParameter

/**
 * Represents an external type parameter element in the Kotlin model structure.
 *
 * This class wraps a [FirTypeParameter] and provides access to its properties and related information
 * in the Kotlin type system. It is part of the intermediate representation and is specifically
 * designed for processing external models.
 *
 * @property firElement The [FirTypeParameter] instance that this element wraps.
 * @property root The root package element ([KtExternalRootPackageElement]) associated with this type parameter.
 * @property typeParameterScope The scope of type parameters defined within this type parameter element.
 * This scope is optional and can be used for transformations or analyses.
 */
class KtExternalTypeParameterElement(
    override val firElement: FirTypeParameter,
    val root: KtExternalRootPackageElement,
    override var typeParameterScope: KtTypeParameterScope? = null,
) : KtTypeParameterElement, FirWrapper<FirTypeParameter> {
    init {
        typeParams[firElement] = this
    }
    /**
     * Represents the name of the external type parameter element.
     *
     * This property provides access to the name of the `FirTypeParameter`
     * associated with this `KtExternalTypeParameterElement`. The `name` is
     * retrieved as a string from the underlying FIR (Frontend Intermediate Representation) element.
     *
     * Custom getter retrieves the name via `firElement.name.asString()`.
     *
     * Setting this property is currently not implemented and has no effect.
     */
    override var name: String
        get() = firElement.name.asString()
        set(value) {}
    /**
     * A mutable list of super types associated with the current type parameter element.
     *
     * This property represents the collection of type bounds defined for a Kotlin type parameter.
     * Each type bound is transformed into a corresponding `KtTypeElement` using the `wrapExternalType` function.
     * The resulting list is mutable, allowing further modifications to the super types.
     *
     * The underlying data is derived from the `firElement.bounds` property of the current `FirTypeParameter`
     * instance. Each bound that successfully maps to a `KtTypeElement` is included in the list.
     */
//todo I do not think that override existing class is a good idea
    override var supperTypes: MutableList<KtTypeElement> = firElement.bounds.mapNotNull { wrapExternalType(it, root) }.toMutableList()
    /**
     * Represents the variance of a type parameter.
     *
     * Variance defines how a type parameter can be used in a type-safe manner. It can take one of the following values:
     * - [Variance.INVARIANT]: The type parameter is invariant and cannot be substituted with a subtype or supertype.
     * - [Variance.IN]: The type parameter is covariant for its upper bound (can only be used as a supertype, typically in `in`-positions).
     * - [Variance.OUT]: The type parameter is contravariant for its lower bound (can only be used as a subtype, typically in `out`-positions).
     *
     * This property is an overridden implementation of the variance behavior of the containing type element.
     */
    override var variance: Variance
        get() = TODO("Not yet implemented")
        set(value) {}

    /**
     * Represents a mutable list of annotation elements associated with the type parameter.
     *
     * This property is lazily initialized and retrieves annotations from the underlying FIR element,
     * wrapping them into instances of `KtAnnotationElement` through the `wrapExternalAnnotations` utility method.
     *
     * The annotations list is mutable, allowing modifications to the annotations associated with
     * the type parameter in the Kotlin model structure.
     */
    override val annotations: MutableList<KtAnnotationElement> by lazy {
        wrapExternalAnnotations(firElement.annotations, this).toMutableList()
    }
}