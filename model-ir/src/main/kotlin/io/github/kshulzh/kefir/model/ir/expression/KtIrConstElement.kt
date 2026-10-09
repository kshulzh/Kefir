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

package io.github.kshulzh.kefir.model.ir.expression

import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.expression.KtConstElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.transform.IrWrapper
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import org.jetbrains.kotlin.ir.expressions.IrConst

/**
 * Represents a Kotlin IR (Intermediate Representation) constant element within the transformation context.
 *
 * This class provides a specific implementation of [KtConstElement] and [IrWrapper] for handling constant
 * expressions in the Kotlin IR structure. It encapsulates an IR constant ([IrConst]) and provides utilities
 * for interacting with its value, type, and associated annotations.
 *
 * @property irElement The underlying [IrConst] element associated with this constant.
 * @property transformContext An instance of [KtTransformContext] providing the context for IR transformations.
 * @property parent The parent [KtElement], if any, to which this constant element belongs.
 */
class KtIrConstElement(
    override val irElement: IrConst,
    var transformContext: KtTransformContext,
    override var parent: KtElement? = null,
) : KtConstElement<Any>, IrWrapper<IrConst> {
    /**
     * Represents the value of the underlying IR constant element (`IrConst`) wrapped by this class.
     * This property provides access to the compile-time constant value associated with the IR element.
     * The value may be `null` if the IR element does not represent a valid constant.
     */
    override var value: Any? = irElement.value
    /**
     * Represents the type information associated with this element in the context of
     * Kotlin's type system. This property provides access to the underlying type details,
     * allowing for processing, transformation, or analysis of type-related information.
     *
     * The `type` property follows the contract defined by the [KtTypeElement] interface,
     * enabling interaction with the rich hierarchy of type abstractions in Kotlin.
     *
     * Notes:
     * - The getter for this property is not implemented and will throw a `NotImplementedError` if accessed.
     * - The setter is defined but does not currently perform any operations on the provided value.
     */
    override var type: KtTypeElement?
        get() = TODO("Not yet implemented")
        set(value) {}

    /**
     * A mutable list of `KtAnnotationElement` instances associated with the current element.
     *
     * This property is used to store annotation metadata, enabling retrieval
     * and manipulation of annotations linked to this element within the model structure.
     */
    override val annotations: MutableList<KtAnnotationElement> = mutableListOf()
}